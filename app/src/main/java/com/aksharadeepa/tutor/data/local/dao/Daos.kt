package com.aksharadeepa.tutor.data.local.dao

import androidx.room.*
import com.aksharadeepa.tutor.data.local.entity.*
import kotlinx.coroutines.flow.Flow

@Dao
interface SubjectDao {
    @Query("SELECT * FROM subjects")
    fun getAllSubjects(): Flow<List<SubjectEntity>>

    @Query("SELECT * FROM subjects WHERE id = :id")
    fun getSubjectById(id: Int): Flow<SubjectEntity?>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertSubjects(subjects: List<SubjectEntity>)

    @Update
    suspend fun updateSubject(subject: SubjectEntity)
}

@Dao
interface SubSubjectDao {
    @Query("SELECT * FROM sub_subjects")
    fun getAllSubSubjects(): Flow<List<SubSubjectEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertSubSubjects(subSubjects: List<SubSubjectEntity>)

    @Update
    suspend fun updateSubSubject(subSubject: SubSubjectEntity)
}

@Dao
interface ChapterDao {
    @Query("SELECT * FROM chapters")
    fun getAllChapters(): Flow<List<ChapterEntity>>

    @Query("SELECT * FROM chapters WHERE subjectId = :subjectId")
    fun getChaptersBySubject(subjectId: Int): Flow<List<ChapterEntity>>

    @Query("SELECT * FROM chapters WHERE subjectId = :subjectId")
    suspend fun getChaptersListBySubject(subjectId: Int): List<ChapterEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertChapters(chapters: List<ChapterEntity>)

    @Update
    suspend fun updateChapter(chapter: ChapterEntity)
}

@Dao
interface QuestionDao {
    @Query("SELECT * FROM questions WHERE subjectId = :subjectId ORDER BY RANDOM() LIMIT :limit")
    suspend fun getRandomQuestionsForSubject(subjectId: Int, limit: Int): List<QuestionEntity>

    @Query("SELECT * FROM questions WHERE chapterId = :chapterId ORDER BY RANDOM() LIMIT :limit")
    suspend fun getRandomQuestionsForChapter(chapterId: Int, limit: Int): List<QuestionEntity>

    @Query("SELECT * FROM questions WHERE chapterId = :chapterId AND id NOT IN (:excludeIds) ORDER BY RANDOM() LIMIT :limit")
    suspend fun getQuestionsExcluding(chapterId: Int, excludeIds: List<Int>, limit: Int): List<QuestionEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertQuestions(questions: List<QuestionEntity>)
}

@Dao
interface QuizResultDao {
    @Query("SELECT * FROM quiz_results ORDER BY timestamp DESC")
    fun getAllResults(): Flow<List<QuizResultEntity>>

    @Query("SELECT * FROM quiz_results WHERE subjectId = :subjectId")
    fun getResultsBySubject(subjectId: Int): Flow<List<QuizResultEntity>>

    @Query("SELECT MAX(score) FROM quiz_results WHERE chapterId = :chapterId")
    fun getBestScoreForChapter(chapterId: Int): Flow<Int?>

    @Query("SELECT COUNT(*) FROM quiz_results WHERE chapterId = :chapterId")
    fun getAttemptsForChapter(chapterId: Int): Flow<Int>

    @Insert
    suspend fun insertResult(result: QuizResultEntity)
}

@Dao
interface DailyGoalDao {
    @Query("SELECT * FROM daily_goals WHERE dateStr = :dateStr")
    fun getGoalByDate(dateStr: String): Flow<DailyGoalEntity?>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertGoal(goal: DailyGoalEntity)

    @Update
    suspend fun updateGoal(goal: DailyGoalEntity)
}
