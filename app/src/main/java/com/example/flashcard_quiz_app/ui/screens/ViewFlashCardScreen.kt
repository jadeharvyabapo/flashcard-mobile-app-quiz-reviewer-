package com.example.flashcard_quiz_app.ui.screens

import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
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
import com.example.flashcard_quiz_app.model.Flashcard
import com.example.flashcard_quiz_app.viewmodel.FlashcardViewModel
import androidx.compose.runtime.LaunchedEffect

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ViewFlashcardsScreen(
    viewModel: FlashcardViewModel,
    navController: NavController,
    subjectFilter: String? = null // Add this optional parameter
) {
    val allFlashcards by viewModel.flashcards.observeAsState(initial = emptyList())
    val isLoading by viewModel.isLoading.observeAsState(initial = false)
    val scrollState = rememberScrollState()

    // Create a mutable state to hold the currently displayed flashcards
    val displayedFlashcards = remember(allFlashcards) { mutableStateListOf<Flashcard>().apply { addAll(allFlashcards) } }

    LaunchedEffect(subjectFilter) { // Re-fetch when subjectFilter changes
        viewModel.fetchFlashcards(subjectFilter)
        // Update displayedFlashcards when allFlashcards changes due to a re-fetch
        displayedFlashcards.clear()
        displayedFlashcards.addAll(allFlashcards)
    }

    Scaffold(
        floatingActionButton = {
            FloatingActionButton(onClick = {
                navController.navigate("create_flashcard/${subjectFilter ?: ""}")
            }) {
                Icon(Icons.Filled.Add, "Add new flashcard")
            }
        },
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        if (!subjectFilter.isNullOrBlank()) {
                            "Flashcards for ${subjectFilter}"
                        } else {
                            "All Flashcards"
                        }
                    )
                },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.Filled.ArrowBack, "Back")
                    }
                }
            )
        },
        content = { paddingValues ->
            Column(
                modifier = Modifier
                    .padding(paddingValues)
                    .background(Color.White)
                    .fillMaxSize()
                    .verticalScroll(state = scrollState)
                    .padding(horizontal = 16.dp),
                horizontalAlignment = Alignment.Start
            ) {
                if (isLoading) {
                    CircularProgressIndicator(modifier = Modifier.padding(top = 16.dp))
                } else if (displayedFlashcards.isEmpty()) {
                    Text("No flashcards available for ${subjectFilter ?: "all subjects"}.", modifier = Modifier.padding(top = 16.dp))
                } else {
                    displayedFlashcards.forEach { flashcard ->
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween,
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Column(
                                modifier = Modifier.weight(1f)
                            ) {
                                Text(
                                    "Question: ${flashcard.question}",
                                    color = Color.Black,
                                    fontSize = 18.sp,
                                    fontWeight = FontWeight.W500,
                                    modifier = Modifier.padding(top = 16.dp)
                                )
                                Text(
                                    "Answer: ${flashcard.answer}",
                                    color = Color.Black,
                                    fontSize = 18.sp,
                                    fontWeight = FontWeight.W500,
                                    modifier = Modifier.padding(bottom = 8.dp)
                                )
                                Text(
                                    "Subject: ${flashcard.subject}",
                                    color = Color.Gray,
                                    fontSize = 14.sp,
                                    modifier = Modifier.padding(bottom = 8.dp)
                                )
                            }
                            IconButton(onClick = { displayedFlashcards.remove(flashcard) }) {
                                Icon(Icons.Filled.Delete, "Remove from view")
                            }
                        }
                        Spacer(modifier = Modifier.height(8.dp))
                        Divider()
                    }
                }
                Spacer(modifier = Modifier.height(32.dp))
            }
        }
    )
}