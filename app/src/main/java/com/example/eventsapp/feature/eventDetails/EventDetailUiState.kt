package com.example.eventsapp.feature.eventdetail

import com.example.eventsapp.data.repository.source.local.entity.EventEntity

data class EventDetailUiState(
    val event: EventEntity? = null,
    val isLoading: Boolean = false,
    val errorMessage: String? = null,

    val isRegistering: Boolean = false,
    val isRegistered: Boolean = false,
    val registrationMessage: String? = null,
    val registrationError: String? = null
)