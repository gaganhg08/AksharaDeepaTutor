package com.aksharadeepa.tutor.ui.navigation

import androidx.compose.animation.*
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.aksharadeepa.tutor.TutorApplication
import com.aksharadeepa.tutor.ui.screens.*
import com.aksharadeepa.tutor.ui.viewmodel.AnalyticsViewModel
import com.aksharadeepa.tutor.ui.viewmodel.GoalsViewModel
import com.aksharadeepa.tutor.ui.viewmodel.HomeViewModel
import com.aksharadeepa.tutor.ui.viewmodel.QuizViewModel
import com.aksharadeepa.tutor.ui.viewmodel.SyllabusViewModel
import com.aksharadeepa.tutor.ui.viewmodel.OnboardingViewModel
import androidx.compose.foundation.layout.fillMaxSize

@Composable
fun TutorAppNavigation(app: TutorApplication) {
    val navController = rememberNavController()
    
    val repository = app.repository
    val userPreferences = app.userPreferences
    
    // Correct ViewModel initialization using factory to pass dependencies
    val homeViewModel: HomeViewModel = viewModel(factory = object : ViewModelProvider.Factory {
        @Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel> create(modelClass: Class<T>): T = HomeViewModel(repository) as T
    })
    val quizViewModel: QuizViewModel = viewModel(factory = object : ViewModelProvider.Factory {
        @Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel> create(modelClass: Class<T>): T = QuizViewModel(repository) as T
    })
    val analyticsViewModel: AnalyticsViewModel = viewModel(factory = object : ViewModelProvider.Factory {
        @Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel> create(modelClass: Class<T>): T = AnalyticsViewModel(repository) as T
    })
    val syllabusViewModel: SyllabusViewModel = viewModel(factory = object : ViewModelProvider.Factory {
        @Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel> create(modelClass: Class<T>): T = SyllabusViewModel(repository) as T
    })
    val goalsViewModel: GoalsViewModel = viewModel(factory = object : ViewModelProvider.Factory {
        @Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel> create(modelClass: Class<T>): T = GoalsViewModel(app.applicationContext, userPreferences, repository) as T
    })
    val onboardingViewModel: OnboardingViewModel = viewModel(factory = object : ViewModelProvider.Factory {
        @Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel> create(modelClass: Class<T>): T = OnboardingViewModel(repository) as T
    })

    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentDestination = navBackStackEntry?.destination

    val showBottomBar = BottomNavScreens.any { it.route == currentDestination?.route }

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        bottomBar = {
            AnimatedVisibility(
                visible = showBottomBar,
                enter = slideInVertically(initialOffsetY = { it }) + fadeIn(),
                exit = slideOutVertically(targetOffsetY = { it }) + fadeOut()
            ) {
                NavigationBar {
                    BottomNavScreens.forEach { screen ->
                        NavigationBarItem(
                            icon = { Icon(screen.icon!!, contentDescription = screen.title) },
                            label = { Text(screen.title!!) },
                            selected = currentDestination?.hierarchy?.any { it.route == screen.route } == true,
                            onClick = {
                                navController.navigate(screen.route) {
                                    popUpTo(navController.graph.findStartDestination().id) {
                                        saveState = true
                                    }
                                    launchSingleTop = true
                                    restoreState = true
                                }
                            }
                        )
                    }
                }
            }
        }
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = Screen.Splash.route,
            modifier = Modifier.fillMaxSize().padding(innerPadding),
            enterTransition = { fadeIn(animationSpec = tween(300)) },
            exitTransition = { fadeOut(animationSpec = tween(300)) },
            popEnterTransition = { slideInHorizontally(initialOffsetX = { -1000 }) + fadeIn() },
            popExitTransition = { slideOutHorizontally(targetOffsetX = { 1000 }) + fadeOut() }
        ) {
            composable(Screen.Splash.route) {
                SplashScreen(onSplashFinished = {
                    val nextRoute = if (app.userPreferences.isOnboardingCompleted) Screen.Home.route else Screen.Onboarding.route
                    navController.navigate(nextRoute) {
                        popUpTo(Screen.Splash.route) { inclusive = true }
                    }
                })
            }
            composable(Screen.Onboarding.route) {
                OnboardingScreen(
                    viewModel = onboardingViewModel,
                    onComplete = {
                        app.userPreferences.isOnboardingCompleted = true
                        navController.navigate(Screen.Home.route) {
                            popUpTo(Screen.Onboarding.route) { inclusive = true }
                        }
                    }
                )
            }
            composable(
                route = Screen.Home.route,
                enterTransition = { slideInHorizontally(initialOffsetX = { 1000 }) + fadeIn() },
                exitTransition = { slideOutHorizontally(targetOffsetX = { -1000 }) + fadeOut() }
            ) {
                HomeScreen(
                    viewModel = homeViewModel,
                    onNavigateToSubjects = { navController.navigate(Screen.Subjects.route) },
                    onNavigateToQuiz = { subjectId, chapterId -> navController.navigate(Screen.Quiz.createRoute(subjectId, chapterId)) },
                    onNavigateToAnalytics = { navController.navigate(Screen.Analytics.route) },
                    onNavigateToGoals = { navController.navigate(Screen.DailyGoals.route) },
                    onNavigateToSettings = { navController.navigate(Screen.Settings.route) }
                )
            }
            composable(
                route = Screen.Subjects.route,
                enterTransition = { slideInHorizontally(initialOffsetX = { 1000 }) + fadeIn() },
                exitTransition = { slideOutHorizontally(targetOffsetX = { -1000 }) + fadeOut() }
            ) {
                SubjectListScreen(
                    viewModel = syllabusViewModel,
                    onBack = { navController.popBackStack() },
                    onStartQuiz = { subjectId, chapterId -> navController.navigate(Screen.Quiz.createRoute(subjectId, chapterId)) }
                )
            }
            composable(
                route = Screen.Quiz.route,
                arguments = listOf(
                    navArgument("subjectId") { type = NavType.IntType },
                    navArgument("chapterId") { type = NavType.IntType }
                ),
                enterTransition = { slideInVertically(initialOffsetY = { 1000 }) + fadeIn() },
                popExitTransition = { slideOutVertically(targetOffsetY = { 1000 }) + fadeOut() }
            ) { backStackEntry ->
                val subjectId = backStackEntry.arguments?.getInt("subjectId") ?: 1
                val chapterId = backStackEntry.arguments?.getInt("chapterId") ?: 1
                QuizScreen(
                    subjectId = subjectId,
                    chapterId = chapterId,
                    viewModel = quizViewModel,
                    onExit = { navController.popBackStack() },
                    onQuizFinished = { score -> 
                        navController.navigate(Screen.QuizResult.createRoute(score)) {
                            popUpTo(Screen.Quiz.route) { inclusive = true }
                        }
                    }
                )
            }
            composable(
                route = Screen.QuizResult.route,
                arguments = listOf(navArgument("score") { type = NavType.IntType }),
                enterTransition = { scaleIn() + fadeIn() },
                exitTransition = { scaleOut() + fadeOut() }
            ) { backStackEntry ->
                val score = backStackEntry.arguments?.getInt("score") ?: 0
                QuizResultScreen(
                    score = score,
                    viewModel = quizViewModel,
                    onBackToHome = {
                        navController.navigate(Screen.Home.route) {
                            popUpTo(Screen.Home.route) { inclusive = true }
                        }
                    },
                    onRetake = {
                        val state = quizViewModel.uiState.value
                        quizViewModel.startQuiz(state.subjectId, state.chapterId, isRetake = true)
                        navController.navigate(Screen.Quiz.createRoute(state.subjectId, state.chapterId)) {
                            popUpTo(Screen.Quiz.route) { inclusive = true }
                        }
                    }
                )
            }
            composable(
                route = Screen.Analytics.route,
                enterTransition = { slideInHorizontally(initialOffsetX = { 1000 }) + fadeIn() },
                exitTransition = { slideOutHorizontally(targetOffsetX = { -1000 }) + fadeOut() }
            ) {
                StrengthAnalyticsScreen(
                    viewModel = analyticsViewModel,
                    onBack = { navController.popBackStack() }
                )
            }
            composable(
                route = Screen.DailyGoals.route,
                enterTransition = { slideInHorizontally(initialOffsetX = { 1000 }) + fadeIn() },
                exitTransition = { slideOutHorizontally(targetOffsetX = { -1000 }) + fadeOut() }
            ) {
                DailyGoalsScreen(
                    viewModel = goalsViewModel,
                    onBack = { navController.popBackStack() }
                )
            }
            composable(
                route = Screen.Settings.route,
                enterTransition = { slideInHorizontally(initialOffsetX = { 1000 }) + fadeIn() },
                exitTransition = { slideOutHorizontally(targetOffsetX = { -1000 }) + fadeOut() }
            ) {
                SettingsScreen(onBack = { navController.popBackStack() })
            }
        }
    }
}

