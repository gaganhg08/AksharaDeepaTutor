package com.aksharadeepa.tutor.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.aksharadeepa.tutor.data.local.entity.ChapterEntity
import com.aksharadeepa.tutor.data.repository.TutorRepository
import androidx.compose.foundation.layout.fillMaxSize

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ChapterProgressScreen(
    subjectId: Int,
    repository: TutorRepository,
    onBack: () -> Unit,
    onStartQuiz: (Int) -> Unit
) {
    var chapters by remember { mutableStateOf<List<ChapterEntity>>(emptyList()) }
    
    LaunchedEffect(subjectId) {
        repository.getChapters(subjectId).collect {
            chapters = it
        }
    }

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            TopAppBar(
                title = { Text("Chapters") },
                navigationIcon = {
                    IconButton(onClick = onBack) { Icon(Icons.Default.ArrowBack, "Back") }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = MaterialTheme.colorScheme.primary, titleContentColor = MaterialTheme.colorScheme.onPrimary, navigationIconContentColor = MaterialTheme.colorScheme.onPrimary)
            )
        },
        floatingActionButton = {
            ExtendedFloatingActionButton(onClick = { onStartQuiz(subjectId) }) {
                Text("Take Quiz")
            }
        }
    ) { padding ->
        LazyColumn(modifier = Modifier.padding(padding).padding(16.dp)) {
            items(chapters) { chapter ->
                Card(modifier = Modifier.fillMaxWidth().padding(bottom = 8.dp)) {
                    Row(modifier = Modifier.padding(16.dp)) {
                        Text(chapter.title, modifier = Modifier.weight(1f))
                        Checkbox(checked = chapter.isCompleted, onCheckedChange = null)
                    }
                }
            }
        }
    }
}


