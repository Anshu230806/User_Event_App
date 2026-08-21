package com.example.eventsapp.feature.home

import com.example.eventsapp.data.repository.source.local.entity.EventEntity

data class HomeUiState(
    val eventsList: List<EventEntity> = emptyList(),
    val isLoading: Boolean = true,
     val errorMessage: String? = null
)
