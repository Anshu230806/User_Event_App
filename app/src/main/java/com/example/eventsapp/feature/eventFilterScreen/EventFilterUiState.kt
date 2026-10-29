//package com.example.eventsapp.feature.eventFilterScreen
//
//import com.example.eventsapp.data.repository.source.local.entity.EventEntity
//
//enum class EventSearchFilter(val label: String) {
//    TODAY_EVENTS("Today's Events"),
//    REGISTRATION_CLOSING_TODAY("Registration Closes Today"),
//    CUSTOM("Choose Date"),
//    ALL("All Events")
//}
//
//data class EventsFilterUiState(
//    val allEvents: List<EventEntity> = emptyList(),
//    val events: List<EventEntity> = emptyList(),
//    val selectedFilter: EventSearchFilter = EventSearchFilter.TODAY_EVENTS,
//    val selectedDateMillis: Long? = null,
//    val selectedTimeMinutes: Int? = null,
//    val durationMinutes: Int? = null,
//    val isLoading: Boolean = false,
//    val errorMessage: String? = null
//)


package com.example.eventsapp.feature.eventFilterScreen

import com.example.eventsapp.data.repository.source.local.entity.EventEntity

// The four quick tiles at the top of the screen. Only one can be selected at a time.
// chipLabel is the text shown in the "Applied Filters" panel (null = no chip for that tile).
enum class EventSearchFilter(val label: String, val chipLabel: String?) {
    TODAY_EVENTS("Today's Events", "Today's events"),
    REGISTRATION_CLOSING_TODAY("Registration Closes Today", "Registration closes today"),
    CUSTOM("Choose Date", null),   // represented by the "Date: ..." chip instead
    ALL("All Events", null)        // "no date filter", so no chip
}

// Which chip in the "Applied Filters" panel the user is removing.
enum class AppliedFilterType { QUICK, DATE, TIME, DURATION }

// Options shown in the duration dropdown (minutes).
val EVENT_DURATION_OPTIONS_MINUTES = listOf(30, 60, 90, 120, 180, 240)

// One-time things the screen must react to (same idea as LoginUiEffect).
sealed interface EventFilterUiEffect {
    data class NavigateToEventDetail(val eventId: String) : EventFilterUiEffect
}

data class EventFilterUiState(

    // ---------- Data ----------
    // Everything currently stored in Room.
    val allEvents: List<EventEntity> = emptyList(),
    // allEvents after the APPLIED filters have been run on it. This is what the list shows.
    val events: List<EventEntity> = emptyList(),

    // ---------- Form (what the user is editing, not applied yet) ----------
    val selectedFilter: EventSearchFilter = EventSearchFilter.TODAY_EVENTS,
    // Start of the chosen day in the device's time zone (NOT the raw DatePicker value).
    val selectedDateMillis: Long? = null,
    // Minutes since midnight, e.g. 3:30 PM = 15 * 60 + 30 = 930.
    val selectedTimeMinutes: Int? = null,
    val selectedDurationMinutes: Int? = null,

    // ---------- Applied (what the list is actually filtered by) ----------
    val appliedFilter: EventSearchFilter = EventSearchFilter.TODAY_EVENTS,
    val appliedDateMillis: Long? = null,
    val appliedTimeMinutes: Int? = null,
    val appliedDurationMinutes: Int? = null,

    // true  -> show the form (Custom Date / Start Time / Duration / Apply / Clear)
    // false -> show the "Applied Filters" chips panel with "Update Filters"
    val isFilterFormExpanded: Boolean = true,

    val isLoading: Boolean = true,
    val errorMessage: String? = null,
    val effect: EventFilterUiEffect? = null
) {
    // Which chips the "Applied Filters" panel should show, in display order.
    val appliedChipTypes: List<AppliedFilterType>
        get() = buildList {
            if (appliedFilter.chipLabel != null) add(AppliedFilterType.QUICK)
            if (appliedDateMillis != null) add(AppliedFilterType.DATE)
            if (appliedTimeMinutes != null) add(AppliedFilterType.TIME)
            if (appliedDurationMinutes != null) add(AppliedFilterType.DURATION)
        }
}