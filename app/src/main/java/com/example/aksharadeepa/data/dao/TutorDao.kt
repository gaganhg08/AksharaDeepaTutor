package com.example.aksharadeepa.data.dao

import androidx.room.*
import kotlinx.coroutines.flow.Flow
import com.example.aksharadeepa.data.entity.*

@Dao
interface TutorDao {
    @Query("SELECT * FROM subjects")
    fun getAllSubjects(): Flow<List<Subject>>

    @Query("SELECT * FROM chapters WHERE subjectId = :subjectId")
    fun getChaptersForSubject(subjectId: Int): Flow<List<Chapter>>

    @Query("SELECT * FROM chapters")
    fun getAllChapters(): Flow<List<Chapter>>

    @Query("SELECT * FROM questions WHERE chapterId = :chapterId")
    suspend fun getQuestionsForChapter(chapterId: Int): List<Question>

    @Update
    suspend fun updateChapter(chapter: Chapter)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertSubjects(subjects: List<Subject>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertChapters(chapters: List<Chapter>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertQuestions(questions: List<Question>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertQuizResult(result: QuizResult)

    @Query("SELECT * FROM quiz_results WHERE subjectId = :subjectId")
    fun getQuizResultsForSubject(subjectId: Int): Flow<List<QuizResult>>
    
    @Query("SELECT * FROM quiz_results")
    fun getAllQuizResults(): Flow<List<QuizResult>>
}
