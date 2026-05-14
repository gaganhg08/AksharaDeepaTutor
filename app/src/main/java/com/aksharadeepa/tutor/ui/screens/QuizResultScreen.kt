package com.aksharadeepa.tutor.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Home
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.aksharadeepa.tutor.ui.viewmodel.QuizViewModel
import com.aksharadeepa.tutor.ui.theme.CorrectGreen
import com.aksharadeepa.tutor.ui.theme.IncorrectRed
import androidx.compose.foundation.layout.fillMaxSize

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun QuizResultScreen(
    score: Int,
    viewModel: QuizViewModel,
    onBackToHome: () -> Unit,
    onRetake: () -> Unit
) {
    val uiState by viewModel.uiState.collectAsState()
    val totalQuestions = uiState.questions.size
    
    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            TopAppBar(
                title = { Text("Quiz Results") },
                navigationIcon = {
                    IconButton(onClick = onBackToHome) { Icon(Icons.Default.Home, contentDescription = "Home") }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    titleContentColor = MaterialTheme.colorScheme.onPrimary,
                    navigationIconContentColor = MaterialTheme.colorScheme.onPrimary
                )
            )
        }
    ) { padding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primaryContainer),
                    elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
                ) {
                    Column(
                        modifier = Modifier
                            .padding(24.dp)
                            .fillMaxWidth(),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text("You Scored", style = MaterialTheme.typography.titleMedium, color = MaterialTheme.colorScheme.onPrimaryContainer)
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = "$score / $totalQuestions",
                            style = MaterialTheme.typography.headlineLarge,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onPrimaryContainer
                        )
                        
                        val percentage = if (totalQuestions > 0) (score.toFloat() / totalQuestions) * 100 else 0f
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = if (percentage >= 80f) "Outstanding!" else if (percentage >= 50f) "Good effort!" else "Keep practicing!",
                            style = MaterialTheme.typography.bodyLarge,
                            color = MaterialTheme.colorScheme.onPrimaryContainer.copy(alpha = 0.8f)
                        )
                        
                        Spacer(modifier = Modifier.height(16.dp))
                        
                        // Performance tracking
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceEvenly
                        ) {
                            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                Text("Best Score", style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onPrimaryContainer.copy(alpha = 0.7f))
                                Text("${uiState.bestScore ?: score}", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.onPrimaryContainer)
                            }
                            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                Text("Attempts", style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onPrimaryContainer.copy(alpha = 0.7f))
                                Text("${uiState.attempts + 1}", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.onPrimaryContainer)
                            }
                        }

                        Spacer(modifier = Modifier.height(24.dp))
                        
                        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceEvenly) {
                            OutlinedButton(
                                onClick = onBackToHome,
                                colors = ButtonDefaults.outlinedButtonColors(
                                    contentColor = MaterialTheme.colorScheme.onPrimaryContainer
                                )
                            ) {
                                Text("Dashboard")
                            }
                            
                            Button(
                                onClick = onRetake,
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = MaterialTheme.colorScheme.primary,
                                    contentColor = MaterialTheme.colorScheme.onPrimary
                                )
                            ) {
                                Text("Retake Quiz")
                            }
                        }
                    }
                }
                
                Spacer(modifier = Modifier.height(8.dp))
                Text("Answer Review", style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold)
            }
            
            itemsIndexed(uiState.questions) { index, question ->
                val userAnswer = uiState.selectedAnswers[index]
                val isCorrect = userAnswer == question.correctAnswer
                
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                    elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Row(verticalAlignment = Alignment.Top) {
                            Box(
                                modifier = Modifier
                                    .size(24.dp)
                                    .clip(CircleShape)
                                    .background(if (isCorrect) CorrectGreen else IncorrectRed),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    imageVector = if (isCorrect) Icons.Default.Check else Icons.Default.Close,
                                    contentDescription = null,
                                    tint = Color.White,
                                    modifier = Modifier.size(16.dp)
                                )
                            }
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = "Q${index + 1}: ${question.text}",
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.Medium
                            )
                        }
                        
                        Spacer(modifier = Modifier.height(12.dp))
                        
                        val userOptionText = when (userAnswer) {
                            "A" -> question.optionA
                            "B" -> question.optionB
                            "C" -> question.optionC
                            "D" -> question.optionD
                            else -> "Time out / No answer"
                        }
                        
                        Text("Your Answer: $userAnswer - $userOptionText", 
                            style = MaterialTheme.typography.bodyMedium, 
                            color = if (isCorrect) CorrectGreen else IncorrectRed,
                            fontWeight = FontWeight.Bold
                        )
                        
                        if (!isCorrect) {
                            Spacer(modifier = Modifier.height(4.dp))
                            val correctOptionText = when (question.correctAnswer) {
                                "A" -> question.optionA
                                "B" -> question.optionB
                                "C" -> question.optionC
                                "D" -> question.optionD
                                else -> ""
                            }
                            Text("Correct Answer: ${question.correctAnswer} - $correctOptionText", 
                                style = MaterialTheme.typography.bodyMedium, 
                                color = CorrectGreen,
                                fontWeight = FontWeight.Bold
                            )
                        }
                        
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = question.explanation,
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
                        )
                    }
                }
            }
        }
    }
}


