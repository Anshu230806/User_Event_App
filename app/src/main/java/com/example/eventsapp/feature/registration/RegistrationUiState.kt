package com.example.eventsapp.feature.registration

data class RegistrationUiState(
    val fullName: String = "",
    val email: String = "",
    val phone: String = "",
    val collegeName: String = "",
    val course: String = "",
    val additionalDetails: String = "",
    val isSubmitting: Boolean = false,
    val successMessage: String? = null,
    val errorMessage: String? = null
)
