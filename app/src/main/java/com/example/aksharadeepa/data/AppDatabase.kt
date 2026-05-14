package com.example.aksharadeepa.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import com.example.aksharadeepa.data.dao.TutorDao
import com.example.aksharadeepa.data.entity.Chapter
import com.example.aksharadeepa.data.entity.Question
import com.example.aksharadeepa.data.entity.QuizResult
import com.example.aksharadeepa.data.entity.Subject
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@Database(entities = [Subject::class, Chapter::class, Question::class, QuizResult::class], version = 1, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {
    abstract fun tutorDao(): TutorDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context, scope: CoroutineScope): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "tutor_database"
                )
                .addCallback(TutorDatabaseCallback(scope))
                .build()
                INSTANCE = instance
                instance
            }
        }
    }

    private class TutorDatabaseCallback(
        private val scope: CoroutineScope
    ) : RoomDatabase.Callback() {
        override fun onCreate(db: SupportSQLiteDatabase) {
            super.onCreate(db)
            INSTANCE?.let { database ->
                scope.launch(Dispatchers.IO) {
                    populateDatabase(database.tutorDao())
                }
            }
        }

        suspend fun populateDatabase(dao: TutorDao) {
            // Add Subjects
            val subjects = listOf(
                Subject(id = 1, name = "Science"),
                Subject(id = 2, name = "Mathematics"),
                Subject(id = 3, name = "Social Studies")
            )
            dao.insertSubjects(subjects)

            // Add Chapters
            val chapters = mutableListOf<Chapter>()
            // Science Chapters
            chapters.add(Chapter(id = 1, subjectId = 1, title = "Chemical Reactions and Equations"))
            chapters.add(Chapter(id = 2, subjectId = 1, title = "Acids, Bases and Salts"))
            // Math Chapters
            chapters.add(Chapter(id = 3, subjectId = 2, title = "Real Numbers"))
            chapters.add(Chapter(id = 4, subjectId = 2, title = "Polynomials"))
            // Social Studies Chapters
            chapters.add(Chapter(id = 5, subjectId = 3, title = "The Rise of Nationalism in Europe"))
            chapters.add(Chapter(id = 6, subjectId = 3, title = "Nationalism in India"))
            
            dao.insertChapters(chapters)

            // Add Questions (5 per chapter)
            val questions = mutableListOf<Question>()
            // Chap 1 Science
            questions.add(Question(0, 1, "Which of the following is a balanced equation?", "Mg + O2 -> MgO", "2Mg + O2 -> 2MgO", "Mg + 2O2 -> MgO2", "None", 1))
            questions.add(Question(0, 1, "Rusting of iron is an example of:", "Reduction", "Oxidation", "Sublimation", "Condensation", 1))
            questions.add(Question(0, 1, "What happens when dilute HCl is added to iron filings?", "H2 gas and FeCl2 are produced", "Cl2 gas and Fe(OH)2 are produced", "No reaction takes place", "Iron salt and water are produced", 0))
            questions.add(Question(0, 1, "A chemical equation is balanced in accordance with the law of:", "Conservation of mass", "Multiple proportions", "Constant proportions", "Reciprocal proportions", 0))
            questions.add(Question(0, 1, "Which of the following is an endothermic process?", "Dilution of sulphuric acid", "Sublimation of dry ice", "Condensation of water vapours", "Respiration in human beings", 1))
            
            for (i in 2..6) {
                for (j in 1..5) {
                    questions.add(Question(0, i, "Sample Question $j for Chapter $i?", "Option A", "Option B", "Option C", "Option D", 0))
                }
            }
            dao.insertQuestions(questions)
        }
    }
}
