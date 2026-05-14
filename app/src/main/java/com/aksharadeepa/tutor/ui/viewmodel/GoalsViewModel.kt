package com.aksharadeepa.tutor.ui.viewmodel

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.aksharadeepa.tutor.data.local.UserPreferences
import com.aksharadeepa.tutor.data.local.entity.DailyGoalEntity
import com.aksharadeepa.tutor.data.repository.TutorRepository
import com.aksharadeepa.tutor.worker.WorkManagerHelper
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*

data class GoalsState(
    val reminderEnabled: Boolean = true,
    val reminderHour: Int = 18,
    val reminderMinute: Int = 0,
    val streakCount: Int = 0,
    val longestStreak: Int = 0,
    val isGoalCompletedToday: Boolean = false,
    val targetQuizzes: Int = 2,
    val targetChapters: Int = 1,
    val completedQuizzes: Int = 0,
    val completedChapters: Int = 0,
    val totalChaptersCompleted: Int = 0
)

class GoalsViewModel(
    private val context: Context,
    private val prefs: UserPreferences,
    private val repository: TutorRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(GoalsState())
    val uiState: StateFlow<GoalsState> = _uiState.asStateFlow()

    init {
        refresh()
        observeTodayGoal()
        observeTotalProgress()
    }

    private fun observeTodayGoal() {
        viewModelScope.launch {
            repository.getTodayGoalFlow().collectLatest { goal ->
                if (goal != null) {
                    val isCompleted = goal.completedQuizzes >= goal.targetQuizzes || goal.chaptersRead >= goal.targetChapters
                    _uiState.update { 
                        it.copy(
                            completedQuizzes = goal.completedQuizzes,
                            completedChapters = goal.chaptersRead,
                            isGoalCompletedToday = isCompleted
                        )
                    }
                }
            }
        }
    }

    private fun observeTotalProgress() {
        viewModelScope.launch {
            repository.getAllChaptersFlow().collectLatest { chapters ->
                val totalCompleted = chapters.count { it.isCompleted }
                _uiState.update { it.copy(totalChaptersCompleted = totalCompleted) }
            }
        }
    }

    fun refresh() {
        checkAndUpdateStreak()
        loadPrefs()
    }

    private fun loadPrefs() {
        _uiState.update {
            it.copy(
                reminderEnabled = prefs.reminderEnabled,
                reminderHour = prefs.reminderHour,
                reminderMinute = prefs.reminderMinute,
                streakCount = prefs.streakCount,
                longestStreak = prefs.longestStreak,
                targetQuizzes = prefs.globalTargetQuizzes,
                targetChapters = prefs.globalTargetChapters
            )
        }
    }

    private fun checkAndUpdateStreak() {
        val today = Calendar.getInstance()
        val todayStr = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(today.time)
        val yesterday = Calendar.getInstance().apply { add(Calendar.DAY_OF_YEAR, -1) }
        val yesterdayStr = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(yesterday.time)
        
        val lastDate = prefs.lastStudyDate
        
        if (lastDate != todayStr && lastDate != yesterdayStr && lastDate.isNotEmpty()) {
            prefs.streakCount = 0
        }
    }

    fun updateReminderSettings(enabled: Boolean, hour: Int, minute: Int) {
        prefs.reminderEnabled = enabled
        prefs.reminderHour = hour
        prefs.reminderMinute = minute
        loadPrefs()

        if (enabled) {
            WorkManagerHelper.scheduleDailyReminder(context, hour, minute)
        } else {
            WorkManagerHelper.cancelReminder(context)
        }
    }

    fun updateGoalTargets(quizzes: Int, chapters: Int) {
        prefs.globalTargetQuizzes = quizzes
        prefs.globalTargetChapters = chapters
        loadPrefs()
    }
}
