package com.aksharadeepa.tutor

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import com.aksharadeepa.tutor.ui.navigation.TutorAppNavigation
import com.aksharadeepa.tutor.ui.theme.AksharaDeepaTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        
        val app = application as TutorApplication
        
        setContent {
            AksharaDeepaTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    TutorAppNavigation(app = app)
                }
            }
        }
    }
}
