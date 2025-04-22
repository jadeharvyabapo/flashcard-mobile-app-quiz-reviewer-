package com.example.flashcard_quiz_app.model

data class Flashcard(
    val id: String = "",  // Provide a default value
    val question: String = "", // Provide a default value
    val answer: String = "",   // Provide a default value
    val subject: String = ""    // Add the subject field with a default value
)