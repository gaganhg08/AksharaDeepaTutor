package com.aksharadeepa.tutor.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Book
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.aksharadeepa.tutor.ui.viewmodel.QuizViewModel
import com.aksharadeepa.tutor.ui.theme.CorrectGreen
import com.aksharadeepa.tutor.ui.theme.IncorrectRed
import androidx.compose.foundation.layout.fillMaxSize

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun QuizScreen(
    subjectId: Int,
    chapterId: Int,
    viewModel: QuizViewModel,
    onExit: () -> Unit,
    onQuizFinished: (Int) -> Unit
) {
    val uiState by viewModel.uiState.collectAsState()

    LaunchedEffect(subjectId, chapterId) {
        viewModel.startQuiz(subjectId, chapterId)
    }

    if (uiState.isFinished) {
        LaunchedEffect(Unit) {
            onQuizFinished(uiState.score)
        }
    }

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            TopAppBar(
                title = { 
                    Column {
                        Text("Self-Check", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
                        if (uiState.chapterTitle.isNotEmpty()) {
                            Text(uiState.chapterTitle, style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onPrimary.copy(alpha = 0.8f))
                        }
                    }
                },
                navigationIcon = {
                    IconButton(onClick = onExit) { Icon(Icons.Default.Close, contentDescription = "Exit") }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    titleContentColor = MaterialTheme.colorScheme.onPrimary,
                    navigationIconContentColor = MaterialTheme.colorScheme.onPrimary
                )
            )
        }
    ) { padding ->
        if (uiState.questions.isNotEmpty() && uiState.currentIndex < uiState.questions.size) {
            val currentQuestion = uiState.questions[uiState.currentIndex]
            
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding)
                    .verticalScroll(rememberScrollState())
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                // Progress Bar
                val progress by animateFloatAsState(
                    targetValue = (uiState.currentIndex + 1).toFloat() / uiState.questions.size.toFloat(),
                    label = "Progress"
                )
                LinearProgressIndicator(
                    progress = progress,
                    modifier = Modifier.fillMaxWidth().height(8.dp),
                    color = MaterialTheme.colorScheme.primary,
                    trackColor = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.2f)
                )

                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
                    Text("Question ${uiState.currentIndex + 1} of ${uiState.questions.size}", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
                    
                    // Timer
                    val timerProgress by animateFloatAsState(targetValue = uiState.timeLeft / 30f, label = "Timer")
                    Box(contentAlignment = Alignment.Center, modifier = Modifier.size(48.dp)) {
                        CircularProgressIndicator(
                            progress = timerProgress,
                            modifier = Modifier.fillMaxSize(),
                            color = if (uiState.timeLeft <= 5) IncorrectRed else MaterialTheme.colorScheme.primary,
                            trackColor = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.1f)
                        )
                        Text("${uiState.timeLeft}s", style = MaterialTheme.typography.labelSmall, fontWeight = FontWeight.Bold)
                    }
                }

                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant),
                    elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                ) {
                    Text(
                        text = currentQuestion.text,
                        style = MaterialTheme.typography.titleLarge,
                        modifier = Modifier.padding(16.dp)
                    )
                }

                val options = listOf(
                    "A" to currentQuestion.optionA,
                    "B" to currentQuestion.optionB,
                    "C" to currentQuestion.optionC,
                    "D" to currentQuestion.optionD
                )

                options.forEach { (key, value) ->
                    val isSelected = uiState.currentSelectedOption == key
                    val showResult = uiState.showAnswerResult
                    val isCorrectAnswer = key == currentQuestion.correctAnswer
                    
                    val targetBgColor = if (showResult) {
                        if (isCorrectAnswer) CorrectGreen
                        else if (isSelected) IncorrectRed
                        else MaterialTheme.colorScheme.surface
                    } else {
                        if (isSelected) MaterialTheme.colorScheme.primaryContainer else MaterialTheme.colorScheme.surface
                    }
                    
                    val targetTextColor = if (showResult) {
                        if (isCorrectAnswer || (isSelected && !isCorrectAnswer)) Color.White
                        else MaterialTheme.colorScheme.onSurface
                    } else {
                        if (isSelected) MaterialTheme.colorScheme.onPrimaryContainer else MaterialTheme.colorScheme.onSurface
                    }

                    val animatedBgColor by animateColorAsState(targetValue = targetBgColor, label = "bgColor")
                    val animatedTextColor by animateColorAsState(targetValue = targetTextColor, label = "textColor")

                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable(enabled = !showResult) {
                                viewModel.selectOption(key) 
                            },
                        colors = CardDefaults.cardColors(containerColor = animatedBgColor),
                        elevation = CardDefaults.cardElevation(defaultElevation = if (isSelected) 4.dp else 1.dp)
                    ) {
                        Text(
                            text = "$key. $value",
                            style = MaterialTheme.typography.bodyLarge,
                            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                            color = animatedTextColor,
                            modifier = Modifier.padding(16.dp)
                        )
                    }
                }

                // AI Explanation Box
                AnimatedVisibility(visible = uiState.showAnswerResult) {
                    Card(
                        modifier = Modifier.fillMaxWidth().padding(top = 8.dp),
                        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.tertiaryContainer),
                        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                    ) {
                        Column(modifier = Modifier.padding(16.dp)) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(Icons.Default.Book, contentDescription = null, tint = MaterialTheme.colorScheme.onTertiaryContainer)
                                Spacer(modifier = Modifier.width(8.dp))
                                Text("Tutor Explanation", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.onTertiaryContainer)
                            }
                            Spacer(modifier = Modifier.height(8.dp))
                            Text(currentQuestion.explanation, style = MaterialTheme.typography.bodyMedium, color = MaterialTheme.colorScheme.onTertiaryContainer)
                        }
                    }
                }
                
                Spacer(modifier = Modifier.height(100.dp))
            }
        } else {
            Box(modifier = Modifier.fillMaxSize().padding(padding), contentAlignment = Alignment.Center) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Icon(Icons.Default.Book, contentDescription = null, modifier = Modifier.size(64.dp), tint = Color.Gray)
                    Spacer(modifier = Modifier.height(16.dp))
                    Text("No questions available for this topic yet.", style = MaterialTheme.typography.titleMedium, color = Color.Gray)
                    Spacer(modifier = Modifier.height(8.dp))
                    Button(onClick = onExit) { Text("Go Back") }
                }
            }
        }
    }
}


