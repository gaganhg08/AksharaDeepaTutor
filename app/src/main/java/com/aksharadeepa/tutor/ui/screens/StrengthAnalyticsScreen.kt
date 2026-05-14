package com.aksharadeepa.tutor.ui.screens

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material.icons.filled.TrendingUp
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.aksharadeepa.tutor.ui.viewmodel.AnalyticsViewModel
import kotlin.math.PI
import kotlin.math.cos
import kotlin.math.sin
import androidx.compose.foundation.layout.fillMaxSize

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun StrengthAnalyticsScreen(
    viewModel: AnalyticsViewModel,
    onBack: () -> Unit
) {
    val uiState by viewModel.uiState.collectAsState()

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            TopAppBar(
                title = { Text("Strength Analytics") },
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
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding),
            contentPadding = PaddingValues(start = 16.dp, top = 16.dp, end = 16.dp, bottom = 120.dp), // Deep safe area
            verticalArrangement = Arrangement.spacedBy(20.dp)
        ) {
            item {
                Text(
                    text = "Your Performance Radar",
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = "A breakdown of your mastery across subjects.",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
                )
            }

            // Radar Chart Section
            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
                ) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(320.dp)
                            .padding(16.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        RadarChart(
                            science = uiState.scienceMastery,
                            math = uiState.mathMastery,
                            social = uiState.socialMastery,
                            modifier = Modifier.size(200.dp)
                        )

                        Text("Science\n${uiState.scienceMastery.toInt()}%",
                            modifier = Modifier.align(Alignment.TopCenter),
                            textAlign = TextAlign.Center,
                            style = MaterialTheme.typography.labelLarge,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.primary
                        )
                        Text("Math\n${uiState.mathMastery.toInt()}%",
                            modifier = Modifier
                                .align(Alignment.BottomEnd)
                                .offset(y = (-20).dp),
                            textAlign = TextAlign.Center,
                            style = MaterialTheme.typography.labelLarge,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.primary
                        )
                        Text("Social\n${uiState.socialMastery.toInt()}%",
                            modifier = Modifier
                                .align(Alignment.BottomStart)
                                .offset(y = (-20).dp),
                            textAlign = TextAlign.Center,
                            style = MaterialTheme.typography.labelLarge,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.primary
                        )
                    }
                }
            }

            // Average Score Card
            item {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    Card(
                        modifier = Modifier.weight(1f),
                        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.secondaryContainer)
                    ) {
                        Column(modifier = Modifier.padding(16.dp)) {
                            Icon(Icons.Default.TrendingUp, contentDescription = null, tint = MaterialTheme.colorScheme.onSecondaryContainer)
                            Spacer(modifier = Modifier.height(8.dp))
                            Text("Average Score", style = MaterialTheme.typography.labelMedium)
                            Text("${uiState.averageScore.toInt()}%", style = MaterialTheme.typography.headlineMedium, fontWeight = FontWeight.Bold)
                        }
                    }

                    Card(
                        modifier = Modifier.weight(1f),
                        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.errorContainer)
                    ) {
                        Column(modifier = Modifier.padding(16.dp)) {
                            Icon(Icons.Default.Warning, contentDescription = null, tint = MaterialTheme.colorScheme.onErrorContainer)
                            Spacer(modifier = Modifier.height(8.dp))
                            Text("Weakest Subject", style = MaterialTheme.typography.labelMedium)
                            Text(uiState.weakestSubject, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold, maxLines = 1)
                        }
                    }
                }
            }

            // Recommendation Card
            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primaryContainer)
                ) {
                    Row(modifier = Modifier.padding(16.dp), verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Default.CheckCircle, contentDescription = null, tint = MaterialTheme.colorScheme.onPrimaryContainer, modifier = Modifier.size(40.dp))
                        Spacer(modifier = Modifier.width(16.dp))
                        Column {
                            Text("Focus Recommendation", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.onPrimaryContainer)
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(uiState.recommendation, style = MaterialTheme.typography.bodyMedium, color = MaterialTheme.colorScheme.onPrimaryContainer.copy(alpha = 0.8f))
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun RadarChart(
    science: Float,
    math: Float,
    social: Float,
    modifier: Modifier = Modifier
) {
    val animScience by animateFloatAsState(targetValue = science / 100f, animationSpec = tween(1500), label = "animScience")
    val animMath by animateFloatAsState(targetValue = math / 100f, animationSpec = tween(1500), label = "animMath")
    val animSocial by animateFloatAsState(targetValue = social / 100f, animationSpec = tween(1500), label = "animSocial")

    val lineColor = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.2f)
    val primaryColor = MaterialTheme.colorScheme.primary
    val fillColor = MaterialTheme.colorScheme.primary.copy(alpha = 0.4f)
    
    Canvas(modifier = modifier) {
        val radius = size.minDimension / 2f
        val center = Offset(size.width / 2f, size.height / 2f)
        
        // Draw concentric triangles (Web Grid)
        for (i in 1..4) {
            val r = radius * (i / 4f)
            val path = Path().apply {
                moveTo(center.x, center.y - r)
                lineTo(center.x + r * cos(PI / 6).toFloat(), center.y + r * sin(PI / 6).toFloat())
                lineTo(center.x - r * cos(PI / 6).toFloat(), center.y + r * sin(PI / 6).toFloat())
                close()
            }
            drawPath(path, color = lineColor, style = Stroke(width = 2f))
        }
        
        // Draw Axes
        drawLine(color = lineColor, start = center, end = Offset(center.x, center.y - radius), strokeWidth = 2f)
        drawLine(color = lineColor, start = center, end = Offset(center.x + radius * cos(PI / 6).toFloat(), center.y + radius * sin(PI / 6).toFloat()), strokeWidth = 2f)
        drawLine(color = lineColor, start = center, end = Offset(center.x - radius * cos(PI / 6).toFloat(), center.y + radius * sin(PI / 6).toFloat()), strokeWidth = 2f)
        
        // Draw Data Polygon
        val dataPath = Path().apply {
            moveTo(center.x, center.y - (radius * animScience))
            lineTo(center.x + (radius * animMath) * cos(PI / 6).toFloat(), center.y + (radius * animMath) * sin(PI / 6).toFloat())
            lineTo(center.x - (radius * animSocial) * cos(PI / 6).toFloat(), center.y + (radius * animSocial) * sin(PI / 6).toFloat())
            close()
        }
        
        drawPath(dataPath, color = fillColor)
        drawPath(dataPath, color = primaryColor, style = Stroke(width = 6f, cap = StrokeCap.Round, join = androidx.compose.ui.graphics.StrokeJoin.Round))
        
        // Draw Points
        drawCircle(color = primaryColor, radius = 10f, center = Offset(center.x, center.y - (radius * animScience)))
        drawCircle(color = primaryColor, radius = 10f, center = Offset(center.x + (radius * animMath) * cos(PI / 6).toFloat(), center.y + (radius * animMath) * sin(PI / 6).toFloat()))
        drawCircle(color = primaryColor, radius = 10f, center = Offset(center.x - (radius * animSocial) * cos(PI / 6).toFloat(), center.y + (radius * animSocial) * sin(PI / 6).toFloat()))
    }
}


