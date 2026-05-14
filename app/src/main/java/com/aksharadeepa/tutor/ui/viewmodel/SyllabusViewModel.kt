package com.aksharadeepa.tutor.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.aksharadeepa.tutor.data.local.entity.ChapterEntity
import com.aksharadeepa.tutor.data.local.entity.SubSubjectEntity
import com.aksharadeepa.tutor.data.local.entity.SubjectEntity
import com.aksharadeepa.tutor.data.repository.TutorRepository
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

data class SubSubjectWithChapters(
    val subSubject: SubSubjectEntity,
    val chapters: List<ChapterEntity>,
    val isExpanded: Boolean = false
)

data class SubjectWithSubSubjects(
    val subject: SubjectEntity,
    val subSubjects: List<SubSubjectWithChapters>,
    val isExpanded: Boolean = false
)

class SyllabusViewModel(private val repository: TutorRepository) : ViewModel() {
    
    private val _expandedSubjects = MutableStateFlow<Set<Int>>(emptySet())
    private val _expandedSubSubjects = MutableStateFlow<Set<Int>>(emptySet())

    val syllabusData: StateFlow<List<SubjectWithSubSubjects>> = combine(
        repository.subjects,
        repository.subSubjects,
        repository.getAllChaptersFlow(),
        _expandedSubjects,
        _expandedSubSubjects
    ) { subjects, subSubjects, allChapters, expandedSubj, expandedSubSubj ->
        subjects.map { subject ->
            val nestedSubSubjects = subSubjects.filter { it.subjectId == subject.id }.map { subSubject ->
                SubSubjectWithChapters(
                    subSubject = subSubject,
                    chapters = allChapters.filter { it.subSubjectId == subSubject.id },
                    isExpanded = expandedSubSubj.contains(subSubject.id)
                )
            }
            SubjectWithSubSubjects(
                subject = subject,
                subSubjects = nestedSubSubjects,
                isExpanded = expandedSubj.contains(subject.id)
            )
        }
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    fun toggleSubjectExpanded(subjectId: Int) {
        val current = _expandedSubjects.value.toMutableSet()
        if (current.contains(subjectId)) current.remove(subjectId) else current.add(subjectId)
        _expandedSubjects.value = current
    }

    fun toggleSubSubjectExpanded(subSubjectId: Int) {
        val current = _expandedSubSubjects.value.toMutableSet()
        if (current.contains(subSubjectId)) current.remove(subSubjectId) else current.add(subSubjectId)
        _expandedSubSubjects.value = current
    }

    fun toggleChapter(chapter: ChapterEntity) {
        viewModelScope.launch {
            repository.toggleChapterCompletion(chapter)
        }
    }
}
