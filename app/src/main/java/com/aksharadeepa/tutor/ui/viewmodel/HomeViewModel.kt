package com.aksharadeepa.tutor.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.aksharadeepa.tutor.data.local.entity.ProfileEntity
import com.aksharadeepa.tutor.data.local.entity.SubjectEntity
import com.aksharadeepa.tutor.data.repository.TutorRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn

class HomeViewModel(repository: TutorRepository) : ViewModel() {
    val subjects: StateFlow<List<SubjectEntity>> = repository.subjects
        .stateIn(viewModelScope, SharingStarted.Lazily, emptyList())
        
    val profile: StateFlow<ProfileEntity?> = repository.getProfileFlow()
        .stateIn(viewModelScope, SharingStarted.Lazily, null)

    val todayGoal: StateFlow<com.aksharadeepa.tutor.data.local.entity.DailyGoalEntity?> = repository.getTodayGoalFlow()
        .stateIn(viewModelScope, SharingStarted.Lazily, null)
}
