package com.aksharadeepa.tutor.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.expandVertically
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material.icons.filled.PlayArrow
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
import com.aksharadeepa.tutor.ui.viewmodel.SyllabusViewModel
import com.aksharadeepa.tutor.ui.viewmodel.SubjectWithSubSubjects
import com.aksharadeepa.tutor.ui.viewmodel.SubSubjectWithChapters
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import androidx.compose.foundation.layout.fillMaxSize

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SubjectListScreen(
    viewModel: SyllabusViewModel,
    onBack: () -> Unit,
    onStartQuiz: (Int, Int) -> Unit
) {
    val syllabusData by viewModel.syllabusData.collectAsState()

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            TopAppBar(
                title = { Text("Syllabus Tracker", fontWeight = FontWeight.Bold) },
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
            contentPadding = PaddingValues(start = 16.dp, top = 16.dp, end = 16.dp, bottom = 120.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            items(syllabusData) { item ->
                SubjectTreeItem(
                    data = item,
                    onToggleExpand = { viewModel.toggleSubjectExpanded(item.subject.id) },
                    onToggleSubSubject = { subSubjectId -> viewModel.toggleSubSubjectExpanded(subSubjectId) },
                    onToggleChapter = { chapter -> viewModel.toggleChapter(chapter) },
                    onTakeQuiz = { chapterId -> onStartQuiz(item.subject.id, chapterId) }
                )
            }
        }
    }
}

@Composable
fun SubjectTreeItem(
    data: SubjectWithSubSubjects,
    onToggleExpand: () -> Unit,
    onToggleSubSubject: (Int) -> Unit,
    onToggleChapter: (com.aksharadeepa.tutor.data.local.entity.ChapterEntity) -> Unit,
    onTakeQuiz: (Int) -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
    ) {
        Column {
            // Subject Header
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable(onClick = onToggleExpand)
                    .padding(16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = data.subject.name,
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    
                    val progressAnim by animateFloatAsState(targetValue = data.subject.progressPercentage / 100f, label = "Progress")
                    
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        LinearProgressIndicator(
                            progress = { progressAnim },
                            modifier = Modifier
                                .weight(1f)
                                .height(8.dp)
                                .clip(RoundedCornerShape(4.dp)),
                            color = MaterialTheme.colorScheme.primary,
                            trackColor = MaterialTheme.colorScheme.primary.copy(alpha = 0.2f)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "${data.subject.progressPercentage}%",
                            style = MaterialTheme.typography.bodySmall,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
                
                Spacer(modifier = Modifier.width(16.dp))
                
                Icon(
                    imageVector = if (data.isExpanded) Icons.Default.KeyboardArrowUp else Icons.Default.KeyboardArrowDown,
                    contentDescription = "Expand/Collapse",
                    tint = MaterialTheme.colorScheme.primary
                )
            }

            // Expanded Sub-Subjects
            AnimatedVisibility(
                visible = data.isExpanded,
                enter = expandVertically(),
                exit = shrinkVertically()
            ) {
                Column(modifier = Modifier.fillMaxWidth()) {
                    data.subSubjects.forEach { subItem ->
                        SubSubjectTreeItem(
                            data = subItem,
                            onToggleExpand = { onToggleSubSubject(subItem.subSubject.id) },
                            onToggleChapter = onToggleChapter,
                            onTakeQuiz = onTakeQuiz
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun SubSubjectTreeItem(
    data: SubSubjectWithChapters,
    onToggleExpand: () -> Unit,
    onToggleChapter: (com.aksharadeepa.tutor.data.local.entity.ChapterEntity) -> Unit,
    onTakeQuiz: (Int) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.3f))
    ) {
        // Sub-Subject Header
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .clickable(onClick = onToggleExpand)
                .padding(horizontal = 16.dp, vertical = 12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = data.subSubject.name,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.SemiBold,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Spacer(modifier = Modifier.height(4.dp))
                
                val progressAnim by animateFloatAsState(targetValue = data.subSubject.progressPercentage / 100f, label = "SubProgress")
                
                Row(verticalAlignment = Alignment.CenterVertically) {
                    LinearProgressIndicator(
                        progress = { progressAnim },
                        modifier = Modifier
                            .weight(1f)
                            .height(6.dp)
                            .clip(RoundedCornerShape(3.dp)),
                        color = MaterialTheme.colorScheme.secondary,
                        trackColor = MaterialTheme.colorScheme.secondary.copy(alpha = 0.2f)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "${data.subSubject.progressPercentage}%",
                        style = MaterialTheme.typography.labelMedium,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
            Spacer(modifier = Modifier.width(16.dp))
            Icon(
                imageVector = if (data.isExpanded) Icons.Default.KeyboardArrowUp else Icons.Default.KeyboardArrowDown,
                contentDescription = "Expand/Collapse SubSubject",
                tint = MaterialTheme.colorScheme.secondary,
                modifier = Modifier.size(20.dp)
            )
        }

        // Expanded Chapters (Topics)
        AnimatedVisibility(
            visible = data.isExpanded,
            enter = expandVertically(),
            exit = shrinkVertically()
        ) {
            Column(modifier = Modifier
                .fillMaxWidth()
                .background(MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.6f))
                .padding(start = 24.dp, end = 16.dp, top = 8.dp, bottom = 8.dp)
            ) {
                data.chapters.forEach { chapter ->
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 4.dp)
                            .clickable { onToggleChapter(chapter) },
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Checkbox(
                            checked = chapter.isCompleted,
                            onCheckedChange = { onToggleChapter(chapter) },
                            colors = CheckboxDefaults.colors(checkedColor = MaterialTheme.colorScheme.secondary)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = chapter.title,
                                style = MaterialTheme.typography.bodyMedium,
                                fontWeight = if (chapter.isCompleted) FontWeight.Normal else FontWeight.Medium,
                                color = if (chapter.isCompleted) Color.Gray else MaterialTheme.colorScheme.onSurface,
                                textDecoration = if (chapter.isCompleted) androidx.compose.ui.text.style.TextDecoration.LineThrough else null
                            )
                        }
                        // Chapter Quiz Button
                        IconButton(onClick = { onTakeQuiz(chapter.id) }) {
                            Icon(Icons.Default.PlayArrow, contentDescription = "Take Quiz", tint = MaterialTheme.colorScheme.secondary)
                        }
                    }
                }
            }
        }
    }
}


