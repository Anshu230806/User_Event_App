
package com.example.eventsapp.feature.auth

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await

@HiltViewModel
class ForgotPasswordViewModel @Inject constructor(
    private val firebaseAuth: FirebaseAuth
) : ViewModel() {

    private val _uiState = MutableStateFlow(ForgotPasswordUiState())
    val uiState: StateFlow<ForgotPasswordUiState> =
        _uiState.asStateFlow()

    fun onEmailChange(email: String) {
        _uiState.update {
            it.copy(
                email = email,
                errorMessage = null,
                successMessage = null
            )
        }
    }

    fun sendResetEmail() {
        val email = _uiState.value.email.trim()

        if (_uiState.value.isLoading) return

        if (email.isBlank()) {
            _uiState.update {
                it.copy(errorMessage = "Please enter your email address.")
            }
            return
        }

        if (!android.util.Patterns.EMAIL_ADDRESS
                .matcher(email)
                .matches()
        ) {
            _uiState.update {
                it.copy(errorMessage = "Please enter a valid email address.")
            }
            return
        }

        viewModelScope.launch {
            _uiState.update {
                it.copy(
                    isLoading = true,
                    errorMessage = null,
                    successMessage = null
                )
            }

            try {
                firebaseAuth
                    .sendPasswordResetEmail(email)
                    .await()

                _uiState.update {
                    it.copy(
                        isLoading = false,
                        successMessage =
                            "If an account exists for this email, check your inbox for a password reset link."
                    )
                }
            } catch (exception: CancellationException) {
                throw exception
            } catch (exception: Exception) {
                _uiState.update {
                    it.copy(
                        isLoading = false,
                        errorMessage =
                            "Unable to send the reset email. Check your connection and try again."
                    )
                }
            }
        }
    }
}
