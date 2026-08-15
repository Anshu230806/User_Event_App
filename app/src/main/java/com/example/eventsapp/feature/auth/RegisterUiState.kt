package com.example.eventsapp.feature.auth


sealed interface RegisterUiEffect{
    data object navigateToHome: RegisterUiEffect
   // data class showErrorMessage(val message: String? = null): LoginUiEffect
}

// every state must be intiliazed with initial values otherwise it create error during viewModel getting state values
data class RegisterUiState(
    val email:String="",
    val password:String="",
    val confirmPass:String="",
    val isLoading:Boolean= false,
    val errorMessage:String?= null,
    val effect: RegisterUiEffect?=null
)
