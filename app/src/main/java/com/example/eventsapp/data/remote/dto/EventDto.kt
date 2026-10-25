package com.example.eventsapp.data.remote.dto

data class EventsResponseDto(
    val success: Boolean,
    val data: EventsDataDto
)

data class EventsDataDto(
    val events: List<EventDto>
)

data class EventDto(
    val id: String,
    val title: String,
    val posterUrl: String? = null,
    val description: String? = null,
    val startsAt: String,
    val endsAt: String? = null,
    val venue: String? = null,
    val eligibility: String? = null,
    val likesCount: Int = 0,
    val clubId: String? = null,
    val organizerId: String? = null
)
