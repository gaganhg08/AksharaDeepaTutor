package com.example.aksharadeepa.repository

import com.example.aksharadeepa.data.dao.TutorDao
import com.example.aksharadeepa.data.entity.Chapter
import com.example.aksharadeepa.data.entity.Question
import com.example.aksharadeepa.data.entity.QuizResult
import com.example.aksharadeepa.data.entity.Subject
import kotlinx.coroutines.flow.Flow

class TutorRepository(private val tutorDao: TutorDao) {
    val allSubjects: Flow<List<Subject>> = tutorDao.getAllSubjects()
    val allChapters: Flow<List<Chapter>> = tutorDao.getAllChapters()
    val allQuizResults: Flow<List<QuizResult>> = tutorDao.getAllQuizResults()

    suspend fun getQuestionsForChapter(chapterId: Int): List<Question> {
        return tutorDao.getQuestionsForChapter(chapterId)
    }

    suspend fun updateChapter(chapter: Chapter) {
        tutorDao.updateChapter(chapter)
    }

    suspend fun insertQuizResult(result: QuizResult) {
        tutorDao.insertQuizResult(result)
    }
}
