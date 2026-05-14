package com.aksharadeepa.tutor.data.local.entity

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import androidx.room.Index

@Entity(tableName = "subjects")
data class SubjectEntity(
    @PrimaryKey val id: Int,
    val name: String,
    val description: String,
    val progressPercentage: Int = 0
)

@Entity(
    tableName = "sub_subjects",
    foreignKeys = [
        ForeignKey(
            entity = SubjectEntity::class,
            parentColumns = ["id"],
            childColumns = ["subjectId"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [Index(value = ["subjectId"])]
)
data class SubSubjectEntity(
    @PrimaryKey val id: Int,
    val subjectId: Int,
    val name: String,
    val progressPercentage: Int = 0
)

@Entity(
    tableName = "chapters",
    foreignKeys = [
        ForeignKey(
            entity = SubjectEntity::class,
            parentColumns = ["id"],
            childColumns = ["subjectId"],
            onDelete = ForeignKey.CASCADE
        ),
        ForeignKey(
            entity = SubSubjectEntity::class,
            parentColumns = ["id"],
            childColumns = ["subSubjectId"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [Index(value = ["subjectId"]), Index(value = ["subSubjectId"])]
)
data class ChapterEntity(
    @PrimaryKey val id: Int,
    val subjectId: Int,
    val subSubjectId: Int,
    val title: String,
    val isCompleted: Boolean = false,
    val readProgress: Float = 0f,
    val completionTimestamp: Long? = null
)

@Entity(
    tableName = "questions",
    foreignKeys = [
        ForeignKey(
            entity = SubjectEntity::class,
            parentColumns = ["id"],
            childColumns = ["subjectId"],
            onDelete = ForeignKey.CASCADE
        ),
        ForeignKey(
            entity = ChapterEntity::class,
            parentColumns = ["id"],
            childColumns = ["chapterId"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [Index(value = ["subjectId"]), Index(value = ["chapterId"])]
)
data class QuestionEntity(
    @PrimaryKey val id: Int,
    val subjectId: Int,
    val chapterId: Int,
    val text: String,
    val optionA: String,
    val optionB: String,
    val optionC: String,
    val optionD: String,
    val correctAnswer: String,
    val explanation: String
)

@Entity(
    tableName = "quiz_results",
    foreignKeys = [
        ForeignKey(
            entity = SubjectEntity::class,
            parentColumns = ["id"],
            childColumns = ["subjectId"],
            onDelete = ForeignKey.CASCADE
        ),
        ForeignKey(
            entity = ChapterEntity::class,
            parentColumns = ["id"],
            childColumns = ["chapterId"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [Index(value = ["subjectId"]), Index(value = ["chapterId"])]
)
data class QuizResultEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val subjectId: Int,
    val chapterId: Int,
    val score: Int,
    val totalQuestions: Int,
    val timestamp: Long
)

@Entity(tableName = "daily_goals")
data class DailyGoalEntity(
    @PrimaryKey val dateStr: String,
    val completedQuizzes: Int = 0,
    val chaptersRead: Int = 0,
    val targetQuizzes: Int = 2,
    val targetChapters: Int = 1
)
