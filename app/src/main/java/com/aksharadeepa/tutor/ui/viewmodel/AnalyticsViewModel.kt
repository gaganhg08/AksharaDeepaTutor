package com.aksharadeepa.tutor.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.aksharadeepa.tutor.data.repository.TutorRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn

data class AnalyticsState(
    val scienceMastery: Float = 0f,
    val mathMastery: Float = 0f,
    val socialMastery: Float = 0f,
    val averageScore: Float = 0f,
    val weakestSubject: String = "None",
    val recommendation: String = "Keep learning!"
)

class AnalyticsViewModel(repository: TutorRepository) : ViewModel() {
    val uiState: StateFlow<AnalyticsState> = repository.subjects.map { subjects ->
        val science = subjects.find { it.id == 1 }?.progressPercentage?.toFloat() ?: 0f
        val math = subjects.find { it.id == 2 }?.progressPercentage?.toFloat() ?: 0f
        val social = subjects.find { it.id == 3 }?.progressPercentage?.toFloat() ?: 0f
        
        val average = if (subjects.isNotEmpty()) (science + math + social) / 3f else 0f
        
        val map = mapOf("Science" to science, "Mathematics" to math, "Social Studies" to social)
        val weakest = map.minByOrNull { it.value }
        
        val weakestName = weakest?.key ?: "None"
        val recommendation = when (weakestName) {
            "Science" -> "Review chemical reactions and physics concepts."
            "Mathematics" -> "Practice more algebra and geometry equations."
            "Social Studies" -> "Read more about Indian history and civic structure."
            else -> "You are doing great!"
        }
        
        AnalyticsState(
            scienceMastery = science,
            mathMastery = math,
            socialMastery = social,
            averageScore = average,
            weakestSubject = weakestName,
            recommendation = recommendation
        )
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = AnalyticsState()
    )
}
