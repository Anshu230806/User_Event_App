package com.example.eventsapp.feature.auth

import android.R.attr.password
import android.util.Log.e
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.compose.viewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RegisterViewModel @Inject constructor(
    private val authRepository: AuthRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(RegisterUiState())
    val uiState: StateFlow<RegisterUiState> = _uiState.asStateFlow()

    private val _uiEffect = MutableSharedFlow<RegisterUiEffect>()
    val uiEffect = _uiEffect.asSharedFlow()

    fun onEmailChange(email: String){
        _uiState.value = _uiState.value.copy(
            email = email,
            errorMessage = null
        )
    }

    fun onPasswordChange(password: String){
        _uiState.value= _uiState.value.copy(
            password = password,
            errorMessage = null
        )
    }

    fun consumeEffect() {
        _uiState.update { currentState ->
            currentState.copy(effect = null)
        }
    }

    fun onConfirmPassChange(confirmPass: String){
        _uiState.value = _uiState.value.copy(
            confirmPass = confirmPass,
            errorMessage = null
        )
    }


    fun signUp(){

        val current = _uiState.value

        if(current.email.isBlank()){
            _uiState.value = current.copy(
                errorMessage = "enter email"
            )
            return
        }

        if(current.password.length < 6){
            _uiState.value = current.copy(
                errorMessage = "password should be at least 6 characters"
            )
            return
        }

        viewModelScope.launch{

            _uiState.value= current.copy(
                isLoading = true,
                errorMessage = null
            )

            try{
               val result = authRepository.signup(
                    email = current.email.trim(),
                     password = current.password
                )

                result.onSuccess{
                    _uiState.update{
                        it.copy(
                            isLoading = false,
                            effect = RegisterUiEffect.navigateToHome
                        )
                    }
                }.onFailure { exception ->
                    _uiState.value= current.copy(
                        isLoading = false,
                        errorMessage = exception.message ?: "signup failed"
                    )
                }
            }catch(e: Exception){
                _uiState.value= current.copy(
                    isLoading = false,
                    errorMessage = e.message ?: "signup failed"
                )
            }
        }
    }
}