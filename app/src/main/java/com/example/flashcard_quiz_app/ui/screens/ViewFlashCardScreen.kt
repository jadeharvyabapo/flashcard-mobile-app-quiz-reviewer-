package com.example.flashcard_quiz_app.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.flashcard_quiz_app.R
import com.example.flashcard_quiz_app.ui.FlashcardViewModel

@Composable
fun ViewFlashcardsScreen(viewModel: FlashcardViewModel, navController: NavController) {
    val flashcards = viewModel.flashcards.observeAsState(initial = emptyList())
    val scrollState = rememberScrollState()
    Column(
        //  verticalArrangement = Arrangement.Center,
        //horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .background(Color.White)
            .verticalScroll(state = scrollState)
    ) {
        val imageModifier = Modifier
            .size(280.dp)
            .padding(start = 59.dp, top = 48.dp)
        Image(
            painter = painterResource(id = R.drawable.cloud),
            contentDescription = null,
            // modifier = Modifier.padding(top = 73.dp),
            contentScale = ContentScale.Crop,
            modifier = imageModifier
        )
        if (flashcards.value.isEmpty()) {
            FilledTonalButton(
                onClick = { navController.navigate("create_flashcard") },
                shape = RoundedCornerShape(30),
                modifier = Modifier
                    .padding(start = 65.dp, top = 350.dp),
                    //.size(92.dp),
                colors = ButtonDefaults.buttonColors(Color.hsl(15f, 0.9f, 0.9f))
            ) {
                Text("Oops!! No flashcards available. Click back")
            }
        } else {
            flashcards.value.forEach { flashcard ->
                Text(
                    "Question: ${flashcard.question}",
                    color = Color.Black,
                    fontSize = 23.sp,
                    fontWeight = FontWeight.W500,
                    modifier = Modifier.padding(top = 63.dp)
                )
                Text(
                    "Answer: ${flashcard.answer}",
                    color = Color.Black,
                    fontSize = 23.sp,
                    fontWeight = FontWeight.W500,
                    // modifier = Modifier.padding(23.dp)
                )
            }

            FilledTonalButton(
                onClick = { navController.navigate("create_flashcard") },
                shape = RoundedCornerShape(30),
                modifier = Modifier
                    .padding(start = 160.dp, top = 50.dp),
                   // .size(92.dp),
                colors = ButtonDefaults.buttonColors(Color.hsl(15f, 0.9f, 0.9f))
            ) {
                Text("Back")
            }
        }
    }
}
