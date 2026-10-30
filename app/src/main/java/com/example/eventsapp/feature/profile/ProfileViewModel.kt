package com.example.eventsapp.feature.profile

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.eventsapp.data.repository.source.local.dao.UserDao
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val firebaseAuth: FirebaseAuth,
    private val userDao: UserDao
) : ViewModel() {
    private val _uiState = MutableStateFlow(ProfileUiState())
    val uiState: StateFlow<ProfileUiState> = _uiState.asStateFlow()

    init {
        loadProfile()
    }

    fun loadProfile() {
        val user = firebaseAuth.currentUser
        val firebaseEmail = user?.email.orEmpty()
        val firebaseDisplayName = user?.displayName.orEmpty()

        viewModelScope.launch {
            val cachedUser = userDao.getCurrentUser()

            val rawName = cachedUser?.fullName?.ifBlank { null }
                ?: firebaseDisplayName.ifBlank { null }
                ?: if (firebaseEmail.isNotBlank()) {
                    firebaseEmail.substringBefore('@')
                        .replace('.', ' ')
                        .replace('-', ' ')
                        .split(" ")
                        .filter { it.isNotBlank() }
                        .joinToString(" ") { word ->
                            word.replaceFirstChar { if (it.isLowerCase()) it.titlecase() else it.toString() }
                        }
                } else ""

            val finalEmail = cachedUser?.email?.ifBlank { null }
                ?: firebaseEmail

            val finalRole = cachedUser?.role?.ifBlank { null }
                ?: "Student"

            _uiState.update {
                it.copy(
                    name = rawName,
                    email = finalEmail,
                    role = finalRole,
                    errorMessage = if (user == null && cachedUser == null) "No signed-in user found" else null
                )
            }
        }
    }

    fun signOut(onSignedOut: () -> Unit) {
        viewModelScope.launch {
            val user = firebaseAuth.currentUser
            if (user != null) {
                userDao.deleteProfile(user.uid)
            }
            firebaseAuth.signOut()
            _uiState.value = ProfileUiState()
            onSignedOut()
        }
    }
}
