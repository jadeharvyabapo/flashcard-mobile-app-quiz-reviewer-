package com.example.flashcard_quiz_app.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.flashcard_quiz_app.R
import com.example.flashcard_quiz_app.ui.FlashcardViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CreateFlashcardScreen(viewModel: FlashcardViewModel, navController: NavController) {
    var question by remember { mutableStateOf("") }
    var questionError by remember { mutableStateOf("") }
    var answer by remember { mutableStateOf("") }
    var answerError by remember { mutableStateOf("") }
    val openDialog = remember { mutableStateOf(false) }

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
                .size(280.dp)
                .padding(top = 18.dp)
            Image(
                painter = painterResource(id = R.drawable.images),
                contentDescription = null,
                // modifier = Modifier.padding(top = 73.dp),
                contentScale = ContentScale.Crop,
                modifier = imageModifier
            )
            TextField(
                value = question,
                onValueChange = { question = it },
                label = { Text("Enter your Question", color = Color.White) },
                textStyle = TextStyle(color = Color.White),
                colors = TextFieldDefaults
                    .colors(Color.hsl(15f, 0.9f, 0.9f)),
                modifier = Modifier
                    .padding(start = 16.dp, top = 96.dp)
                    .fillMaxWidth(0.95f),
                isError = questionError.isNotEmpty()
            )
            if (questionError.isNotEmpty()) {
                Text(text = questionError, color = Color.Red)
            }

            TextField(
                value = answer,
                onValueChange = { answer = it },
                label = { Text("Enter your Answer", color = Color.White) },
                textStyle = TextStyle(color = Color.White),
                colors = TextFieldDefaults.colors(Color.hsl(
                        15f,
                        0.9f,
                        0.9f
                    )
                ),
                modifier = Modifier
                    .padding(start = 16.dp, top = 35.dp)
                    .fillMaxWidth(0.95f),
                isError = answerError.isNotEmpty()
            )
            if (answerError.isNotEmpty()) {
                Text(text = answerError, color = Color.Red)
            }

            Row {
                FilledTonalButton(
                    onClick = {
                        if (question.isBlank()) {
                            questionError = "Question field cannot be empty!"
                        } else if (answer.isBlank()) {
                            answerError = "Answer field cannot be empty!"
                        } else {
                            viewModel.addFlashcard(question, answer)
                            openDialog.value = true
                            questionError = ""
                            answerError = ""
                        }
                    },
                    modifier = Modifier.padding(start = 16.dp, top = 85.dp),
                    colors = ButtonDefaults.buttonColors(Color.hsl(15f, 0.9f, 0.9f))
                ) {
                    Text("Save Flashcard", color = Color.Black)
                }
                if (openDialog.value) {
                    AlertDialog(
                        onDismissRequest = { openDialog.value = false },
                        title = { Text("Success") },
                        text = { Text("Your question is saved!") },
                        confirmButton = {
                            FilledTonalButton(onClick = { openDialog.value = false }) {
                                Text("OK")
                            }
                        }
                    )
                }


                FilledTonalButton(
                    onClick = { navController.navigate("view_flashcards") },
                    modifier = Modifier.padding(start = 16.dp, top = 85.dp),
                    colors = ButtonDefaults.buttonColors(Color.hsl(15f, 0.9f, 0.9f))
                )
                {
                    Text("View Flashcards", color = Color.Black)
                }
            }

            FilledTonalButton(
                onClick = { navController.navigate("quiz") },
                modifier = Modifier.padding(start = 26.dp, top = 25.dp),
                colors = ButtonDefaults.buttonColors(Color.hsl(15f, 0.9f, 0.9f))
            )
            {
                Text("Start Quiz", color = Color.Black)
            }
        }
    }
}







