package com.example.flashcard_quiz_app

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import com.example.flashcard_quiz_app.ui.AppNavigation
import com.example.flashcard_quiz_app.ui.theme.FlashCardQuizAppTheme
import com.google.firebase.FirebaseApp

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            FlashCardQuizAppTheme{
                Surface(modifier = Modifier.fillMaxSize()) {
                    FirebaseApp.initializeApp(this)
                    AppNavigation()
                 }
            }
        }
    }
}
