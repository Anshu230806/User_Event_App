package com.example.eventsapp.feature.registration

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle

@Composable
fun RegistrationRoute(
    eventId: String,
    onBack: () -> Unit,
    onSuccess: () -> Unit,
    viewModel: RegistrationViewModel = hiltViewModel()
) {
    val state by viewModel.uiState.collectAsStateWithLifecycle()
    RegistrationScreen(
        state = state,
        onBack = onBack,
        onFullName = viewModel::updateFullName,
        onEmail = viewModel::updateEmail,
        onPhone = viewModel::updatePhone,
        onCollege = viewModel::updateCollege,
        onCourse = viewModel::updateCourse,
        onDetails = viewModel::updateDetails,
        onSubmit = { viewModel.submit(eventId) },
        onSuccess = onSuccess
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun RegistrationScreen(
    state: RegistrationUiState,
    onBack: () -> Unit,
    onFullName: (String) -> Unit,
    onEmail: (String) -> Unit,
    onPhone: (String) -> Unit,
    onCollege: (String) -> Unit,
    onCourse: (String) -> Unit,
    onDetails: (String) -> Unit,
    onSubmit: () -> Unit,
    onSuccess: () -> Unit
) {
    androidx.compose.runtime.LaunchedEffect(state.successMessage) {
        if (state.successMessage != null) onSuccess()
    }
    Scaffold(topBar = {
        TopAppBar(
            title = { Text("Event registration") },
            navigationIcon = {
                IconButton(onClick = onBack) {
                    Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                }
            }
        )
    }) { padding ->
        Column(
            Modifier.fillMaxSize().padding(padding).verticalScroll(rememberScrollState()).padding(20.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Text("Complete your details to register.", style = MaterialTheme.typography.bodyMedium)
            OutlinedTextField(state.fullName, onFullName, label = { Text("Full name *") }, modifier = Modifier.fillMaxWidth(), singleLine = true)
            OutlinedTextField(state.email, onEmail, label = { Text("Email *") }, modifier = Modifier.fillMaxWidth(), singleLine = true)
            OutlinedTextField(state.phone, onPhone, label = { Text("Phone") }, modifier = Modifier.fillMaxWidth(), singleLine = true)
            OutlinedTextField(state.collegeName, onCollege, label = { Text("College") }, modifier = Modifier.fillMaxWidth(), singleLine = true)
            OutlinedTextField(state.course, onCourse, label = { Text("Course / year") }, modifier = Modifier.fillMaxWidth(), singleLine = true)
            OutlinedTextField(state.additionalDetails, onDetails, label = { Text("Additional details") }, modifier = Modifier.fillMaxWidth(), minLines = 3)
            state.errorMessage?.let { Text(it, color = MaterialTheme.colorScheme.error) }
            Button(onClick = onSubmit, enabled = !state.isSubmitting, modifier = Modifier.fillMaxWidth()) {
                if (state.isSubmitting) CircularProgressIndicator()
                else Text("Submit registration")
            }
            Spacer(Modifier.height(8.dp))
        }
    }
}
