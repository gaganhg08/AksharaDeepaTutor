package com.aksharadeepa.tutor.ui.screens

import android.Manifest
import android.os.Build
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.aksharadeepa.tutor.ui.viewmodel.GoalsViewModel
import com.aksharadeepa.tutor.ui.theme.CorrectGreen
import com.aksharadeepa.tutor.ui.theme.IncorrectRed
import androidx.compose.foundation.layout.fillMaxSize

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DailyGoalsScreen(viewModel: GoalsViewModel, onBack: () -> Unit) {
    val uiState by viewModel.uiState.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.refresh()
    }

    val permissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission(),
        onResult = { isGranted ->
            if (isGranted) {
                viewModel.updateReminderSettings(true, uiState.reminderHour, uiState.reminderMinute)
            }
        }
    )

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            TopAppBar(
                title = { Text("Study Goals & Achievements") },
                navigationIcon = {
                    IconButton(onClick = onBack) { Icon(Icons.Default.ArrowBack, "Back") }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    titleContentColor = MaterialTheme.colorScheme.onPrimary,
                    navigationIconContentColor = MaterialTheme.colorScheme.onPrimary
                )
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .verticalScroll(rememberScrollState())
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(20.dp)
        ) {
            StreakCard(streakCount = uiState.streakCount, longestStreak = uiState.longestStreak)
            
            DailyCompletionCard(
                completedQuizzes = uiState.completedQuizzes,
                targetQuizzes = uiState.targetQuizzes,
                completedChapters = uiState.completedChapters,
                targetChapters = uiState.targetChapters,
                isCompletedToday = uiState.isGoalCompletedToday
            )

            GoalSettingsCard(
                targetQuizzes = uiState.targetQuizzes,
                targetChapters = uiState.targetChapters,
                onUpdateGoals = { q, c -> viewModel.updateGoalTargets(q, c) }
            )

            AchievementBadgesCard(
                totalChaptersCompleted = uiState.totalChaptersCompleted,
                longestStreak = uiState.longestStreak,
                completedQuizzes = uiState.completedQuizzes
            )

            Text("Reminder Settings", style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold)

            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text("Daily Notification", style = MaterialTheme.typography.titleMedium)
                        Switch(
                            checked = uiState.reminderEnabled,
                            onCheckedChange = { enabled ->
                                if (enabled && Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                                    permissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
                                } else {
                                    viewModel.updateReminderSettings(enabled, uiState.reminderHour, uiState.reminderMinute)
                                }
                            }
                        )
                    }

                    if (uiState.reminderEnabled) {
                        Spacer(modifier = Modifier.height(16.dp))
                        Text("Reminder Time", style = MaterialTheme.typography.labelMedium, color = Color.Gray)
                        Spacer(modifier = Modifier.height(8.dp))
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            var hourStr by remember { mutableStateOf(uiState.reminderHour.toString().padStart(2, '0')) }
                            var minStr by remember { mutableStateOf(uiState.reminderMinute.toString().padStart(2, '0')) }

                            OutlinedTextField(
                                value = hourStr,
                                onValueChange = { 
                                    hourStr = it.take(2)
                                    val h = it.toIntOrNull()
                                    if (h != null && h in 0..23) viewModel.updateReminderSettings(true, h, uiState.reminderMinute)
                                },
                                modifier = Modifier.width(70.dp),
                                textStyle = LocalTextStyle.current.copy(textAlign = androidx.compose.ui.text.style.TextAlign.Center),
                                keyboardOptions = androidx.compose.foundation.text.KeyboardOptions(keyboardType = androidx.compose.ui.text.input.KeyboardType.Number)
                            )
                            Text(":", style = MaterialTheme.typography.titleLarge)
                            OutlinedTextField(
                                value = minStr,
                                onValueChange = { 
                                    minStr = it.take(2)
                                    val m = it.toIntOrNull()
                                    if (m != null && m in 0..59) viewModel.updateReminderSettings(true, uiState.reminderHour, m)
                                },
                                modifier = Modifier.width(70.dp),
                                textStyle = LocalTextStyle.current.copy(textAlign = androidx.compose.ui.text.style.TextAlign.Center),
                                keyboardOptions = androidx.compose.foundation.text.KeyboardOptions(keyboardType = androidx.compose.ui.text.input.KeyboardType.Number)
                            )
                            Text("(24h format)", style = MaterialTheme.typography.bodySmall, color = Color.Gray, modifier = Modifier.padding(start = 8.dp))
                        }
                    }
                }
            }
            Spacer(modifier = Modifier.height(100.dp))
        }
    }
}

@Composable
fun StreakCard(streakCount: Int, longestStreak: Int) {
    val infiniteTransition = rememberInfiniteTransition(label = "pulse")
    val scale by infiniteTransition.animateFloat(
        initialValue = 1f,
        targetValue = 1.1f,
        animationSpec = infiniteRepeatable(
            animation = tween(1000, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ), label = "fire_pulse"
    )

    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = Color(0xFFFFF3E0)),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(
            modifier = Modifier
                .padding(24.dp)
                .fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Box(
                modifier = Modifier
                    .size(80.dp)
                    .scale(if (streakCount > 0) scale else 1f)
                    .clip(CircleShape)
                    .background(if (streakCount > 0) Color(0xFFFF9800).copy(alpha = 0.2f) else Color.LightGray.copy(alpha = 0.3f)),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    Icons.Default.LocalFireDepartment, 
                    contentDescription = "Streak Fire",
                    tint = if (streakCount > 0) Color(0xFFFF9800) else Color.Gray,
                    modifier = Modifier.size(50.dp)
                )
            }
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = "$streakCount Day Streak!",
                style = MaterialTheme.typography.headlineMedium,
                fontWeight = FontWeight.Bold,
                color = if (streakCount > 0) Color(0xFFFF5722) else Color.Gray
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text("Longest Streak: $longestStreak days", style = MaterialTheme.typography.bodyMedium, color = Color.Gray)
        }
    }
}

