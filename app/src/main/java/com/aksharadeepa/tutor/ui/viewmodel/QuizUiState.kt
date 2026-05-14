package com.aksharadeepa.tutor.ui.viewmodel

import com.aksharadeepa.tutor.data.local.entity.QuestionEntity

data class QuizUiState(
    val questions: List<QuestionEntity> = emptyList(),
    val currentIndex: Int = 0,
    val timeLeft: Int = 30,
    val currentSelectedOption: String? = null,
    val showAnswerResult: Boolean = false,
    val score: Int = 0,
    val isFinished: Boolean = false,
    val selectedAnswers: MutableList<String?> = mutableListOf(),
    val chapterTitle: String = "",
    val subjectId: Int = -1,
    val chapterId: Int = -1,
    val bestScore: Int? = null,
    val attempts: Int = 0
)
