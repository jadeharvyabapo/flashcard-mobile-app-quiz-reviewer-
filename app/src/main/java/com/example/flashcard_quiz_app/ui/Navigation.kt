package com.example.flashcard_quiz_app.ui

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.flashcard_quiz_app.ui.screens.CreateFlashcardScreen
import com.example.flashcard_quiz_app.ui.screens.QuizScreen
import com.example.flashcard_quiz_app.ui.screens.ScoreScreen
import com.example.flashcard_quiz_app.ui.screens.ViewFlashcardsScreen

@Composable
fun AppNavigation() {
    val navController = rememberNavController()
    NavHost(navController = navController, startDestination = "create_flashcard") {
        composable("create_flashcard") { CreateFlashcardScreen(viewModel = FlashcardViewModel(), navController = navController) }
        composable("view_flashcards") { ViewFlashcardsScreen(viewModel = FlashcardViewModel(), navController) } // Ensure this is defined
        composable("quiz") { QuizScreen(viewModel = FlashcardViewModel(), navController = navController) }
        composable("score/{score}") {  backStackEntry ->
            // Get the score from the navigation argument
            val score = backStackEntry.arguments?.getString("score")
            ScoreScreen(score, navController) }
    }
}
