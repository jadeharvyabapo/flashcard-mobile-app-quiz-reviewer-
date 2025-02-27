package com.example.flashcard_quiz_app.ui.screens

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.flashcard_quiz_app.R
import com.example.flashcard_quiz_app.ui.FlashcardViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun QuizScreen(viewModel: FlashcardViewModel, navController: NavController) {
    val flashcards = viewModel.flashcards.observeAsState(initial = emptyList())
    var currentQuestionIndex by remember { mutableIntStateOf(0) }
    var answer by remember { mutableStateOf("") }

    if (flashcards.value.isNotEmpty() && currentQuestionIndex < flashcards.value.size) {
        val currentQuestion = flashcards.value[currentQuestionIndex]

        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.White)
        ) {
            Column(
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                val imageModifier = Modifier
                    .size(300.dp)
                    .background(Color.White)
                    .padding(top = 18.dp)
                Image(
                    painter = painterResource(id = R.drawable.cartoon),
                    contentDescription = null,
                    // modifier = Modifier.padding(top = 73.dp),
                    contentScale = ContentScale.Crop,
                    modifier = imageModifier
                )

                Log.d("QuizScreen", "Current question: ${currentQuestion.question}")
                Text(
                    text = "Question:  " + currentQuestion.question,
                    fontSize = 23.sp,
                    lineHeight = 32.sp,
                    modifier = Modifier.padding(start = 12.dp, top = 73.dp),
                    color = Color.Black,
                )

                TextField(
                    value = answer,
                    onValueChange = { answer = it },
                    label = { Text("Answer", color = Color.White) },
                    textStyle = TextStyle(color = Color.White),
                    colors = TextFieldDefaults.colors(Color.hsl(15f, 0.9f, 0.9f)),
                    modifier = Modifier
                        .padding(start = 2.dp, top = 56.dp)
                        .fillMaxWidth(0.79f)
                )
                FilledTonalButton(
                    onClick = {
                        val isCorrect = viewModel.checkAnswer(answer, currentQuestionIndex)
                        if (isCorrect) {
                            // If the answer is correct, go to the next question
                            currentQuestionIndex++
                        } else {
                            // If the answer is incorrect, also go to the next question
                            currentQuestionIndex++
                        }
                        // Clear the answer for the next question
                        answer = ""
                    },
                    modifier = Modifier
                        .padding(start = 16.dp, top = 85.dp),
                       // .size(100.dp),
                    colors = ButtonDefaults.buttonColors(Color.hsl(15f, 0.9f, 0.9f))
                ) {
                    Text("Next Question")
                }

            }
        }
    }
    else  if (currentQuestionIndex >= flashcards.value.size - 1) {
        Column(
//            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.background(Color.White)
        ) {
            val imageModifier = Modifier
                .size(380.dp)
                .padding(top = 58.dp, bottom = 10.dp)
            Image(
                painter = painterResource(id = R.drawable.finish),
                contentDescription = null,
                // modifier = Modifier.padding(top = 73.dp),
                contentScale = ContentScale.Crop,
                modifier = imageModifier
            )
            Text(text = "Hurray!! You completed the quiz.",
                color = Color.Black,
                fontSize = 23.sp,
                fontWeight = FontWeight.W600,
                modifier = Modifier.padding(top = 110.dp))
            FilledTonalButton(
                onClick = {
                    navController.navigate("score/$currentQuestionIndex")
                },
                modifier = Modifier.padding(23.dp),
                  //  .size(120.dp),
                colors = ButtonDefaults.buttonColors(Color.hsl(15f, 0.9f, 0.9f))
            ) {
                Text("View Score")
            }
        }
    }
    else {
        Text("No flashcards available. Back to view score", modifier = Modifier.padding(140.dp))
    }
}

