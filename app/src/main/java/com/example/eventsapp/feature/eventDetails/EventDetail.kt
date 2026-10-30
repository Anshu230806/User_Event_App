package com.example.eventsapp.feature.eventdetail

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Payments
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import com.example.eventsapp.data.repository.source.local.entity.EventEntity
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EventDetailScreen(
    event: EventEntity,
    onBackClick: () -> Unit,
    onRegisterClick: () -> Unit = {},
    isRegistering: Boolean = false,
    isRegistered: Boolean = false,
    registrationMessage: String? = null,
    registrationError: String? = null
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Event details") },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        },bottomBar = {
            Surface(tonalElevation = 3.dp) {
                Column(
                    modifier = Modifier.padding(16.dp)
                ) {

                    registrationMessage?.let {
                        Text(
                            text = it,
                            color = MaterialTheme.colorScheme.primary
                        )
                    }

                    registrationError?.let {
                        Text(
                            text = it,
                            color = MaterialTheme.colorScheme.error
                        )
                    }

                    Button(
                        onClick = onRegisterClick,
                        enabled = !isRegistering && !isRegistered,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 8.dp),
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        if (isRegistering) {
                            CircularProgressIndicator(
                                modifier = Modifier.size(22.dp),
                                color = MaterialTheme.colorScheme.onPrimary
                            )
                        } else {
                            Text(
                                if (isRegistered) {
                                    "Registered"
                                } else {
                                    "Register for event"
                                }
                            )
                        }
                    }
                }
            }
        }
    ) { padding ->
        Column(
            modifier = Modifier.fillMaxSize().padding(padding).verticalScroll(rememberScrollState())
        ) {
            if (!event.eventImageURL.isNullOrBlank()) {
                AsyncImage(
                    model = event.eventImageURL,
                    contentDescription = event.eventTitle,
                    modifier = Modifier.fillMaxWidth().height(240.dp),
                    contentScale = ContentScale.Crop
                )
            } else {
                Surface(modifier = Modifier.fillMaxWidth().height(180.dp), color = MaterialTheme.colorScheme.primaryContainer) {
                    Box(contentAlignment = androidx.compose.ui.Alignment.Center) {
                        Text("EventHub", style = MaterialTheme.typography.headlineMedium, color = MaterialTheme.colorScheme.onPrimaryContainer)
                    }
                }
            }
            Column(Modifier.padding(20.dp), verticalArrangement = Arrangement.spacedBy(14.dp)) {
                Text(event.eventTitle, style = MaterialTheme.typography.headlineSmall, fontWeight = FontWeight.Bold)
                Text(if (event.isPaid) "₹${event.price}" else "Free event", style = MaterialTheme.typography.titleMedium, color = MaterialTheme.colorScheme.primary, fontWeight = FontWeight.Bold)
                HorizontalDivider()
                DetailRow(icon = { Icon(Icons.Default.CalendarMonth, contentDescription = null) }, title = "Date and time", value = formatDate(event.eventStartTime))
                DetailRow(icon = { Icon(Icons.Default.LocationOn, contentDescription = null) }, title = "Venue", value = listOf(event.eventVenue, event.eventAddress).filter { it.isNotBlank() }.joinToString(", "))
                DetailRow(icon = { Icon(Icons.Default.Payments, contentDescription = null) }, title = "Eligibility", value = event.eventEligiblity.ifBlank { "Not specified" })
                HorizontalDivider()
                Text("About this event", style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold)
                Text(event.eventDescription, style = MaterialTheme.typography.bodyLarge)
                Text("Likes: ${event.eventLikes}", style = MaterialTheme.typography.bodyMedium, color = MaterialTheme.colorScheme.onSurfaceVariant)
            }
        }
    }
}

@Composable
private fun DetailRow(icon: @Composable () -> Unit, title: String, value: String) {
    Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
        icon()
        Column {
            Text(title, style = MaterialTheme.typography.titleSmall, fontWeight = FontWeight.SemiBold)
            Spacer(Modifier.height(3.dp))
            Text(value.ifBlank { "Not specified" }, style = MaterialTheme.typography.bodyMedium)
        }
    }
}

private fun formatDate(timestamp: Long): String = SimpleDateFormat(
    "EEE, dd MMM yyyy • hh:mm a", Locale.getDefault()
).format(Date(timestamp))



@Preview(showBackground = true, showSystemUi = true)
@Composable
private fun EventDetailScreenPreview() {
    MaterialTheme {
        EventDetailScreen(
            event = EventEntity(
                eventId = "event-1",
                eventTitle = "Annual Tech Fest 2026",
                eventImageURL = null,
                eventDescription = """
                    Join us for an exciting technology festival featuring
                    coding competitions, hackathons, technical workshops,
                    project exhibitions, and networking opportunities.

                    Connect with developers, showcase your projects, and
                    learn from industry experts.
                """.trimIndent(),
                eventDate = 1795564800000L,
                eventStartTime = 1795584600000L,
                eventDuration = 180L,
                eventVenue = "SKIT Campus",
                eventAddress = "Ramnagaria, Jaipur, Rajasthan",
                eventEligiblity = "College students and technology enthusiasts",
                eventLikes = 128,
                isPaid = false,
                price = 0,
                clubId = "club-1",
                organiserId = "organiser-1",
                categoryId = "category-1",
                registrationLink = "https://example.com/register"
            ),
            onBackClick = {},
            onRegisterClick = {}
        )
    }
}

@Composable
fun EventDetailRoute(
    eventId: String,
    onBackClick: () -> Unit,
    onRegisterClick: (String) -> Unit,
    viewModel: EventDetailViewModel = androidx.hilt.navigation.compose.hiltViewModel()
) {
    val state by viewModel.uiState.collectAsStateWithLifecycle()
    androidx.compose.runtime.LaunchedEffect(eventId) { viewModel.loadEvent(eventId) }
    val errorMessage = state.errorMessage
    val event = state.event
    when {
        state.isLoading -> androidx.compose.foundation.layout.Box(
            Modifier.fillMaxSize(),
            contentAlignment = androidx.compose.ui.Alignment.Center
        ) { CircularProgressIndicator() }
        errorMessage != null -> androidx.compose.foundation.layout.Column(Modifier.fillMaxSize().padding(24.dp)) {
            Text(errorMessage, color = MaterialTheme.colorScheme.error)
            TextButton(onClick = onBackClick) { Text("Back") }
        }
        event != null -> EventDetailScreen(
            event = event,
            onBackClick = onBackClick,
            onRegisterClick = viewModel::registerForEvent,
            isRegistering = state.isRegistering,
            isRegistered = state.isRegistered,
            registrationMessage = state.registrationMessage,
            registrationError = state.registrationError
        )
    }
}
