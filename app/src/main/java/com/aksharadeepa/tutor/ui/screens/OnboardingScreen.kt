package com.aksharadeepa.tutor.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.aksharadeepa.tutor.ui.viewmodel.OnboardingViewModel
import androidx.compose.foundation.layout.fillMaxSize

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun OnboardingScreen(viewModel: OnboardingViewModel, onComplete: () -> Unit) {
    val uiState by viewModel.uiState.collectAsState()

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            TopAppBar(
                title = { Text("Welcome to Akshara-Deepa") },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    titleContentColor = MaterialTheme.colorScheme.onPrimary
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
            Text("Let's set up your profile!", style = MaterialTheme.typography.headlineMedium, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.primary)
            Text("We'll use this to tailor your study journey.", style = MaterialTheme.typography.bodyLarge, color = MaterialTheme.colorScheme.onSurfaceVariant)

            OutlinedTextField(
                value = uiState.studentName,
                onValueChange = { viewModel.updateName(it) },
                label = { Text("Your Name") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true
            )

            OutlinedTextField(
                value = uiState.schoolName,
                onValueChange = { viewModel.updateSchool(it) },
                label = { Text("School Name") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true
            )

            Text("Preferred Medium", style = MaterialTheme.typography.titleMedium)
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                FilterChip(
                    selected = uiState.medium == "English",
                    onClick = { viewModel.updateMedium("English") },
                    label = { Text("English") }
                )
                FilterChip(
                    selected = uiState.medium == "Kannada",
                    onClick = { viewModel.updateMedium("Kannada") },
                    label = { Text("Kannada") }
                )
            }

            OutlinedTextField(
                value = uiState.targetPercentage,
                onValueChange = { viewModel.updateTarget(it) },
                label = { Text("Target SSLC Score (%)") },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                modifier = Modifier.fillMaxWidth(),
                singleLine = true
            )

            Spacer(modifier = Modifier.weight(1f))

            Button(
                onClick = { viewModel.saveProfile(onComplete) },
                modifier = Modifier.fillMaxWidth().height(56.dp),
                colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary)
            ) {
                Text("Start Learning Journey", style = MaterialTheme.typography.titleMedium)
            }
            Spacer(modifier = Modifier.height(100.dp))
        }
    }
}


