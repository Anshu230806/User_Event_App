package com.example.eventsapp.feature.auth

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

@HiltViewModel
class LoginViewModel @Inject constructor(
 private val authRepository: AuthRepository
): ViewModel(){
   private val _uiState = MutableStateFlow(LoginUiState())
    val uiState: StateFlow<LoginUiState> = _uiState.asStateFlow()


    fun onEmailChange(email: String) {
        _uiState.value = _uiState.value.copy(
            email = email,
            errorMessage = null
        )
    }

    fun onPasswordChange(password: String) {
        _uiState.value = _uiState.value.copy(
            password = password,
            errorMessage = null
        )
    }

    fun consumeEffect() {
        _uiState.update { currentState ->
            currentState.copy(effect = null)
        }
    }

    fun login() {

        val currentState = _uiState.value

        if (currentState.email.isBlank()) {
            _uiState.value = currentState.copy(
                errorMessage = "Please enter your email"
            )
            return
        }

        if (currentState.password.isBlank()) {
            _uiState.value = currentState.copy(
                errorMessage = "Please enter your password"
            )
            return
        }

        viewModelScope.launch {

            _uiState.value = currentState.copy(
                isLoading = true,
                errorMessage = null
            )

            try {

                val result = authRepository.login(
                    email = currentState.email.trim(),
                    password = currentState.password
                )

                _uiState.value = _uiState.value.copy(
                    isLoading = false
                )

                result.onSuccess{
                    _uiState.update{
                        it.copy(
                            isLoading = false,
                            effect = LoginUiEffect.navigateToHome
                        )
                    }
                }.onFailure{ exception ->

                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        errorMessage = exception.message ?: "Invalid email or password"
                    )
                }
            } catch (e: Exception) {

                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    errorMessage = e.message ?: "Invalid email or password"
                )
            }
        }
    }
}