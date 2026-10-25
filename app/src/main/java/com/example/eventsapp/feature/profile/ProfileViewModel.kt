package com.example.eventsapp.feature.profile

import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val firebaseAuth: FirebaseAuth
) : ViewModel() {
    private val _uiState = MutableStateFlow(ProfileUiState())
    val uiState: StateFlow<ProfileUiState> = _uiState.asStateFlow()

    init { loadProfile() }

    fun loadProfile() {
        val user = firebaseAuth.currentUser
        _uiState.value = ProfileUiState(
            name = user?.displayName.orEmpty(),
            email = user?.email.orEmpty(),
            errorMessage = if (user == null) "No signed-in user found" else null
        )
    }

    fun signOut(onSignedOut: () -> Unit) {
        firebaseAuth.signOut()
        _uiState.value = ProfileUiState()
        onSignedOut()
    }
}
