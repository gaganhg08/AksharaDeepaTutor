package com.aksharadeepa.tutor.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.aksharadeepa.tutor.data.local.entity.QuizResultEntity
import com.aksharadeepa.tutor.data.repository.TutorRepository
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class QuizViewModel(private val repository: TutorRepository) : ViewModel() {

    private val _uiState = MutableStateFlow(QuizUiState())
    val uiState: StateFlow<QuizUiState> = _uiState.asStateFlow()

    private var timerJob: Job? = null
    private var currentSubjectId: Int = -1
    private var currentChapterId: Int = -1
    private val askedQuestionIds = mutableListOf<Int>()

    fun startQuiz(subjectId: Int, chapterId: Int, isRetake: Boolean = false) {
        currentSubjectId = subjectId
        currentChapterId = chapterId
        if (!isRetake) {
            askedQuestionIds.clear()
        }
        viewModelScope.launch {
            val chapter = repository.getChapterById(chapterId)
            var questions = repository.getQuizQuestionsForChapter(chapterId, askedQuestionIds)
            
            // If we don't have enough unused questions left, reset history to allow repeats
            if (questions.size < 5) {
                askedQuestionIds.clear()
                questions = repository.getQuizQuestionsForChapter(chapterId, askedQuestionIds)
            }
            
            questions.forEach { askedQuestionIds.add(it.id) }
            
            val bestScore = repository.getBestScoreForChapter(chapterId).firstOrNull()
            val attempts = repository.getAttemptsForChapter(chapterId).firstOrNull() ?: 0
            
            _uiState.update { 
                QuizUiState(
                    questions = questions,
                    selectedAnswers = MutableList(questions.size) { null },
                    chapterTitle = chapter?.title ?: "Chapter Quiz",
                    subjectId = subjectId,
                    chapterId = chapterId,
                    bestScore = bestScore,
                    attempts = attempts
                ) 
            }
            startTimer()
        }
    }

    private fun startTimer() {
        timerJob?.cancel()
        timerJob = viewModelScope.launch {
            _uiState.update { it.copy(timeLeft = 30, showAnswerResult = false, currentSelectedOption = null) }
            while (_uiState.value.timeLeft > 0) {
                delay(1000L)
                _uiState.update { it.copy(timeLeft = it.timeLeft - 1) }
            }
            // Time out automatically selects null and handles failure gracefully
            handleAnswerSubmission(null)
        }
    }

    fun selectOption(option: String) {
        if (_uiState.value.showAnswerResult) return // Prevent multiple taps
        handleAnswerSubmission(option)
    }

    private fun handleAnswerSubmission(selectedOption: String?) {
        timerJob?.cancel()
        
        val currentState = _uiState.value
        if (currentState.currentIndex >= currentState.questions.size) return
        
        val currentQuestion = currentState.questions[currentState.currentIndex]
        val isCorrect = selectedOption == currentQuestion.correctAnswer

        val newAnswers = currentState.selectedAnswers.toMutableList()
        newAnswers[currentState.currentIndex] = selectedOption

        _uiState.update { 
            it.copy(
                currentSelectedOption = selectedOption,
                showAnswerResult = true,
                score = if (isCorrect) it.score + 1 else it.score,
                selectedAnswers = newAnswers
            ) 
        }

        // Pause for 4 seconds to show Red/Green UI feedback and AI Explanation before auto-advancing
        viewModelScope.launch {
            delay(4000L)
            moveToNextQuestion()
        }
    }

    private fun moveToNextQuestion() {
        val currentState = _uiState.value
        if (currentState.currentIndex < currentState.questions.size - 1) {
            _uiState.update { it.copy(currentIndex = it.currentIndex + 1) }
            startTimer()
        } else {
            finishQuiz()
        }
    }

    private fun finishQuiz() {
        val currentState = _uiState.value
        _uiState.update { it.copy(isFinished = true) }
        
        // Save final score natively to Room Database
        viewModelScope.launch {
            repository.saveQuizResult(
                QuizResultEntity(
                    subjectId = currentSubjectId,
                    chapterId = currentChapterId,
                    score = currentState.score,
                    totalQuestions = currentState.questions.size,
                    timestamp = System.currentTimeMillis()
                )
            )
        }
    }
}
