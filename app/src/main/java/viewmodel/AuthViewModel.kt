package com.example.flashcard_quiz_app.viewmodel

import android.app.Application
import android.content.Intent
import android.util.Log
import androidx.activity.result.ActivityResultLauncher
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await

class AuthViewModel(application: Application) : AndroidViewModel(application) {

    private val auth: FirebaseAuth = Firebase.auth
    private val _errorMessage = MutableStateFlow<String?>(null)
    val errorMessage: StateFlow<String?> = _errorMessage

    private val _isLoggedIn = MutableStateFlow(auth.currentUser != null)
    val isLoggedIn: StateFlow<Boolean> = _isLoggedIn

    private lateinit var googleSignInClient: GoogleSignInClient

    init {
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken("772766294588-ickuccicjqiliml9bhkg4dkl4dplnds7.apps.googleusercontent.com") // Replace with your actual Web Client ID
            .requestEmail()
            .build()
        googleSignInClient = GoogleSignIn.getClient(application, gso)
    }

    fun startGoogleSignIn(launcher: ActivityResultLauncher<Intent>) {
        val signInIntent = googleSignInClient.signInIntent
        launcher.launch(signInIntent)
    }

    suspend fun signInWithGoogle(idToken: String, onSuccess: () -> Unit, onError: (String) -> Unit) {
        val credential = GoogleAuthProvider.getCredential(idToken, null)
        auth.signInWithCredential(credential)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    _isLoggedIn.value = true
                    _errorMessage.value = null
                    onSuccess()
                } else {
                    _errorMessage.value = task.exception?.message ?: "Google Sign-in failed"
                    onError(_errorMessage.value!!)
                    Log.e("AuthViewModel", "Google Sign-in failed: ${_errorMessage.value}")
                }
            }
            .await()
    }

    fun signUp(email: String, password: String, onSuccess: () -> Unit, onError: (String) -> Unit) {
        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    _errorMessage.value = null
                    _isLoggedIn.value = true
                    onSuccess()
                } else {
                    _errorMessage.value = task.exception?.message ?: "Signup failed"
                    onError(_errorMessage.value!!)
                    Log.e("AuthViewModel", "Signup failed: ${_errorMessage.value}")
                }
            }
    }

    fun login(email: String, password: String, onSuccess: () -> Unit, onError: (String) -> Unit) {
        auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    _errorMessage.value = null
                    _isLoggedIn.value = true
                    onSuccess()
                } else {
                    _errorMessage.value = task.exception?.message ?: "Login failed"
                    onError(_errorMessage.value!!)
                    Log.e("AuthViewModel", "Login failed: ${_errorMessage.value}")
                }
            }
    }

    fun getCurrentUser() = auth.currentUser

    fun signOut(onSuccess: () -> Unit) {
        auth.signOut()
        _isLoggedIn.value = false
        onSuccess()
    }
}