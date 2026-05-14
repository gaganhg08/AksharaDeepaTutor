package com.example.aksharadeepa

import android.app.Application
import com.example.aksharadeepa.data.AppDatabase
import com.example.aksharadeepa.repository.TutorRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob

class TutorApplication : Application() {
    val applicationScope = CoroutineScope(SupervisorJob())
    val database by lazy { AppDatabase.getDatabase(this, applicationScope) }
    val repository by lazy { TutorRepository(database.tutorDao()) }
}
