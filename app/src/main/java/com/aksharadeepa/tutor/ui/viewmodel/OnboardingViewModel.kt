package com.aksharadeepa.tutor.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.aksharadeepa.tutor.data.local.entity.ProfileEntity
import com.aksharadeepa.tutor.data.repository.TutorRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class OnboardingViewModel(private val repository: TutorRepository) : ViewModel() {
    private val _uiState = MutableStateFlow(OnboardingUiState())
    val uiState: StateFlow<OnboardingUiState> = _uiState.asStateFlow()

    fun updateName(name: String) = _uiState.update { it.copy(studentName = name) }
    fun updateSchool(school: String) = _uiState.update { it.copy(schoolName = school) }
    fun updateMedium(medium: String) = _uiState.update { it.copy(medium = medium) }
    fun updateTarget(target: String) = _uiState.update { it.copy(targetPercentage = target) }

    fun saveProfile(onSaved: () -> Unit) {
        val state = _uiState.value
        val targetInt = state.targetPercentage.toIntOrNull() ?: 80
        val profile = ProfileEntity(
            studentName = state.studentName.ifBlank { "Student" },
            schoolName = state.schoolName.ifBlank { "SSLC School" },
            medium = state.medium,
            targetPercentage = targetInt
        )
        viewModelScope.launch {
            repository.saveProfile(profile)
            onSaved()
        }
    }
}
