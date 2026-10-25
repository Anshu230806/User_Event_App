package com.example.eventsapp.feature.eventdetail

import androidx.lifecycle.ViewModel
import com.example.eventsapp.data.repository.source.local.entity.EventEntity
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

@HiltViewModel
class EventDetailViewModel @Inject constructor() : ViewModel() {
    private val _uiState = MutableStateFlow(EventDetailUiState())
    val uiState: StateFlow<EventDetailUiState> = _uiState.asStateFlow()

    // Call when navigating to this screen with the selected event from the event list.
    fun setEvent(event: EventEntity) {
        _uiState.value = EventDetailUiState(event = event)
    }

    fun showError(message: String) {
        _uiState.value = _uiState.value.copy(isLoading = false, errorMessage = message)
    }
}
