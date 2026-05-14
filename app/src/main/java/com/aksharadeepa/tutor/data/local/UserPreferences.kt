package com.aksharadeepa.tutor.data.local

import android.content.Context
import android.content.SharedPreferences

class UserPreferences(context: Context) {
    private val prefs: SharedPreferences = context.getSharedPreferences("tutor_prefs", Context.MODE_PRIVATE)

    var reminderEnabled: Boolean
        get() = prefs.getBoolean("reminder_enabled", true)
        set(value) = prefs.edit().putBoolean("reminder_enabled", value).apply()

    var reminderHour: Int
        get() = prefs.getInt("reminder_hour", 18) // Default 6 PM
        set(value) = prefs.edit().putInt("reminder_hour", value).apply()

    var reminderMinute: Int
        get() = prefs.getInt("reminder_minute", 0)
        set(value) = prefs.edit().putInt("reminder_minute", value).apply()

    var isOnboardingCompleted: Boolean
        get() = prefs.getBoolean("onboarding_completed", false)
        set(value) = prefs.edit().putBoolean("onboarding_completed", value).apply()

    var streakCount: Int
        get() = prefs.getInt("streak_count", 0)
        set(value) = prefs.edit().putInt("streak_count", value).apply()

    var lastStudyDate: String
        get() = prefs.getString("last_study_date", "") ?: ""
        set(value) = prefs.edit().putString("last_study_date", value).apply()

    var longestStreak: Int
        get() = prefs.getInt("longest_streak", 0)
        set(value) = prefs.edit().putInt("longest_streak", value).apply()

    var globalTargetQuizzes: Int
        get() = prefs.getInt("target_quizzes", 2)
        set(value) = prefs.edit().putInt("target_quizzes", value).apply()

    var globalTargetChapters: Int
        get() = prefs.getInt("target_chapters", 1)
        set(value) = prefs.edit().putInt("target_chapters", value).apply()
}
