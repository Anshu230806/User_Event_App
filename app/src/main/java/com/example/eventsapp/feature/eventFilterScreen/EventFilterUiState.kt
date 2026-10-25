package com.example.eventsapp.feature.eventFilterScreen

import com.example.eventsapp.data.repository.source.local.entity.EventEntity

enum class EventSearchFilter(val label: String) {
    TODAY_EVENTS("Today's Events"),
    REGISTRATION_CLOSING_TODAY("Registration Closes Today"),
    CUSTOM("Choose Date"),
    ALL("All Events")
}

data class EventsFilterUiState(
    val allEvents: List<EventEntity> = emptyList(),
    val events: List<EventEntity> = emptyList(),
    val selectedFilter: EventSearchFilter = EventSearchFilter.TODAY_EVENTS,
    val selectedDateMillis: Long? = null,
    val selectedTimeMinutes: Int? = null,
    val durationMinutes: Int? = null,
    val isLoading: Boolean = false,
    val errorMessage: String? = null
)
