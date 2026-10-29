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



package com.example.eventsapp.feature.eventFilterScreen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.eventsapp.data.repository.repositories.EventsRepository
import com.example.eventsapp.data.repository.source.local.entity.EventEntity
import dagger.hilt.android.lifecycle.HiltViewModel
import java.util.Calendar
import java.util.TimeZone
import javax.inject.Inject
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

@HiltViewModel
class EventFilterViewModel @Inject constructor(
    private val eventsRepository: EventsRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(EventFilterUiState())
    val uiState: StateFlow<EventFilterUiState> = _uiState.asStateFlow()

    init {
        observeEvents()
        syncIfCacheIsEmpty()
    }

    // ---------- Loading ----------

    // Room is the single source of truth: every time the table changes we re-run the filters.
    private fun observeEvents() {
        viewModelScope.launch {
            eventsRepository.observeAllEvents()
                .catch { throwable ->
                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            errorMessage = throwable.message ?: "Unable to load events"
                        )
                    }
                }
                .collect { events ->
                    _uiState.update {
                        it.copy(allEvents = events, isLoading = false).withFilteredEvents()
                    }
                }
        }
    }

    // Home already syncs on start. This only covers opening the screen with an empty cache.
    private fun syncIfCacheIsEmpty() {
        viewModelScope.launch {
            val cached = eventsRepository.observeAllEvents().first()
            if (cached.isEmpty()) refreshEvents()
        }
    }

    fun refreshEvents() {
        viewModelScope.launch {
            try {
                eventsRepository.refreshEvents()
            } catch (e: CancellationException) {
                throw e
            } catch (e: Exception) {
                // If cached events exist we keep showing them and stay quiet.
                _uiState.update {
                    if (it.allEvents.isEmpty()) {
                        it.copy(errorMessage = e.message ?: "Unable to refresh events")
                    } else {
                        it
                    }
                }
            }
        }
    }

    // ---------- Form changes (nothing is filtered until Apply) ----------

    fun onFilterSelected(filter: EventSearchFilter) {
        _uiState.update {
            it.copy(
                selectedFilter = filter,
                // A date only makes sense with "Choose Date" or "Registration Closes Today".
                selectedDateMillis = if (
                    filter == EventSearchFilter.TODAY_EVENTS || filter == EventSearchFilter.ALL
                ) null else it.selectedDateMillis,
                isFilterFormExpanded = true,
                errorMessage = null
            )
        }
    }

    // pickerUtcMillis is exactly what DatePickerState.selectedDateMillis returns (UTC midnight).
    fun onDateSelected(pickerUtcMillis: Long?) {
        val localDayStart = pickerUtcMillis?.let(::pickerUtcMillisToLocalDayStart)

        _uiState.update {
            it.copy(
                selectedDateMillis = localDayStart,
                // Picking a date switches to "Choose Date", except when the user is
                // combining it with "Registration Closes Today".
                selectedFilter = if (
                    localDayStart != null &&
                    it.selectedFilter != EventSearchFilter.REGISTRATION_CLOSING_TODAY
                ) EventSearchFilter.CUSTOM else it.selectedFilter,
                isFilterFormExpanded = true,
                errorMessage = null
            )
        }
    }

    // Minutes since midnight, e.g. 3:30 PM = 930. null = any time.
    fun onTimeSelected(minutes: Int?) {
        _uiState.update {
            it.copy(selectedTimeMinutes = minutes, isFilterFormExpanded = true, errorMessage = null)
        }
    }

    // Duration in minutes. null = any duration.
    fun onDurationSelected(minutes: Int?) {
        _uiState.update {
            it.copy(selectedDurationMinutes = minutes, isFilterFormExpanded = true, errorMessage = null)
        }
    }

    // ---------- Buttons ----------

    fun onApplyFilters() {
        val current = _uiState.value

        if (current.selectedFilter == EventSearchFilter.CUSTOM && current.selectedDateMillis == null) {
            _uiState.update { it.copy(errorMessage = "Please select a date") }
            return
        }

        _uiState.update {
            it.copy(
                appliedFilter = it.selectedFilter,
                appliedDateMillis = it.selectedDateMillis,
                appliedTimeMinutes = it.selectedTimeMinutes,
                appliedDurationMinutes = it.selectedDurationMinutes,
                isFilterFormExpanded = false,
                errorMessage = null
            ).withFilteredEvents()
        }
    }

    // Back to the screen's starting point: Today's Events, no extra filters.
    fun onClearFilters() {
        _uiState.update {
            EventFilterUiState(allEvents = it.allEvents, isLoading = it.isLoading)
                .withFilteredEvents()
        }
    }

    // "Update Filters": reopen the form, starting from what is currently applied.
    fun onEditFilters() {
        _uiState.update {
            it.copy(
                selectedFilter = it.appliedFilter,
                selectedDateMillis = it.appliedDateMillis,
                selectedTimeMinutes = it.appliedTimeMinutes,
                selectedDurationMinutes = it.appliedDurationMinutes,
                isFilterFormExpanded = true,
                errorMessage = null
            )
        }
    }

    // The X on an applied chip: removes that one filter immediately.
    fun onRemoveAppliedFilter(type: AppliedFilterType) {
        _uiState.update { state ->
            val next = when (type) {
                AppliedFilterType.QUICK -> {
                    // Keep a chosen date working by falling back to "Choose Date".
                    val fallback = if (state.appliedDateMillis != null) {
                        EventSearchFilter.CUSTOM
                    } else {
                        EventSearchFilter.ALL
                    }
                    state.copy(appliedFilter = fallback, selectedFilter = fallback)
                }

                AppliedFilterType.DATE -> {
                    val filter = if (state.appliedFilter == EventSearchFilter.CUSTOM) {
                        EventSearchFilter.ALL
                    } else {
                        state.appliedFilter
                    }
                    state.copy(
                        appliedFilter = filter,
                        selectedFilter = filter,
                        appliedDateMillis = null,
                        selectedDateMillis = null
                    )
                }

                AppliedFilterType.TIME -> state.copy(
                    appliedTimeMinutes = null,
                    selectedTimeMinutes = null
                )

                AppliedFilterType.DURATION -> state.copy(
                    appliedDurationMinutes = null,
                    selectedDurationMinutes = null
                )
            }

            // Last chip removed -> nothing to show in the panel, so reopen the form.
            next.copy(isFilterFormExpanded = next.appliedChipTypes.isEmpty())
                .withFilteredEvents()
        }
    }

    // ---------- Navigation effect (same pattern as Login) ----------

    fun onEventClick(eventId: String) {
        _uiState.update {
            it.copy(effect = EventFilterUiEffect.NavigateToEventDetail(eventId))
        }
    }

    fun consumeEffect() {
        _uiState.update { it.copy(effect = null) }
    }

    // Recomputes `events` from allEvents + the APPLIED filters. Call after changing either.
    private fun EventFilterUiState.withFilteredEvents(): EventFilterUiState =
        copy(
            events = filterEvents(
                events = allEvents,
                filter = appliedFilter,
                dateMillis = appliedDateMillis,
                timeMinutes = appliedTimeMinutes,
                durationMinutes = appliedDurationMinutes
            )
        )
}

