//package com.example.eventsapp.feature.eventFilterScreen
//
//import androidx.lifecycle.ViewModel
//import androidx.lifecycle.viewModelScope
//import com.example.eventsapp.data.repository.source.local.entity.EventEntity
//import dagger.hilt.android.lifecycle.HiltViewModel
//import java.util.Calendar
//import javax.inject.Inject
//import kotlinx.coroutines.flow.MutableStateFlow
//import kotlinx.coroutines.flow.StateFlow
//import kotlinx.coroutines.flow.asStateFlow
//import kotlinx.coroutines.flow.catch
//import kotlinx.coroutines.flow.update
//import kotlinx.coroutines.launch
//import com.example.eventsapp.data.repository.repositories.EventsRepository
//
//@HiltViewModel
//class EventsFilterViewModel @Inject constructor(
//    private val repository: EventsRepository
//) : ViewModel() {
//
//    private val _uiState = MutableStateFlow(
//        EventsFilterUiState(isLoading = true)
//    )
//    val uiState: StateFlow<EventsFilterUiState> =
//        _uiState.asStateFlow()
//
//    init {
//        viewModelScope.launch {
//            repository.observeEvents()
//                .catch { error ->
//                    _uiState.update {
//                        it.copy(
//                            isLoading = false,
//                            errorMessage = error.message
//                                ?: "Unable to load events"
//                        )
//                    }
//                }
//                .collect { events ->
//                    _uiState.update {
//                        it.copy(allEvents = events, isLoading = false)
//                    }
//                    applyFilters()
//                }
//        }
//    }
//
//    fun selectFilter(filter: EventSearchFilter) {
//        _uiState.update {
//            it.copy(selectedFilter = filter, errorMessage = null)
//        }
//        applyFilters()
//    }
//
//    fun selectDate(dateMillis: Long?) {
//        _uiState.update {
//            it.copy(
//                selectedDateMillis = dateMillis,
//                selectedFilter = EventSearchFilter.CUSTOM
//            )
//        }
//        applyFilters()
//    }
//
//    // Minutes since midnight, e.g. 14:30 = 870.
//    fun selectTime(minutes: Int?) {
//        _uiState.update { it.copy(selectedTimeMinutes = minutes) }
//        applyFilters()
//    }
//
//    // Duration in minutes; null means no duration filter.
//    fun selectDuration(minutes: Int?) {
//        _uiState.update { it.copy(durationMinutes = minutes) }
//        applyFilters()
//    }
//
//    fun clearOptionalFilters() {
//        _uiState.update {
//            it.copy(
//                selectedTimeMinutes = null,
//                durationMinutes = null
//            )
//        }
//        applyFilters()
//    }
//
//    private fun applyFilters() {
//        val state = _uiState.value
//        val now = Calendar.getInstance()
//
//        fun dayStart(millis: Long): Long =
//            Calendar.getInstance().apply {
//                timeInMillis = millis
//                set(Calendar.HOUR_OF_DAY, 0)
//                set(Calendar.MINUTE, 0)
//                set(Calendar.SECOND, 0)
//                set(Calendar.MILLISECOND, 0)
//            }.timeInMillis
//
//        val todayStart = dayStart(now.timeInMillis)
//        val tomorrowStart = Calendar.getInstance().apply {
//            timeInMillis = todayStart
//            add(Calendar.DAY_OF_MONTH, 1)
//        }.timeInMillis
//
//        val dateStart = state.selectedDateMillis?.let(::dayStart)
//        val dateEnd = dateStart?.let { start ->
//            Calendar.getInstance().apply {
//                timeInMillis = start
//                add(Calendar.DAY_OF_MONTH, 1)
//            }.timeInMillis
//        }
//
//        val filtered = state.allEvents.filter { event ->
//            val dateMatches = when (state.selectedFilter) {
//                EventSearchFilter.ALL -> true
//
//                EventSearchFilter.TODAY_EVENTS ->
//                    event.eventStartTime >= todayStart &&
//                            event.eventStartTime < tomorrowStart
//
//                EventSearchFilter.REGISTRATION_CLOSING_TODAY ->
//                    false // Requires a registration-deadline field.
//
//                EventSearchFilter.CUSTOM ->
//                    dateStart != null && dateEnd != null &&
//                            event.eventStartTime >= dateStart &&
//                            event.eventStartTime < dateEnd
//            }
//
//            val timeMatches = state.selectedTimeMinutes?.let { minutes ->
//                val eventCalendar = Calendar.getInstance().apply {
//                    timeInMillis = event.eventStartTime
//                }
//                eventCalendar.get(Calendar.HOUR_OF_DAY) * 60 +
//                        eventCalendar.get(Calendar.MINUTE) == minutes
//            } ?: true
//
//            val durationMatches = state.durationMinutes?.let { minutes ->
//                event.eventDuration == minutes.toLong()
//            } ?: true
//
//            dateMatches && timeMatches && durationMatches
//        }.sortedBy { it.eventStartTime }
//
//        _uiState.update { it.copy(events = filtered) }
//    }
//}
//
