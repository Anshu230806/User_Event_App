package com.example.eventsapp.feature.clubs

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Groups
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
import com.example.eventsapp.feature.club.ClubScreen
import com.example.eventsapp.feature.theme.EventsAppTheme

@Composable
fun ClubsRoute(onClubClick: (String) -> Unit = {}, viewModel: ClubsViewModel = hiltViewModel()) {
    val state by viewModel.uiState.collectAsStateWithLifecycle()
    ClubsScreen(state = state, onClubClick = onClubClick)
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ClubsScreen(state: ClubsUiState, onClubClick: (String) -> Unit = {}) {
    Scaffold(topBar = { TopAppBar(title = { Text("Clubs") }) }) { padding ->
        when {
            state.isLoading -> Box(Modifier.fillMaxSize().padding(padding), contentAlignment = Alignment.Center) { CircularProgressIndicator() }
            state.errorMessage != null -> Box(Modifier.fillMaxSize().padding(24.dp), contentAlignment = Alignment.Center) { Text(state.errorMessage, color = MaterialTheme.colorScheme.error) }
            state.clubs.isEmpty() -> EmptyClubs(Modifier.fillMaxSize().padding(padding))
            else -> LazyColumn(Modifier.fillMaxSize().padding(padding).padding(16.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
                items(state.clubs, key = { it.id }) { club ->
                    Card(Modifier.fillMaxWidth().clickable { onClubClick(club.id) }, shape = RoundedCornerShape(16.dp)) {
                        Row(Modifier.padding(16.dp), verticalAlignment = Alignment.CenterVertically) {
                            Icon(Icons.Default.Groups, contentDescription = null, tint = MaterialTheme.colorScheme.primary, modifier = Modifier.size(42.dp))
                            Spacer(Modifier.width(14.dp))
                            Column { Text(club.name, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold); Spacer(Modifier.height(4.dp)); Text(club.description, style = MaterialTheme.typography.bodyMedium) }
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun EmptyClubs(modifier: Modifier = Modifier) {
    Box(modifier.padding(24.dp), contentAlignment = Alignment.Center) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Icon(Icons.Default.Groups, contentDescription = null, modifier = Modifier.size(56.dp), tint = MaterialTheme.colorScheme.primary)
            Spacer(Modifier.height(12.dp))
            Text("Discover campus clubs", style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold)
            Spacer(Modifier.height(6.dp))
            Text("Clubs will appear here when connected to the backend.", style = MaterialTheme.typography.bodyMedium)
        }
    }
}


@Preview(showBackground = true, showSystemUi = true)
@Composable
private fun ClubsScreenPreview() {
    EventsAppTheme {
        ClubsScreen(
            state = ClubsUiState(
                isLoading = false,
                errorMessage = null,
                clubs = listOf(
                    ClubUiModel(
                        id = "club-1",
                        name = "Coding Club",
                        description = "Explore DSA, competitive programming, and coding contests."
                    ),
                    ClubUiModel(
                        id = "club-2",
                        name = "Robotics Club",
                        description = "Build robots and explore embedded systems and automation."
                    ),
                    ClubUiModel(
                        id = "club-3",
                        name = "Web Development Club",
                        description = "Learn frontend, backend, and full-stack development."
                    ),
                    ClubUiModel(
                        id = "club-4",
                        name = "AI & ML Club",
                        description = "Discover machine learning, artificial intelligence, and AI projects."
                    )
                ),
            ),
            onClubClick = {}
        )
    }
}