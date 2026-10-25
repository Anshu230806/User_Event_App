package com.example.eventsapp.feature.clubs

data class ClubUiModel(
    val id: String,
    val name: String,
    val description: String,
    val logoUrl: String? = null
)

data class ClubsUiState(
    val clubs: List<ClubUiModel> = emptyList(),
    val isLoading: Boolean = false,
    val errorMessage: String? = null
)
