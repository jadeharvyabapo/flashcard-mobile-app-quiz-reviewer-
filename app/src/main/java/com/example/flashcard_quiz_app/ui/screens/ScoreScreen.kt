package com.example.flashcard_quiz_app.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController

@Composable
fun ScoreScreen(correctAnswers: Int, totalQuestions: Int, navController: NavController) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Your Score",
            fontSize = 32.sp,
            fontWeight = FontWeight.Bold,
            color = Color.Black
        )
        Text(
            text = "$correctAnswers / $totalQuestions", // Display the score
            fontSize = 48.sp,
            fontWeight = FontWeight.ExtraBold,
            color = Color.Black,
            modifier = Modifier.padding(top = 16.dp)
        )
        Button(
            onClick = {
                navController.navigate("quiz") {
                    popUpTo("score/{correctAnswers}/{totalQuestions}") { inclusive = true }
                    launchSingleTop = true
                }
            },
            modifier = Modifier.padding(top = 8.dp)
        ) {
            Text("Retake Quiz")
        }

        // Button to Go Back to Subjects
        Button(
            onClick = {
                navController.navigate("my_subjects") {
                    popUpTo("score/{correctAnswers}/{totalQuestions}") { inclusive = true }
                    launchSingleTop = true
                }
            },
            modifier = Modifier.padding(top = 16.dp)
        ) {
            Text("Back to Subjects")
        }
    }
}