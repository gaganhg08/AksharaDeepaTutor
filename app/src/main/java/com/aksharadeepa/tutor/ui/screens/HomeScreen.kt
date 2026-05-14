package com.aksharadeepa.tutor.ui.screens

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.aksharadeepa.tutor.data.local.entity.SubjectEntity
import com.aksharadeepa.tutor.ui.viewmodel.HomeViewModel
import androidx.compose.foundation.layout.fillMaxSize

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    viewModel: HomeViewModel,
    onNavigateToSubjects: () -> Unit,
    onNavigateToQuiz: (Int, Int) -> Unit,
    onNavigateToAnalytics: () -> Unit,
    onNavigateToGoals: () -> Unit,
    onNavigateToSettings: () -> Unit
) {
    val subjects by viewModel.subjects.collectAsState()
    val profile by viewModel.profile.collectAsState()
    val todayGoal by viewModel.todayGoal.collectAsState()
    
    val totalProgress = subjects.sumOf { it.progressPercentage }
    val overallMastery = if (subjects.isNotEmpty()) totalProgress / subjects.size else 0
    val studentName = profile?.studentName ?: "Student"
    
    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            TopAppBar(
                title = { Text("Study Mission Center", fontWeight = FontWeight.Bold) },
                actions = {
                    IconButton(onClick = onNavigateToSettings) {
                        Icon(Icons.Default.Settings, contentDescription = "Settings")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    titleContentColor = MaterialTheme.colorScheme.onPrimary,
                    actionIconContentColor = MaterialTheme.colorScheme.onPrimary
                )
            )
        }
    ) { padding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .background(MaterialTheme.colorScheme.background),
            contentPadding = PaddingValues(start = 16.dp, top = 16.dp, end = 16.dp, bottom = 120.dp),
            verticalArrangement = Arrangement.spacedBy(20.dp)
        ) {
            item {
                WelcomeCard(studentName, overallMastery)
            }
            
            item {
                QuickNavRow(
                    onNavigateToSubjects = onNavigateToSubjects,
                    onNavigateToAnalytics = onNavigateToAnalytics,
                    onNavigateToGoals = onNavigateToGoals
                )
            }

            val weakSubjects = subjects.filter { it.progressPercentage < 40 }
            if (weakSubjects.isNotEmpty()) {
                item {
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.errorContainer),
                        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                    ) {
                        Row(modifier = Modifier.padding(16.dp), verticalAlignment = Alignment.CenterVertically) {
                            Icon(Icons.Default.Warning, contentDescription = null, tint = MaterialTheme.colorScheme.onErrorContainer)
                            Spacer(modifier = Modifier.width(16.dp))
                            Text("⚠ AI Alert: ${weakSubjects.first().name} needs more practice to hit your target!", color = MaterialTheme.colorScheme.onErrorContainer, fontWeight = FontWeight.Medium)
                        }
                    }
                }
            }
            
            item {
                DailyGoalCard(todayGoal)
            }
            
            item {
                DailyLearningTipCard()
            }
            
            item {
                Text(
                    text = "Your Subjects",
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onBackground
                )
            }
            
            items(subjects) { subject ->
                SubjectProgressCard(subject = subject, onClick = { onNavigateToSubjects() })
            }
            
            item {
                Button(
                    onClick = { onNavigateToSubjects() },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(56.dp),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Icon(Icons.Default.PlayArrow, contentDescription = null)
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("Continue Mission", fontSize = 18.sp, fontWeight = FontWeight.Bold)
                }
            }
        }
    }
}

@Composable
fun WelcomeCard(studentName: String, overallMastery: Int) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primaryContainer),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Row(
            modifier = Modifier.padding(20.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text("Namaskara, $studentName!", style = MaterialTheme.typography.headlineSmall, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.onPrimaryContainer)
                Spacer(modifier = Modifier.height(4.dp))
                Text("Ready to learn today?", style = MaterialTheme.typography.bodyLarge, color = MaterialTheme.colorScheme.onPrimaryContainer.copy(alpha = 0.8f))
            }
            
            Box(contentAlignment = Alignment.Center, modifier = Modifier.size(80.dp)) {
                val animatedProgress by animateFloatAsState(
                    targetValue = overallMastery / 100f,
                    animationSpec = tween(1500),
                    label = "MasteryAnimation"
                )
                val primaryColor = MaterialTheme.colorScheme.primary
                val backgroundColor = MaterialTheme.colorScheme.onPrimaryContainer.copy(alpha = 0.2f)
                
                Canvas(modifier = Modifier.fillMaxSize()) {
                    drawArc(
                        color = backgroundColor,
                        startAngle = 0f,
                        sweepAngle = 360f,
                        useCenter = false,
                        style = Stroke(width = 8.dp.toPx(), cap = StrokeCap.Round)
                    )
                    drawArc(
                        color = primaryColor,
                        startAngle = -90f,
                        sweepAngle = animatedProgress * 360f,
                        useCenter = false,
                        style = Stroke(width = 8.dp.toPx(), cap = StrokeCap.Round)
                    )
                }
                Text(
                    text = "$overallMastery%",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onPrimaryContainer
                )
            }
        }
    }
}

@Composable
fun QuickNavRow(
    onNavigateToSubjects: () -> Unit,
    onNavigateToAnalytics: () -> Unit,
    onNavigateToGoals: () -> Unit
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        QuickNavIcon("Subjects", Icons.Default.Book, MaterialTheme.colorScheme.secondary, onNavigateToSubjects)
        QuickNavIcon("Analytics", Icons.Default.PieChart, Color(0xFF9C27B0), onNavigateToAnalytics)
        QuickNavIcon("Goals", Icons.Default.Star, Color(0xFFFF9800), onNavigateToGoals)
    }
}