// ======================================================================
// Pure filtering logic. Top-level + internal so it can be unit tested
// without a ViewModel (pass `now` to freeze the clock).
// ======================================================================

private const val MILLIS_PER_MINUTE = 60_000L

internal fun filterEvents(
    events: List<EventEntity>,
    filter: EventSearchFilter,
    dateMillis: Long?,
    timeMinutes: Int?,
    durationMinutes: Int?,
    now: Long = System.currentTimeMillis()
): List<EventEntity> {
    val todayStart = startOfDay(now)
    val tomorrowStart = startOfNextDay(now)
    val dateStart = dateMillis?.let(::startOfDay)
    val dateEnd = dateMillis?.let(::startOfNextDay)

    return events.filter { event ->
        val start = event.eventStartTime

        val matchesDate = when (filter) {
            EventSearchFilter.TODAY_EVENTS ->
                start in todayStart until tomorrowStart

            EventSearchFilter.REGISTRATION_CLOSING_TODAY -> {
                val deadline = event.registrationDeadline
                val closesToday = deadline != null && deadline in todayStart until tomorrowStart
                // The screen also lets this tile be combined with a specific event date.
                val onChosenDate =
                    dateStart == null || dateEnd == null || start in dateStart until dateEnd
                closesToday && onChosenDate
            }

            EventSearchFilter.CUSTOM ->
                dateStart != null && dateEnd != null && start in dateStart until dateEnd

            EventSearchFilter.ALL -> true
        }

        // Exact start time (hour + minute), as shown in the design.
        val matchesTime = timeMinutes?.let { minutesOfDay(start) == it } ?: true

        // EventEntity.eventDuration is in MILLISECONDS (endsAt - startsAt in EventMapper).
        val matchesDuration = durationMinutes?.let {
            event.eventDuration / MILLIS_PER_MINUTE == it.toLong()
        } ?: true

        matchesDate && matchesTime && matchesDuration
    }.sortedBy { it.eventStartTime }
}

// ======================================================================
// Date helpers (device time zone). Also used by EventFilterScreen.
// ======================================================================

internal fun startOfDay(millis: Long): Long =
    Calendar.getInstance().apply {
        timeInMillis = millis
        set(Calendar.HOUR_OF_DAY, 0)
        set(Calendar.MINUTE, 0)
        set(Calendar.SECOND, 0)
        set(Calendar.MILLISECOND, 0)
    }.timeInMillis

// Uses Calendar.add so daylight-saving days (23/25 hours) are handled correctly.
internal fun startOfNextDay(millis: Long): Long =
    Calendar.getInstance().apply {
        timeInMillis = startOfDay(millis)
        add(Calendar.DAY_OF_MONTH, 1)
    }.timeInMillis

internal fun isToday(millis: Long): Boolean {
    val now = System.currentTimeMillis()
    return millis in startOfDay(now) until startOfNextDay(now)
}

internal fun minutesOfDay(millis: Long): Int =
    Calendar.getInstance().apply { timeInMillis = millis }.let {
        it.get(Calendar.HOUR_OF_DAY) * 60 + it.get(Calendar.MINUTE)
    }

// Material3's DatePicker returns the selected day as midnight UTC. In a time zone behind UTC
// that is the previous evening locally, so we rebuild "the same calendar day" locally.
internal fun pickerUtcMillisToLocalDayStart(utcMillis: Long): Long {
    val utc = Calendar.getInstance(TimeZone.getTimeZone("UTC")).apply { timeInMillis = utcMillis }
    return Calendar.getInstance().apply {
        clear()
        set(utc.get(Calendar.YEAR), utc.get(Calendar.MONTH), utc.get(Calendar.DAY_OF_MONTH), 0, 0, 0)
    }.timeInMillis
}

// Reverse of the function above: used as the DatePicker's initial selection.
internal fun localDayStartToPickerUtcMillis(localMillis: Long): Long {
    val local = Calendar.getInstance().apply { timeInMillis = localMillis }
    return Calendar.getInstance(TimeZone.getTimeZone("UTC")).apply {
        clear()
        set(local.get(Calendar.YEAR), local.get(Calendar.MONTH), local.get(Calendar.DAY_OF_MONTH), 0, 0, 0)
    }.timeInMillis
}