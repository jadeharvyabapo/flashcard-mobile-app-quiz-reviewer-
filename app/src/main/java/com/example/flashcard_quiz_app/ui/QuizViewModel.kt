package com.example.flashcard_quiz_app.ui

import android.service.controls.ControlsProviderService.TAG
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.flashcard_quiz_app.data.Flashcard
import com.google.firebase.firestore.FirebaseFirestore

class FlashcardViewModel : ViewModel() {
    private val _flashcards = MutableLiveData<List<Flashcard>>()
    val flashcards: LiveData<List<Flashcard>> get() = _flashcards

    private val db = FirebaseFirestore.getInstance()

    init {
        fetchFlashcards()
    }

    private fun fetchFlashcards() {
        db.collection("flashcards")
            .get()
            .addOnSuccessListener { documents ->
                val flashcards = documents.mapNotNull { it.toObject(Flashcard::class.java) }
                _flashcards.value = flashcards
                Log.d("FlashcardViewModel", "Fetched ${flashcards.size} flashcards from Firebase")
            }
            .addOnFailureListener { exception ->
                Log.w(TAG, "Error getting documents: ", exception)
            }
    }

    fun addFlashcard(question: String, answer: String) {
        val flashcard = Flashcard(question, answer)
        db.collection("flashcards")
            .add(flashcard)
            .addOnSuccessListener { documentReference ->
                Log.d(TAG, "DocumentSnapshot added with ID: ${documentReference.id}")
            }
            .addOnFailureListener { e ->
                Log.w(TAG, "Error adding document", e)
            }
    }
    fun checkAnswer(answer: String, currentQuestionIndex:Int): Boolean {
        val currentQuestion = _flashcards.value?.getOrNull(currentQuestionIndex)
        return currentQuestion?.answer == answer
    }

}