@Composable
fun QuickNavIcon(title: String, icon: ImageVector, color: Color, onClick: () -> Unit) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .clickable(onClick = onClick)
            .padding(8.dp)
    ) {
        Box(
            modifier = Modifier
                .size(60.dp)
                .clip(CircleShape)
                .background(color.copy(alpha = 0.2f)),
            contentAlignment = Alignment.Center
        ) {
            Icon(icon, contentDescription = title, tint = color, modifier = Modifier.size(32.dp))
        }
        Spacer(modifier = Modifier.height(8.dp))
        Text(title, style = MaterialTheme.typography.labelMedium, fontWeight = FontWeight.SemiBold)
    }
}

@Composable
fun DailyGoalCard(goal: com.aksharadeepa.tutor.data.local.entity.DailyGoalEntity?) {
    val targetChapters = goal?.targetChapters ?: 1
    val completedChapters = goal?.chaptersRead ?: 0
    val progress = if (targetChapters > 0) completedChapters.toFloat() / targetChapters else 0f
    
    val targetQuizzes = goal?.targetQuizzes ?: 2
    val completedQuizzes = goal?.completedQuizzes ?: 0
    val qProgress = if (targetQuizzes > 0) completedQuizzes.toFloat() / targetQuizzes else 0f
    
    val overallProgress = ((progress + qProgress) / 2).coerceIn(0f, 1f)

    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(Icons.Default.TrendingUp, contentDescription = null, tint = MaterialTheme.colorScheme.primary)
                Spacer(modifier = Modifier.width(8.dp))
                Text("Daily Study Goal", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
            }
            Spacer(modifier = Modifier.height(12.dp))
            
            val animatedGoal by animateFloatAsState(targetValue = overallProgress, label = "GoalAnimation")
            
            LinearProgressIndicator(
                progress = { animatedGoal },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(10.dp)
                    .clip(RoundedCornerShape(5.dp)),
                color = Color(0xFF4CAF50),
                trackColor = Color(0xFF4CAF50).copy(alpha = 0.2f)
            )
            Spacer(modifier = Modifier.height(8.dp))
            
            val message = if (overallProgress >= 1f) "Great job! Daily goal achieved."
            else if (completedChapters < targetChapters) "${targetChapters - completedChapters} more chapter to complete today's mission."
            else if (completedQuizzes < targetQuizzes) "${targetQuizzes - completedQuizzes} more quiz to complete today's mission."
            else "You are improving consistently!"
            
            Text(message, style = MaterialTheme.typography.bodyMedium, color = Color.Gray)
        }
    }
}

@Composable
fun DailyLearningTipCard() {
    val tips = remember {
        listOf(
            "Studying 25 minutes daily improves memory retention.",
            "Revise weak topics before starting new chapters.",
            "Practice Mathematics daily for better speed.",
            "Short revision sessions improve long-term memory.",
            "Taking quizzes helps strengthen understanding."
        )
    }
    
    var currentTipIndex by remember { mutableStateOf(tips.indices.random()) }

    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.tertiaryContainer),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Box(
                    modifier = Modifier
                        .size(50.dp)
                        .clip(CircleShape)
                        .background(MaterialTheme.colorScheme.tertiary.copy(alpha = 0.2f)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(Icons.Default.Lightbulb, contentDescription = null, tint = MaterialTheme.colorScheme.tertiary)
                }
                Spacer(modifier = Modifier.width(16.dp))
                Column(modifier = Modifier.weight(1f)) {
                    Text("Daily Learning Tip", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.onTertiaryContainer)
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(tips[currentTipIndex], style = MaterialTheme.typography.bodyMedium, color = MaterialTheme.colorScheme.onTertiaryContainer.copy(alpha = 0.8f))
                }
            }
            Spacer(modifier = Modifier.height(8.dp))
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.End) {
                TextButton(
                    onClick = { currentTipIndex = (currentTipIndex + 1) % tips.size }
                ) {
                    Text("Next Tip", color = MaterialTheme.colorScheme.tertiary, fontWeight = FontWeight.Bold)
                }
            }
        }
    }
}

@Composable
fun SubjectProgressCard(subject: SubjectEntity, onClick: () -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            val icon = when (subject.id) {
                1 -> Icons.Default.Science
                2 -> Icons.Default.Calculate
                else -> Icons.Default.Public
            }
            val color = when (subject.id) {
                1 -> Color(0xFF2196F3)
                2 -> Color(0xFFF44336)
                else -> Color(0xFF4CAF50)
            }
            
            Box(
                modifier = Modifier
                    .size(50.dp)
                    .clip(RoundedCornerShape(12.dp))
                    .background(color.copy(alpha = 0.1f)),
                contentAlignment = Alignment.Center
            ) {
                Icon(icon, contentDescription = null, tint = color, modifier = Modifier.size(30.dp))
            }
            
            Spacer(modifier = Modifier.width(16.dp))
            
            Column(modifier = Modifier.weight(1f)) {
                Text(subject.name, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
                Spacer(modifier = Modifier.height(4.dp))
                
                val animatedProgress by animateFloatAsState(
                    targetValue = subject.progressPercentage / 100f,
                    label = "SubjectProgress"
                )
                
                Row(verticalAlignment = Alignment.CenterVertically) {
                    LinearProgressIndicator(
                        progress = animatedProgress,
                        modifier = Modifier
                            .weight(1f)
                            .height(8.dp)
                            .clip(RoundedCornerShape(4.dp)),
                        color = color,
                        trackColor = color.copy(alpha = 0.2f)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("${subject.progressPercentage}%", style = MaterialTheme.typography.bodySmall, fontWeight = FontWeight.Bold)
                }
            }
        }
    }
}


