package com.example.eventsapp.feature.auth


// screen p user enter krega email and password to keep those states and tracking on chenging of sate
sealed interface LoginUiEffect{
    data object navigateToHome: LoginUiEffect
 //   data class showErrorMessage(val message: String): LoginUiEffect

}

data class LoginUiState(
    val email: String ="",
    val password: String ="",
    val isLoading: Boolean= false,
    val errorMessage: String?= null,
    val effect:LoginUiEffect?= null
)
