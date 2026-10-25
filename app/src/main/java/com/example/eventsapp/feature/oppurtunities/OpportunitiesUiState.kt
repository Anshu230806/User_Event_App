package com.example.eventsapp.feature.opportunities

data class OpportunityUiModel(
    val id: String,
    val title: String,
    val organization: String,
    val description: String,
    val type: String = "Opportunity",
    val deadline: String? = null
)

data class OpportunitiesUiState(
    val opportunities: List<OpportunityUiModel> = emptyList(),
    val isLoading: Boolean = false,
    val errorMessage: String? = null
)
