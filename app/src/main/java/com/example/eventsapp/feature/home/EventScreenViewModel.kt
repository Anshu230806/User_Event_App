package com.example.eventsapp.feature.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.eventsapp.data.repository.repositories.EventsRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

@HiltViewModel
class EventScreenViewModel @Inject constructor(
    private val eventRepository: EventsRepository
) : ViewModel() {

     // Observes Room data and converts it into UI state.
    val uiState: StateFlow<HomeUiState> =
        eventRepository
            .observeAllEvents()
            // Converts the Room event list into the state required by the Compose UI.
            .map { events ->
                HomeUiState(
                    eventsList = events,
                    isLoading = false,
                    errorMessage = null
                )
            }
            .catch { throwable ->
                emit(
                    HomeUiState(
                        eventsList = emptyList(),
                        isLoading = false,
                        errorMessage = throwable.message
                            ?: "Unable to load events"
                    )
                )
            }
            // Converts the Flow into a lifecycle-aware StateFlow for the UI.
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(5_000),
                initialValue = HomeUiState()
            )

    init {

        viewModelScope.launch {
            try {
                eventRepository.initializeEvents()
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }


    fun refreshEvents() {
        viewModelScope.launch {
            eventRepository.refreshEvents()
        }
    }
}