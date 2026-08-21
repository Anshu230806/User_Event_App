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

/**
 * ViewModel for the organizer's Event screen.
 *
 * The ViewModel communicates with the repository instead of
 * directly accessing Firebase or Room.
 */
@HiltViewModel
class EventScreenViewModel @Inject constructor(
    private val eventRepository: EventsRepository
) : ViewModel() {

    /**
     * Observes Room data and converts it into UI state.
     */
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

            // Converts exceptions into an error state instead of crashing the Flow.
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

                // Keeps the upstream Flow active briefly after the UI stops observing it.
                started = SharingStarted.WhileSubscribed(5_000),

                // Initial state before Room emits the first event list.
                initialValue = HomeUiState()
            )


    init {

        // Starts the initial Room-first/Firebase-fallback operation.
        viewModelScope.launch {
            try {
                // Checks Room and fetches Firebase only when Room is empty.
                eventRepository.initializeEvents()
            } catch (e: Exception) {
                // Log or handle error - this prevents crash if Firebase or Room fails
                e.printStackTrace()
            }
        }
    }


    /**
     * Manually synchronizes Firebase data with Room.
     */
    fun refreshEvents() {

        // Runs the synchronization inside the ViewModel lifecycle.
        viewModelScope.launch {

            // Fetches Firebase data, maps it, and stores it in Room.
            eventRepository.refreshEvents()
        }
    }
}