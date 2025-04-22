// MySubjectsScreen.kt
package com.example.flashcard_quiz_app.ui.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.flashcard_quiz_app.viewmodel.FlashcardViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MySubjectsScreen(viewModel: FlashcardViewModel, navController: NavController) {
    val subjects = viewModel.subjects.observeAsState(initial = emptyList())
    val isLoading = viewModel.isLoading.observeAsState(initial = true)
    var showDialog by remember { mutableStateOf(false) }
    var newSubject by remember { mutableStateOf("") }
    val flashcards by viewModel.flashcards.observeAsState(initial = emptyList()) // Observe flashcards

    LaunchedEffect(key1 = true) {
        viewModel.fetchFlashcards() // Fetch all flashcards when the screen is shown
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Subjects") }
            )
        },
        floatingActionButton = {
             FloatingActionButton(onClick = { showDialog = true }) {
                Icon(Icons.Filled.Add, "Add new subject")
            }
        },
        content = { paddingValues ->
            Column(
                modifier = Modifier
                    .padding(paddingValues)
                    .fillMaxSize()
                    .padding(horizontal = 16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                if (isLoading.value) {
                    CircularProgressIndicator(modifier = Modifier.padding(top = 32.dp))
                } else if (subjects.value.isEmpty()) {
                    Text(
                        "No subjects created yet. Click the '+' to add your first subject.",
                        color = Color.Gray,
                        textAlign = androidx.compose.ui.text.style.TextAlign.Center,
                    )
                } else {
                    LazyColumn(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 8.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        item {
                            SubjectCard(
                                subject = "All Flashcards",
                                cardCount = flashcards.size, // Use the observed flashcards
                                onClick = { navController.navigate("view_flashcards") }
                            )
                        }
                        items(subjects.value, key = { it }) { subject ->
                            val cardCountForSubject = flashcards.count { it.subject == subject } // Use the observed flashcards
                            SubjectCard(
                                subject = subject,
                                cardCount = cardCountForSubject,
                                onClick = { navController.navigate("subject_flashcards/$subject") },
                                onDelete = { viewModel.removeSubject(subject) }
                            )
                        }
                    }
                }
            }
        }
    )

    if (showDialog) {
        AlertDialog(
            onDismissRequest = { showDialog = false },
            title = { Text("Add New Subject") },
            text = {
                OutlinedTextField(
                    value = newSubject,
                    onValueChange = { newSubject = it },
                    label = { Text("Subject Name") },
                    singleLine = true
                )
            },
            confirmButton = {
                Button(
                    onClick = {
                        if (newSubject.isNotBlank()) {
                            viewModel.addSubject(newSubject)
                            newSubject = ""
                            showDialog = false
                        }
                    }
                ) {
                    Text("Add")
                }
            },
            dismissButton = {
                Button(onClick = { showDialog = false }) {
                    Text("Cancel")
                }
            }
        )
    }
}

@Composable
fun SubjectCard(subject: String, cardCount: Int, onClick: () -> Unit, onDelete: (() -> Unit)? = null) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Row(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = subject,
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.SemiBold
            )
            Text(
                text = "$cardCount Cards",
                style = MaterialTheme.typography.bodyMedium,
                color = Color.Gray
            )
            if (onDelete != null) {
                Spacer(modifier = Modifier.width(8.dp))
                Button(onClick = onDelete) {
                    Text("Remove")
                }
            }
        }
    }
}