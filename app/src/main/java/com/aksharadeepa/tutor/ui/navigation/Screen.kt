package com.aksharadeepa.tutor.ui.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Book
import androidx.compose.material.icons.filled.PieChart
import androidx.compose.material.icons.filled.Settings
import androidx.compose.ui.graphics.vector.ImageVector

sealed class Screen(val route: String, val title: String? = null, val icon: ImageVector? = null) {
    object Splash : Screen("splash")
    object Onboarding : Screen("onboarding")
    object Home : Screen("home", "Home", Icons.Default.Home)
    object Subjects : Screen("subjects", "Subjects", Icons.Default.Book)
    object Analytics : Screen("analytics", "Analytics", Icons.Default.PieChart)
    object Settings : Screen("settings", "Settings", Icons.Default.Settings)
    object DailyGoals : Screen("goals")
    object Quiz : Screen("quiz/{subjectId}/{chapterId}") {
        fun createRoute(subjectId: Int, chapterId: Int) = "quiz/$subjectId/$chapterId"
    }
    object QuizResult : Screen("quiz_result/{score}") {
        fun createRoute(score: Int) = "quiz_result/$score"
    }
}

val BottomNavScreens = listOf(
    Screen.Home,
    Screen.Subjects,
    Screen.Analytics,
    Screen.Settings
)
