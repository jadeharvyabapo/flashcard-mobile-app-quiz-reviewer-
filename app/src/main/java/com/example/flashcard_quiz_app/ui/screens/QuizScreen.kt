package com.example.flashcard_quiz_app.ui.screens

import android.util.Log
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.flashcard_quiz_app.viewmodel.FlashcardViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun QuizScreen(viewModel: FlashcardViewModel, navController: NavController, subjectFilter: String? = null) {
    val allFlashcards = viewModel.flashcards.observeAsState(initial = emptyList())
    val flashcards by remember(allFlashcards.value, subjectFilter) {
        mutableStateOf(
            if (subjectFilter.isNullOrBlank()) {
                allFlashcards.value.shuffled()
            } else {
                allFlashcards.value.filter { it.subject == subjectFilter }.shuffled()
            }
        )
    }
    val isLoading = viewModel.isLoading.observeAsState(initial = false)
    var currentQuestionIndex by remember { mutableIntStateOf(0) }
    var answer by remember { mutableStateOf("") }
    var showFeedback by remember { mutableStateOf(false) }
    var isCorrect by remember { mutableStateOf(false) }
    var correctAnswer by remember { mutableStateOf("") }
    var correctAnswersCount by remember { mutableIntStateOf(0) }

    Log.d("QuizScreen", "Flashcards size: ${flashcards.size}, Current index: $currentQuestionIndex, isLoading: ${isLoading.value}")

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center // <-- Updated for centering
    ) {
        // Progress Indicator
        if (flashcards.isNotEmpty()) {
            LinearProgressIndicator(
                progress = (currentQuestionIndex + 1).toFloat() / flashcards.size,
                modifier = Modifier
                    .fillMaxWidth(0.9f)
                    .padding(top = 16.dp)
            )

        }

        if (isLoading.value) {
            CircularProgressIndicator()
        } else if (flashcards.isNotEmpty() && currentQuestionIndex < flashcards.size) {
            val currentQuestion = flashcards[currentQuestionIndex]

            Card(
                modifier = Modifier
                    .fillMaxWidth(0.9f),
                elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
            ) {
                Column(
                    modifier = Modifier
                        .padding(16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    Text(
                        text = "Question:",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                    Text(
                        text = currentQuestion.question,
                        style = MaterialTheme.typography.bodyLarge,
                        color = MaterialTheme.colorScheme.onSurface,
                        lineHeight = 32.sp,
                        textAlign = androidx.compose.ui.text.style.TextAlign.Center
                    )

                    OutlinedTextField(
                        value = answer,
                        onValueChange = { answer = it },
                        label = { Text("Answer", color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)) },
                        textStyle = TextStyle(color = MaterialTheme.colorScheme.onSurface),
                        colors = TextFieldDefaults.colors(
                            focusedIndicatorColor = MaterialTheme.colorScheme.primary,
                            unfocusedIndicatorColor = MaterialTheme.colorScheme.outline,
                            focusedLabelColor = MaterialTheme.colorScheme.primary,
                            unfocusedLabelColor = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f),
                            cursorColor = MaterialTheme.colorScheme.primary
                        ),
                        modifier = Modifier.fillMaxWidth()
                    )

                    Button(
                        onClick = {
                            Log.d("QuizScreen", "User Answer (before trim): '$answer'")
                            Log.d("QuizScreen", "Correct Answer (from data, before trim): '${currentQuestion.answer}'")
                            val trimmedUserAnswer = answer.trim()
                            val trimmedCorrectAnswer = currentQuestion.answer?.trim() ?: ""
                            Log.d("QuizScreen", "User Answer (after trim): '$trimmedUserAnswer'")
                            Log.d("QuizScreen", "Correct Answer (after trim): '$trimmedCorrectAnswer'")

                            Log.d("QuizScreen", "User Answer Characters:")
                            trimmedUserAnswer.forEach { char -> Log.d("QuizScreen", "'$char': ${char.code}") }

                            Log.d("QuizScreen", "Correct Answer Characters:")
                            trimmedCorrectAnswer.forEach { char -> Log.d("QuizScreen", "'$char': ${char.code}") }

                            val isAnswerCorrectCalculated =
                                trimmedUserAnswer.equals(trimmedCorrectAnswer, ignoreCase = true)
                            Log.d("QuizScreen", "isAnswerCorrectCalculated: $isAnswerCorrectCalculated")
                            isCorrect = isAnswerCorrectCalculated
                            showFeedback = true
                            correctAnswer = currentQuestion.answer.orEmpty()
                            if (isAnswerCorrectCalculated) {
                                correctAnswersCount++
                            }
                        },
                        modifier = Modifier.fillMaxWidth(),
                        colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary, contentColor = MaterialTheme.colorScheme.onPrimary)
                    ) {
                        Text("Submit Answer")
                    }

                    AnimatedVisibility(visible = showFeedback) {
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            val feedbackText = if (isCorrect) "Correct!" else "Incorrect. Correct answer was: $correctAnswer"
                            val feedbackColor = if (isCorrect) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.error

                            Text(
                                text = feedbackText,
                                color = feedbackColor,
                                fontWeight = FontWeight.Bold,
                                style = MaterialTheme.typography.bodyLarge
                            )
                            Button(
                                onClick = {
                                    currentQuestionIndex++
                                    answer = ""
                                    showFeedback = false
                                    isCorrect = false
                                    correctAnswer = ""
                                },
                                colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.secondary, contentColor = MaterialTheme.colorScheme.onSecondary)
                            ) {
                                Text("Next Question")
                            }
                        }
                    }
                }
            }
        } else if (flashcards.isNotEmpty()) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .background(MaterialTheme.colorScheme.background)
                    .padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Text(
                    text = "Quiz Completed!",
                    style = MaterialTheme.typography.headlineSmall,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onBackground,
                    modifier = Modifier.padding(bottom = 16.dp)
                )
                Button(
                    onClick = {
                        navController.navigate("score/${correctAnswersCount}/${flashcards.size}") {
                            popUpTo("quiz") { inclusive = true }
                            launchSingleTop = true
                        }
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary, contentColor = MaterialTheme.colorScheme.onPrimary)
                ) {
                    Text("View Score")
                }
                Spacer(modifier = Modifier.height(16.dp))
                Button(
                    onClick = {
                        navController.navigate("my_subjects") {
                            popUpTo("quiz") { inclusive = true }
                            launchSingleTop = true
                        }
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.secondaryContainer, contentColor = MaterialTheme.colorScheme.onSecondaryContainer)
                ) {
                    Text("Back to Subjects")
                }
            }
        } else {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier
                    .fillMaxSize()
                    .background(MaterialTheme.colorScheme.background)
                    .padding(16.dp),
                verticalArrangement = Arrangement.Center
            ) {
                Text(
                    text = if (!subjectFilter.isNullOrBlank()) {
                        "No flashcards available for the subject: $subjectFilter to start the quiz."
                    } else {
                        "No flashcards available to start the quiz."
                    },
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.7f),
                    textAlign = androidx.compose.ui.text.style.TextAlign.Center
                )
                Spacer(modifier = Modifier.height(16.dp))
                Button(onClick = { navController.popBackStack() }) {
                    Text("Back")
                }
            }
        }
    }
}
