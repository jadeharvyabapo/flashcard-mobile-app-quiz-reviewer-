// CreateFlashcardScreen.kt
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
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
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
import com.example.flashcard_quiz_app.viewmodel.FlashcardViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CreateFlashcardScreen(
    viewModel: FlashcardViewModel,
    navController: NavController,
    initialSubject: String? = null // Receive the initial subject
) {
    var question by remember { mutableStateOf("") }
    var questionError by remember { mutableStateOf("") }
    var answer by remember { mutableStateOf("") }
    var answerError by remember { mutableStateOf("") }
    val subjects = viewModel.subjects.observeAsState(initial = emptyList())
    var expanded by remember { mutableStateOf(false) }
    var selectedSubject by remember { mutableStateOf(initialSubject ?: "") } // Set initial subject
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
                contentScale = ContentScale.Crop,
                modifier = imageModifier
            )

            if (!initialSubject.isNullOrBlank()) {
                Text(
                    text = "Adding to Subject: $initialSubject",
                    color = Color.Black,
                    modifier = Modifier.padding(top = 16.dp)
                )
            }

            TextField(
                value = question,
                onValueChange = { question = it },
                label = { Text("Enter your Question", color = Color.Black) },
                textStyle = TextStyle(color = Color.Black),
                colors = TextFieldDefaults.colors(
                    focusedIndicatorColor = Color.Black,
                    unfocusedIndicatorColor = Color.Gray,
                    focusedLabelColor = Color.Black,
                    unfocusedLabelColor = Color.Gray,
                    cursorColor = Color.Black
                ),
                modifier = Modifier
                    .padding(start = 16.dp, top = 16.dp) // Adjusted top padding
                    .fillMaxWidth(0.95f),
                isError = questionError.isNotEmpty()
            )
            if (questionError.isNotEmpty()) {
                Text(text = questionError, color = Color.Red)
            }

            TextField(
                value = answer,
                onValueChange = { answer = it },
                label = { Text("Enter your Answer", color = Color.Black) },
                textStyle = TextStyle(color = Color.Black),
                colors = TextFieldDefaults.colors(
                    focusedIndicatorColor = Color.Black,
                    unfocusedIndicatorColor = Color.Gray,
                    focusedLabelColor = Color.Black,
                    unfocusedLabelColor = Color.Gray,
                    cursorColor = Color.Black
                ),
                modifier = Modifier
                    .padding(start = 16.dp, top = 16.dp)
                    .fillMaxWidth(0.95f),
                isError = answerError.isNotEmpty()
            )
            if (answerError.isNotEmpty()) {
                Text(text = answerError, color = Color.Red)
            }

            // Dropdown for Subject Selection (only show if no initial subject)
            if (initialSubject.isNullOrBlank()) {
                ExposedDropdownMenuBox(
                    expanded = expanded,
                    onExpandedChange = { expanded = !it },
                    modifier = Modifier
                        .padding(start = 16.dp, top = 16.dp)
                        .fillMaxWidth(0.95f)
                ) {

                    TextField(
                        readOnly = true,
                        value = selectedSubject,
                        onValueChange = { },
                        label = { Text("Select Subject", color = Color.Black) },
                        textStyle = TextStyle(color = Color.Black),
                        colors = ExposedDropdownMenuDefaults.textFieldColors(
                            focusedIndicatorColor = Color.Black,
                            unfocusedIndicatorColor = Color.Gray,
                            focusedLabelColor = Color.Black,
                            unfocusedLabelColor = Color.Gray,
                            cursorColor = Color.Black,
                            disabledTextColor = Color.Black
                        ),
                        modifier = Modifier.fillMaxWidth(),
                        trailingIcon = {
                            ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded)
                        }
                    )

                    ExposedDropdownMenu(
                        expanded = expanded,
                        onDismissRequest = { expanded = false }
                    ) {
                        subjects.value.forEach { subject ->
                            DropdownMenuItem(
                                text = { Text(subject) },
                                onClick = {
                                    selectedSubject = subject
                                    expanded = false
                                }
                            )
                        }
                        if (subjects.value.isEmpty()) {
                            DropdownMenuItem(
                                text = { Text("No subjects available") },
                                enabled = false,
                                onClick = {}
                            )
                        } else {
                            DropdownMenuItem(
                                text = { Text("Add New Subject") },
                                onClick = {
                                    // Implement navigation or a dialog to add a new subject
                                    expanded = false
                                    // You might want to navigate to a "Create Subject" screen or show a dialog here.
                                }
                            )
                        }
                    }
                }
            } else {
                // If there's an initial subject, we still need to ensure it's used when saving
                selectedSubject = initialSubject
            }

            Row {
                FilledTonalButton(
                    onClick = {
                        if (question.isBlank()) {
                            questionError = "Question field cannot be empty!"
                        } else if (answer.isBlank()) {
                            answerError = "Answer field cannot be empty!"
                        } else if (selectedSubject.isBlank() && subjects.value.isNotEmpty() && initialSubject.isNullOrBlank()) {
                            openDialog.value = true
                        } else {
                            viewModel.addFlashcard(question, answer, selectedSubject)
                            openDialog.value = true
                            questionError = ""
                            answerError = ""
                            question = ""
                            answer = ""
                            // Don't reset selectedSubject if we came from a specific subject
                            if (initialSubject == null) {
                                selectedSubject = ""
                            }
                        }
                    },
                    modifier = Modifier.padding(start = 16.dp, top = 35.dp),
                    colors = ButtonDefaults.buttonColors(Color.Black, contentColor = Color.White)
                ) {
                    Text("Save Flashcard")
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
                    onClick = { navController.popBackStack() }, // Go back to the previous screen
                    modifier = Modifier.padding(start = 16.dp, top = 35.dp),
                    colors = ButtonDefaults.buttonColors(Color.Black, contentColor = Color.White)
                )
                {
                    Text("Back")
                }
            }

            FilledTonalButton(
                onClick = { navController.navigate("quiz/${selectedSubject}") }, // Pass selectedSubject
                modifier = Modifier.padding(start = 26.dp, top = 25.dp),
                colors = ButtonDefaults.buttonColors(Color.Black, contentColor = Color.White)
            )
            {
                Text("Start Quiz")
            }
        }
    }
}