package com.aksharadeepa.tutor

import android.app.Application
import com.aksharadeepa.tutor.data.local.AppDatabase
import com.aksharadeepa.tutor.data.local.UserPreferences
import com.aksharadeepa.tutor.data.repository.TutorRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class TutorApplication : Application() {
    
    val database by lazy { AppDatabase.getDatabase(this) }
    val userPreferences by lazy { UserPreferences(this) }
    val repository by lazy { TutorRepository(database, userPreferences) }

    override fun onCreate() {
        super.onCreate()
        
        CoroutineScope(Dispatchers.IO).launch {
            repository.preloadDummyDataIfNeeded()
        }
    }
}
