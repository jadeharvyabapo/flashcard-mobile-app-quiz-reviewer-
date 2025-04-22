// FlashcardViewModel.kt
package com.example.flashcard_quiz_app.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.flashcard_quiz_app.model.Flashcard
import com.google.firebase.firestore.FirebaseFirestore

class FlashcardViewModel : ViewModel() {
    private val _flashcards = MutableLiveData<List<Flashcard>>()
    val flashcards: LiveData<List<Flashcard>> get() = _flashcards

    private val _isLoading = MutableLiveData<Boolean>(false)
    val isLoading: LiveData<Boolean> get() = _isLoading

    private val _subjects = MutableLiveData<List<String>>()
    val subjects: LiveData<List<String>> get() = _subjects

    private val db = FirebaseFirestore.getInstance()
    private val subjectsCollection = db.collection("subjects")

    companion object {
        private const val TAG = "FlashcardViewModel"
    }

    init {
        fetchFlashcards()
        fetchUniqueSubjects()
    }

    fun fetchFlashcards(subject: String? = null) {
        _isLoading.value = true
        val query = if (subject.isNullOrBlank()) {
            db.collection("flashcards")
        } else {
            db.collection("flashcards").whereEqualTo("subject", subject)
        }

        query.get()
            .addOnSuccessListener { documents ->
                val flashcardList = documents.mapNotNull { it.toObject(Flashcard::class.java) }
                _flashcards.value = flashcardList
                _isLoading.value = false
                Log.d(TAG, "Fetched ${flashcardList.size} flashcards (subject: $subject) from Firebase")
            }
            .addOnFailureListener { exception ->
                Log.w(TAG, "Error getting documents: ", exception)
                _isLoading.value = false
            }
    }

    fun fetchUniqueSubjects() {
        _isLoading.value = true
        subjectsCollection.get()
            .addOnSuccessListener { documents ->
                val subjectList = documents.mapNotNull { it.getString("name") }
                    .distinct()
                    .sorted()
                _subjects.value = subjectList
                _isLoading.value = false
                Log.d(TAG, "Fetched ${subjectList.size} unique subjects from Firebase")
            }
            .addOnFailureListener { exception ->
                Log.w(TAG, "Error getting subjects: ", exception)
                _isLoading.value = false
            }
    }

    fun addFlashcard(question: String, answer: String, subject: String) {
        val flashcard = Flashcard(question = question, answer = answer, subject = subject)
        db.collection("flashcards")
            .add(flashcard)
            .addOnSuccessListener { documentReference ->
                Log.d(TAG, "Flashcard added with ID: ${documentReference.id}")
                fetchFlashcards()
            }
            .addOnFailureListener { e ->
                Log.w(TAG, "Error adding flashcard", e)
            }
    }

    fun addSubject(subject: String) {
        subjectsCollection.whereEqualTo("name", subject)
            .get()
            .addOnSuccessListener { documents ->
                if (documents.isEmpty) {
                    val subjectMap = hashMapOf("name" to subject)
                    subjectsCollection.add(subjectMap)
                        .addOnSuccessListener { documentReference ->
                            Log.d(TAG, "Subject added with ID: ${documentReference.id}")
                            fetchUniqueSubjects()
                        }
                        .addOnFailureListener { e ->
                            Log.w(TAG, "Error adding subject", e)
                        }
                } else {
                    Log.d(TAG, "Subject '$subject' already exists.")
                }
            }
            .addOnFailureListener { e ->
                Log.w(TAG, "Error checking for existing subject", e)
            }
    }

    fun removeSubject(subject: String) {
        subjectsCollection.whereEqualTo("name", subject)
            .get()
            .addOnSuccessListener { documents ->
                if (!documents.isEmpty) {
                    val documentId = documents.first().id
                    subjectsCollection.document(documentId)
                        .delete()
                        .addOnSuccessListener {
                            Log.d(TAG, "Subject '$subject' removed")
                            fetchUniqueSubjects()
                            fetchFlashcards()
                        }
                        .addOnFailureListener { e ->
                            Log.w(TAG, "Error removing subject '$subject'", e)
                        }
                } else {
                    Log.d(TAG, "Subject '$subject' not found.")
                }
            }
            .addOnFailureListener { e ->
                Log.w(TAG, "Error querying for subject to remove", e)
            }
    }

    fun checkAnswer(userAnswer: String, index: Int): Boolean {
        val correctAnswer = _flashcards.value?.get(index)?.answer ?: ""
        return userAnswer.trim().equals(correctAnswer.trim(), ignoreCase = true)
    }


}