@Composable
fun DailyCompletionCard(
    completedQuizzes: Int,
    targetQuizzes: Int,
    completedChapters: Int,
    targetChapters: Int,
    isCompletedToday: Boolean
) {
    val qProgress = if (targetQuizzes > 0) completedQuizzes.toFloat() / targetQuizzes else 0f
    val cProgress = if (targetChapters > 0) completedChapters.toFloat() / targetChapters else 0f
    val overallProgress = ((qProgress + cProgress) / 2).coerceIn(0f, 1f)
    
    val animatedProgress by animateFloatAsState(targetValue = overallProgress, label = "prog")

    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = if (isCompletedToday) Color(0xFFE8F5E9) else MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    Icons.Default.CheckCircle, 
                    contentDescription = null,
                    tint = if (isCompletedToday) Color(0xFF4CAF50) else Color.LightGray,
                    modifier = Modifier.size(32.dp)
                )
                Spacer(modifier = Modifier.width(16.dp))
                Column {
                    Text("Today's Mission", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
                    Text(if (isCompletedToday) "Goal Met!" else "In Progress", color = if (isCompletedToday) Color(0xFF4CAF50) else Color.Gray, style = MaterialTheme.typography.bodySmall)
                }
            }
            Spacer(modifier = Modifier.height(16.dp))
            Text("Quizzes: $completedQuizzes / $targetQuizzes", style = MaterialTheme.typography.bodySmall)
            LinearProgressIndicator(progress = { qProgress }, modifier = Modifier.fillMaxWidth().height(6.dp).clip(RoundedCornerShape(3.dp)), color = MaterialTheme.colorScheme.primary)
            Spacer(modifier = Modifier.height(8.dp))
            Text("Chapters: $completedChapters / $targetChapters", style = MaterialTheme.typography.bodySmall)
            LinearProgressIndicator(progress = { cProgress }, modifier = Modifier.fillMaxWidth().height(6.dp).clip(RoundedCornerShape(3.dp)), color = Color(0xFFFF9800))
        }
    }
}

@Composable
fun GoalSettingsCard(
    targetQuizzes: Int,
    targetChapters: Int,
    onUpdateGoals: (Int, Int) -> Unit
) {
    var quizzes by remember { mutableStateOf(targetQuizzes.toFloat()) }
    var chapters by remember { mutableStateOf(targetChapters.toFloat()) }
    
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text("Set Custom Goals", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
            Spacer(modifier = Modifier.height(16.dp))
            
            Text("Target Quizzes per day: ${quizzes.toInt()}", style = MaterialTheme.typography.bodyMedium)
            Slider(
                value = quizzes,
                onValueChange = { quizzes = it },
                onValueChangeFinished = { onUpdateGoals(quizzes.toInt(), chapters.toInt()) },
                valueRange = 1f..10f,
                steps = 8
            )
            
            Text("Target Chapters per day: ${chapters.toInt()}", style = MaterialTheme.typography.bodyMedium)
            Slider(
                value = chapters,
                onValueChange = { chapters = it },
                onValueChangeFinished = { onUpdateGoals(quizzes.toInt(), chapters.toInt()) },
                valueRange = 1f..5f,
                steps = 3,
                colors = SliderDefaults.colors(thumbColor = Color(0xFFFF9800), activeTrackColor = Color(0xFFFF9800))
            )
        }
    }
}

@Composable
fun AchievementBadgesCard(totalChaptersCompleted: Int, longestStreak: Int, completedQuizzes: Int) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text("Achievements", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
            Spacer(modifier = Modifier.height(16.dp))
            
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                BadgeItem("5 Chapters", Icons.Default.MenuBook, totalChaptersCompleted >= 5)
                BadgeItem("7-Day Streak", Icons.Default.LocalFireDepartment, longestStreak >= 7)
                BadgeItem("Quiz Champion", Icons.Default.EmojiEvents, completedQuizzes >= 10) // Mock logic for champ
            }
        }
    }
}

@Composable
fun BadgeItem(title: String, icon: androidx.compose.ui.graphics.vector.ImageVector, isUnlocked: Boolean) {
    Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier.width(80.dp)) {
        Box(
            modifier = Modifier
                .size(60.dp)
                .clip(CircleShape)
                .background(if (isUnlocked) Color(0xFFFFD700).copy(alpha = 0.2f) else Color.LightGray.copy(alpha = 0.2f)),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                icon,
                contentDescription = null,
                tint = if (isUnlocked) Color(0xFFFFD700) else Color.Gray,
                modifier = Modifier.size(32.dp)
            )
        }
        Spacer(modifier = Modifier.height(8.dp))
        Text(title, style = MaterialTheme.typography.bodySmall, fontWeight = FontWeight.SemiBold, textAlign = androidx.compose.ui.text.style.TextAlign.Center, color = if (isUnlocked) MaterialTheme.colorScheme.onSurface else Color.Gray)
    }
}


