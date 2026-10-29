package com.example.eventsapp.feature.registration

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.eventsapp.data.remote.RegistrationApi
import com.example.eventsapp.data.remote.dto.RegistrationCreateRequest
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

@HiltViewModel
class RegistrationViewModel @Inject constructor(
    private val registrationApi: RegistrationApi,
    private val firebaseAuth: FirebaseAuth
) : ViewModel() {
    private val _uiState = MutableStateFlow(RegistrationUiState())
    val uiState: StateFlow<RegistrationUiState> = _uiState.asStateFlow()

    init {
        val user = firebaseAuth.currentUser
        _uiState.value = _uiState.value.copy(
            email = user?.email.orEmpty(),
            fullName = user?.displayName.orEmpty()
        )
    }

    fun updateFullName(value: String) { _uiState.value = _uiState.value.copy(fullName = value, errorMessage = null) }
    fun updateEmail(value: String) { _uiState.value = _uiState.value.copy(email = value, errorMessage = null) }
    fun updatePhone(value: String) { _uiState.value = _uiState.value.copy(phone = value, errorMessage = null) }
    fun updateCollege(value: String) { _uiState.value = _uiState.value.copy(collegeName = value, errorMessage = null) }
    fun updateCourse(value: String) { _uiState.value = _uiState.value.copy(course = value, errorMessage = null) }
    fun updateDetails(value: String) { _uiState.value = _uiState.value.copy(additionalDetails = value, errorMessage = null) }

    fun submit(eventId: String) {
        val state = _uiState.value
        if (state.fullName.isBlank() || state.email.isBlank()) {
            _uiState.value = state.copy(errorMessage = "Full name and email are required")
            return
        }
        viewModelScope.launch {
            _uiState.value = state.copy(isSubmitting = true, errorMessage = null, successMessage = null)
            try {
                val response = registrationApi.registerForEvent(
                    eventId,
                    RegistrationCreateRequest(
                        fullName = state.fullName.trim(),
                        email = state.email.trim(),
                        phone = state.phone.trim().ifBlank { null },
                        collegeName = state.collegeName.trim().ifBlank { null },
                        course = state.course.trim().ifBlank { null },
                        additionalDetails = state.additionalDetails.trim().ifBlank { null }
                    )
                )
                _uiState.value = _uiState.value.copy(
                    isSubmitting = false,
                    successMessage = if (response.success) response.message ?: "Registration submitted" else null,
                    errorMessage = if (response.success) null else response.message ?: "Registration failed"
                )
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    isSubmitting = false,
                    errorMessage = e.message ?: "Unable to register. Please try again."
                )
            }
        }
    }
}
