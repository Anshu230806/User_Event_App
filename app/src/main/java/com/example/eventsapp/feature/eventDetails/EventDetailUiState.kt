package com.example.eventsapp.feature.eventdetail

import com.example.eventsapp.data.repository.source.local.entity.EventEntity

data class EventDetailUiState(
    val event: EventEntity? = null,
    val isLoading: Boolean = false,
    val errorMessage: String? = null
)
