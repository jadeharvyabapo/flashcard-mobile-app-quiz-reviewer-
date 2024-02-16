package com.example.flashcard_quiz_app.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.flashcard_quiz_app.R

@Composable
fun ScoreScreen(score: String?, navController: NavController) {
    Column (
       // verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.background(Color.White)
    ){
        val imageModifier = Modifier
            .size(300.dp)
            .background(Color.White)
           .padding(top = 70.dp)
        Image(
            painter = painterResource(id = R.drawable.result),
            contentDescription = null,
            contentScale = ContentScale.Crop,
            modifier = imageModifier
        )
        Text(
            text = "Your score: $score",
            modifier = Modifier.padding(top = 15.dp),
            color = Color.Black,
            fontSize = 23.sp,
            fontWeight = FontWeight.W600
            )

        FilledTonalButton(onClick = { navController.navigate("create_flashcard") },
            modifier = Modifier.padding(start = 16.dp, top = 45.dp),
            colors = ButtonDefaults.buttonColors(Color.hsl(15f, 0.9f, 0.9f))) {
            Text("Back to 1st screen")
        }
    }
}
