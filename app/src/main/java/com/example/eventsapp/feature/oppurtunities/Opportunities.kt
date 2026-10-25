package com.example.eventsapp.feature.opportunities

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Explore
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle

@Composable
fun OpportunitiesRoute(onOpportunityClick: (String) -> Unit = {}, viewModel: OpportunitiesViewModel = hiltViewModel()) {
    val state by viewModel.uiState.collectAsStateWithLifecycle()
    OpportunitiesScreen(state = state, onOpportunityClick = onOpportunityClick)
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun OpportunitiesScreen(state: OpportunitiesUiState, onOpportunityClick: (String) -> Unit = {}) {
    Scaffold(topBar = { TopAppBar(title = { Text("Opportunities") }) }) { padding ->
        when {
            state.isLoading -> Box(Modifier.fillMaxSize().padding(padding), contentAlignment = Alignment.Center) { CircularProgressIndicator() }
            state.errorMessage != null -> Box(Modifier.fillMaxSize().padding(24.dp), contentAlignment = Alignment.Center) { Text(state.errorMessage, color = MaterialTheme.colorScheme.error) }
            state.opportunities.isEmpty() -> Box(Modifier.fillMaxSize().padding(padding).padding(24.dp), contentAlignment = Alignment.Center) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Icon(Icons.Default.Explore, contentDescription = null, modifier = Modifier.size(56.dp), tint = MaterialTheme.colorScheme.primary)
                    Spacer(Modifier.height(12.dp))
                    Text("Explore opportunities", style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold)
                    Spacer(Modifier.height(6.dp))
                    Text("Internships, competitions and programs will appear here when the API is connected.")
                }
            }
            else -> LazyColumn(Modifier.fillMaxSize().padding(padding).padding(16.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
                items(state.opportunities, key = { it.id }) { item ->
                    Card(Modifier.fillMaxWidth().clickable { onOpportunityClick(item.id) }, shape = RoundedCornerShape(16.dp)) {
                        Column(Modifier.padding(16.dp)) {
                            Text(item.type.uppercase(), style = MaterialTheme.typography.labelMedium, color = MaterialTheme.colorScheme.primary)
                            Spacer(Modifier.height(6.dp))
                            Text(item.title, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
                            Text(item.organization, style = MaterialTheme.typography.bodyMedium)
                            Spacer(Modifier.height(8.dp))
                            Text(item.description, style = MaterialTheme.typography.bodyMedium)
                            item.deadline?.let { Text("Deadline: $it", modifier = Modifier.padding(top = 8.dp), color = MaterialTheme.colorScheme.primary) }
                        }
                    }
                }
            }
        }
    }
}



@Preview(showBackground = true, showSystemUi = true)
@Composable
private fun OpportunitiesScreenPreview() {
    MaterialTheme {
        OpportunitiesScreen(
            state = OpportunitiesUiState(
                isLoading = false,
                errorMessage = null,
                opportunities = listOf(
                    OpportunityUiModel(
                        id = "1",
                        type = "Internship",
                        title = "Android Developer Intern",
                        organization = "Google",
                        description = "Work on Android applications, Kotlin, and Jetpack Compose.",
                        deadline = "15 Nov 2026"
                    ),
                    OpportunityUiModel(
                        id = "2",
                        type = "Competition",
                        title = "Smart India Hackathon",
                        organization = "Government of India",
                        description = "Build innovative solutions to real-world problems with your team.",
                        deadline = "20 Nov 2026"
                    ),
                    OpportunityUiModel(
                        id = "3",
                        type = "Program",
                        title = "Student Developer Program",
                        organization = "Microsoft",
                        description = "Develop technical skills through projects, learning resources, and community events.",
                        deadline = null
                    )
                )
            ),
            onOpportunityClick = {}
        )
    }
}
