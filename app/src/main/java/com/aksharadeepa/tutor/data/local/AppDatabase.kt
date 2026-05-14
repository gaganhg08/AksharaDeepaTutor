package com.aksharadeepa.tutor.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.aksharadeepa.tutor.data.local.dao.*
import com.aksharadeepa.tutor.data.local.entity.*

@Database(
    entities = [
        SubjectEntity::class,
        SubSubjectEntity::class,
        ChapterEntity::class,
        QuestionEntity::class,
        QuizResultEntity::class,
        DailyGoalEntity::class,
        ProfileEntity::class
    ],
    version = 8,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {

    abstract fun subjectDao(): SubjectDao
    abstract fun subSubjectDao(): SubSubjectDao
    abstract fun chapterDao(): ChapterDao
    abstract fun questionDao(): QuestionDao
    abstract fun quizResultDao(): QuizResultDao
    abstract fun dailyGoalDao(): DailyGoalDao
    abstract fun profileDao(): ProfileDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "tutor_database"
                )
                .fallbackToDestructiveMigration()
                .build()
                INSTANCE = instance
                instance
            }
        }
    }
}
