package com.example.eventsapp.feature.clubs

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

@HiltViewModel
class ClubsViewModel @Inject constructor() : ViewModel() {
    private val _uiState = MutableStateFlow(ClubsUiState())
    val uiState: StateFlow<ClubsUiState> = _uiState.asStateFlow()

    // Connect this to the clubs repository/API when that endpoint is available.
    fun showLoadError(message: String) {
        _uiState.value = _uiState.value.copy(isLoading = false, errorMessage = message)
    }
}
