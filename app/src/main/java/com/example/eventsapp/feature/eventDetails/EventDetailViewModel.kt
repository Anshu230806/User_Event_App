package com.example.eventsapp.feature.eventdetail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.eventsapp.data.repository.repositories.EventsRepository
import com.example.eventsapp.data.repository.repositories.RegistrationRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

@HiltViewModel
class EventDetailViewModel @Inject constructor(
    private val eventsRepository: EventsRepository,
    private val registrationRepository: RegistrationRepository
) : ViewModel() {

    private val _uiState =
        MutableStateFlow(EventDetailUiState(isLoading = true))

    val uiState: StateFlow<EventDetailUiState> =
        _uiState.asStateFlow()

    fun loadEvent(eventId: String) {
        viewModelScope.launch {
            _uiState.value = EventDetailUiState(isLoading = true)

            try {
                val event = eventsRepository.getEvent(eventId)

                _uiState.value = if (event != null) {
                    EventDetailUiState(
                        event = event,
                        isLoading = false
                    )
                } else {
                    EventDetailUiState(
                        isLoading = false,
                        errorMessage = "Event not found"
                    )
                }

            } catch (e: Exception) {
                _uiState.value = EventDetailUiState(
                    isLoading = false,
                    errorMessage = e.message ?: "Unable to load event"
                )
            }
        }
    }

    fun registerForEvent() {
        val eventId = _uiState.value.event?.eventId ?: return

        if (_uiState.value.isRegistering ||
            _uiState.value.isRegistered
        ) return

        viewModelScope.launch {

            _uiState.value = _uiState.value.copy(
                isRegistering = true,
                registrationMessage = null,
                registrationError = null
            )

            val result = registrationRepository.registerForEvent(eventId)

            result.onSuccess { message ->
                _uiState.value = _uiState.value.copy(
                    isRegistering = false,
                    isRegistered = true,
                    registrationMessage = message
                )
            }

            result.onFailure { error ->
                _uiState.value = _uiState.value.copy(
                    isRegistering = false,
                    registrationError = error.message
                        ?: "Registration failed. Please try again."
                )
            }
        }
    }
}