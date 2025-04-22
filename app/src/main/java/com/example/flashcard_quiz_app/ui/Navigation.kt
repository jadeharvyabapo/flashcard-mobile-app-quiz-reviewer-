package com.example.flashcard_quiz_app.ui

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.flashcard_quiz_app.viewmodel.FlashcardViewModel
import com.example.flashcard_quiz_app.ui.screens.CreateFlashcardScreen
import com.example.flashcard_quiz_app.ui.screens.MySubjectsScreen
import com.example.flashcard_quiz_app.ui.screens.QuizScreen
import com.example.flashcard_quiz_app.ui.screens.ScoreScreen
import com.example.flashcard_quiz_app.ui.screens.ViewFlashcardsScreen
import com.example.flashcard_quiz_app.ui.screens.LoginScreen
import com.example.flashcard_quiz_app.ui.screens.SignupScreen
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.flashcard_quiz_app.viewmodel.AuthViewModel

@Composable
fun AppNavigation() {
    val navController = rememberNavController()
    val flashcardViewModel: FlashcardViewModel = viewModel()
    val authViewModel: AuthViewModel = viewModel()

    NavHost(navController = navController, startDestination = "login") {
        composable("login") { LoginScreen(navController = navController, authViewModel = authViewModel) }
        composable("signup") { SignupScreen(navController = navController, authViewModel = authViewModel) }
        composable("my_subjects") { MySubjectsScreen(viewModel = flashcardViewModel, navController = navController) }
        composable("create_flashcard") { CreateFlashcardScreen(viewModel = flashcardViewModel, navController = navController) }
        composable("create_flashcard/{subject}") { backStackEntry ->
            val subject = backStackEntry.arguments?.getString("subject")
            CreateFlashcardScreen(viewModel = flashcardViewModel, navController = navController, initialSubject = subject)
        }
        composable("view_flashcards") { ViewFlashcardsScreen(viewModel = flashcardViewModel, navController = navController) }
        composable("subject_flashcards/{subject}") { backStackEntry ->
            val subject = backStackEntry.arguments?.getString("subject")
            if (!subject.isNullOrBlank()) {
                ViewFlashcardsScreen(viewModel = flashcardViewModel, navController = navController, subjectFilter = subject)
            }
        }
        composable("quiz") { QuizScreen(viewModel = flashcardViewModel, navController = navController) } // General quiz route
        composable("quiz/{subjectFilter}") { backStackEntry -> // Route to handle subject-specific quizzes
            val subjectFilter = backStackEntry.arguments?.getString("subjectFilter")
            QuizScreen(viewModel = flashcardViewModel, navController = navController, subjectFilter = subjectFilter)
        }
        composable("score/{correctAnswers}/{totalQuestions}") { backStackEntry ->
            val correctAnswers = backStackEntry.arguments?.getString("correctAnswers")?.toIntOrNull() ?: 0
            val totalQuestions = backStackEntry.arguments?.getString("totalQuestions")?.toIntOrNull() ?: 0
            ScoreScreen(correctAnswers, totalQuestions, navController)
        }
    }
}