package com.example.eventsapp.feature.home

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Event
import androidx.compose.material.icons.filled.EventAvailable
import androidx.compose.material.icons.filled.Explore
import androidx.compose.material.icons.filled.Groups
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.TopAppBarDefaults.topAppBarColors
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil3.compose.AsyncImage
import com.example.eventsapp.data.repository.source.local.entity.EventEntity
import com.example.eventsapp.feature.theme.EventsAppTheme
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

import androidx.compose.foundation.layout.Box
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.ui.Alignment

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EventRoute(
    modifier: Modifier = Modifier,
    eventScreenViewModel: EventScreenViewModel = hiltViewModel(),
    onEventClick: (String)->Unit,
    onHomeClick: () -> Unit = {},
    onMyEventsClick: () -> Unit = {},
    onClubsClick: () -> Unit = {},
    onOppurtunitiesClick: () -> Unit = {},
    onProfileClick: () -> Unit = {}
) {
    val uiState by eventScreenViewModel.uiState.collectAsStateWithLifecycle()

    Scaffold(
        modifier = modifier.fillMaxSize(),
        topBar = {
            TopAppBar(
                colors = topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    titleContentColor = MaterialTheme.colorScheme.onPrimary,
                ),
                title = {
                    Text(text = "Event Organizer App")
                }
            )
        },
        bottomBar = {
            EventBottomNavigation(
                onHomeClick = onHomeClick,
                onOppurtunitiesClick = onOppurtunitiesClick,
                onClubsClick = onClubsClick,
                onMyEventsClick = onMyEventsClick,
                onProfileClick = onProfileClick,
            )
        }
    ) { innerPadding ->
        if (uiState.isLoading) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator()
            }
        } else if (uiState.errorMessage != null) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding),
                contentAlignment = Alignment.Center
            ) {
                Text(text = uiState.errorMessage!!, color = Color.Red)
            }
        } else {
            EventScreen(
                eventList = uiState.eventsList,
                onEventClick = onEventClick,
                contentPadding = innerPadding
            )
        }
    }
}

@Composable
fun EventScreen(
    eventList: List<EventEntity>,
    modifier: Modifier = Modifier,
    onEventClick: (String) -> Unit = {},
    contentPadding: PaddingValues = PaddingValues()
) {
    LazyColumn(
        modifier = modifier.fillMaxSize(),
        contentPadding = PaddingValues(
            top = contentPadding.calculateTopPadding() + 12.dp,
            bottom = contentPadding.calculateBottomPadding() + 12.dp,
            start = 16.dp,
            end = 16.dp
        ),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        items(
            items = eventList,
            key = { event -> event.eventId }
        ) { event ->
            EventCard(
                event = event,
                onClick = {
                    onEventClick(event.eventId)
                }
            )
        }
    }
}
@Composable
fun EventCard(
    event: EventEntity,
    onClick: () -> Unit = {}
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        onClick = onClick,
        elevation = CardDefaults.cardElevation(
            defaultElevation = 3.dp
        )
    ) {
        Column(
            modifier = Modifier.fillMaxWidth()
        ) {
//            AsyncImage(
//                model = event.eventImageURL,
//                contentDescription = event.eventTitle,
//                modifier = Modifier
//                    .fillMaxWidth()
//                    .height(180.dp),
//                contentScale = ContentScale.Crop
//            )

            Column(
                modifier = Modifier.padding(14.dp)
            ) {
                Text(
                    text = event.eventTitle,
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )

                Text(
                    text = event.eventDescription,
                    modifier = Modifier.padding(top = 6.dp),
                    style = MaterialTheme.typography.bodyMedium,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )

                Text(
                    text = formatEventDateTime(event.eventStartTime),
                    modifier = Modifier.padding(top = 10.dp),
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.primary
                )

                Text(
                    text = event.eventVenue,
                    modifier = Modifier.padding(top = 4.dp),
                    style = MaterialTheme.typography.bodyMedium
                )

                Text(
                    text = if (event.isPaid) {
                        "₹${event.price}"
                    } else {
                        "Free"
                    },
                    modifier = Modifier.padding(top = 8.dp),
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.primary
                )
            }
        }
    }
}


private fun formatEventDateTime(
    timestamp: Long
): String {
    val formatter = SimpleDateFormat(
        "dd MMM yyyy • hh:mm a",
        Locale.getDefault()
    )
    return formatter.format(Date(timestamp))
}


@Composable
fun EventBottomNavigation(
    onHomeClick: () -> Unit,
    onOppurtunitiesClick: () -> Unit,
    onClubsClick: ()->Unit,
    onMyEventsClick: () -> Unit,
    onProfileClick: () -> Unit
) {
    NavigationBar {
        NavigationBarItem(
            selected = true,
            onClick = onHomeClick,
            icon = { Icon(Icons.Default.Home, contentDescription = "Home") },
            label = { Text("Home") }
        )
        NavigationBarItem(
            selected = false,
            onClick = onOppurtunitiesClick,
            icon = { Icon(Icons.Default.Explore, contentDescription = "Oppurtunities") },
            label = { Text("Events") }
        )
        NavigationBarItem(
            selected = false,
            onClick = onClubsClick,
            icon = { Icon(Icons.Default.Groups, contentDescription = "Clubs") },
            label = { Text("Add") }
        )
        NavigationBarItem(
            selected = false,
            onClick = onMyEventsClick,
            icon = { Icon(Icons.Default.EventAvailable, contentDescription = "MyEvents") },
            label = { Text("Profile") }
        )
        NavigationBarItem(
            selected = false,
            onClick = onProfileClick,
            icon = { Icon(Icons.Default.Person, contentDescription = "Profile") },
            label = { Text("Profile") }
        )
    }
}


//
//@Preview(showBackground = true)
//@Composable
//fun EventScreenPreview() {
//    EventsAppTheme {
//        EventScreen(
//            eventList = emptyList(),
//            contentPadding = PaddingValues(16.dp)
//        )
//    }
//}


@Preview(
    showBackground = true,
    showSystemUi = true
)
@Composable
fun EventScreenPreview() {
    EventsAppTheme {
        EventScreen(
            eventList = listOf(
                EventEntity(
                    eventId = "event_001",
                    eventTitle = "Technovate 2K24",
                    eventImageURL = null,
                    eventDescription = "Innovate. Build. Inspire.",
                    eventStartTime = 1713600000000L,
                    eventDuration = 7200000L,
                    eventDate = 1220000L,
                    eventVenue = "New Auditorium",
                    eventAddress = "Block A",
                    eventEligiblity = "All Students",
                    eventLikes = 25,
                    isPaid = true,
                    price = 299,
                    clubId = "gdsc",
                    organiserId = "organiser_001",
                    categoryId = "technology",
                    registrationLink = null
                ),
                EventEntity(
                    eventId = "event_002",
                    eventTitle = "Web Development Workshop",
                    eventImageURL = null,
                    eventDescription = "Learn modern web development.",
                    eventStartTime = 1715400000000L,
                    eventDuration = 10800000L,
                    eventDate = 1220000L,
                    eventVenue = "Computer Lab",
                    eventAddress = "Block B",
                    eventEligiblity = "CSE Students",
                    eventLikes = 18,
                    isPaid = false,
                    price = 0,
                    clubId = "acm",
                    organiserId = "organiser_002",
                    categoryId = "web",
                    registrationLink = null
                )
            ),
            onEventClick = {},
            contentPadding = PaddingValues()
        )
    }
}
