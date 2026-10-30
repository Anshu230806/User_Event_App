package com.example.eventsapp.feature.profile

data class ProfileUiState(
    val name: String = "",
    val email: String = "",
    val isLoading: Boolean = false,
    val errorMessage: String? = null,
    val role: String = "Student"
)
