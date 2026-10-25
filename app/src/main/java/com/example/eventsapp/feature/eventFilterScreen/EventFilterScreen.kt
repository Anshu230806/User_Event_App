
package com.example.eventsapp.feature

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Event
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Schedule
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.eventsapp.data.repository.source.local.entity.EventEntity
import com.example.eventsapp.feature.theme.EventsAppTheme
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale
import java.util.TimeZone

enum class EventSearchFilter(val label: String) {
    TODAY_EVENTS("Today's Events"),
    REGISTRATION_CLOSING_TODAY("Registration Closes Today"),
    CUSTOM_DATE("Choose Date"),
    ALL_EVENTS("All Events")
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EventsFilterScreen(
    events: List<EventEntity>,
    onEventClick: (String) -> Unit = {},

    // Return the event's registration deadline as epoch milliseconds.
    // Return null when the deadline is unknown.
    registrationDeadlineProvider: (EventEntity) -> Long? = { null }
) {
    var selectedFilter by remember {
        mutableStateOf(EventSearchFilter.TODAY_EVENTS)
    }

    var selectedDateMillis by remember {
        mutableStateOf<Long?>(null)
    }

    var selectedTimeMinutes by remember {
        mutableStateOf<Int?>(null)
    }

    var selectedDurationMinutes by remember {
        mutableStateOf<Int?>(null)
    }

    var showDatePicker by remember {
        mutableStateOf(false)
    }

    var showTimePicker by remember {
        mutableStateOf(false)
    }

    var showDurationMenu by remember {
        mutableStateOf(false)
    }

    var appliedFilter by remember {
        mutableStateOf(EventSearchFilter.TODAY_EVENTS)
    }

    var appliedDateMillis by remember {
        mutableStateOf<Long?>(null)
    }

    var appliedTimeMinutes by remember {
        mutableStateOf<Int?>(null)
    }

    var appliedDurationMinutes by remember {
        mutableStateOf<Int?>(null)
    }

    fun startOfDay(timestamp: Long): Long {
        return Calendar.getInstance().apply {
            timeInMillis = timestamp
            set(Calendar.HOUR_OF_DAY, 0)
            set(Calendar.MINUTE, 0)
            set(Calendar.SECOND, 0)
            set(Calendar.MILLISECOND, 0)
        }.timeInMillis
    }

    fun startOfNextDay(timestamp: Long): Long {
        return Calendar.getInstance().apply {
            timeInMillis = startOfDay(timestamp)
            add(Calendar.DAY_OF_MONTH, 1)
        }.timeInMillis
    }

    val filteredEvents = remember(
        events,
        appliedFilter,
        appliedDateMillis,
        appliedTimeMinutes,
        appliedDurationMinutes
    ) {
        val now = System.currentTimeMillis()
        val todayStart = startOfDay(now)
        val tomorrowStart = startOfNextDay(now)

        events.filter { event ->
            val eventTime = event.eventStartTime

            val matchesDate = when (appliedFilter) {
                EventSearchFilter.TODAY_EVENTS ->
                    eventTime >= todayStart &&
                            eventTime < tomorrowStart

                EventSearchFilter.REGISTRATION_CLOSING_TODAY -> {
                    val deadline = registrationDeadlineProvider(event)
                    deadline != null &&
                            deadline >= todayStart &&
                            deadline < tomorrowStart
                }

                EventSearchFilter.CUSTOM_DATE -> {
                    val selectedDate = appliedDateMillis

                    selectedDate != null &&
                            eventTime >= startOfDay(selectedDate) &&
                            eventTime < startOfNextDay(selectedDate)
                }

                EventSearchFilter.ALL_EVENTS -> true
            }

            val matchesTime = appliedTimeMinutes?.let { selectedMinutes ->
                val eventCalendar = Calendar.getInstance().apply {
                    timeInMillis = eventTime
                }

                val eventMinutes =
                    eventCalendar.get(Calendar.HOUR_OF_DAY) * 60 +
                            eventCalendar.get(Calendar.MINUTE)

                eventMinutes == selectedMinutes
            } ?: true

            // Assumption: eventDuration is stored in minutes.
            // Change this comparison if your backend uses another unit.
            val matchesDuration = appliedDurationMinutes?.let { duration ->
                event.eventDuration == duration.toLong()
            } ?: true

            matchesDate && matchesTime && matchesDuration
        }.sortedBy { it.eventStartTime }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Explore Events") }
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(horizontal = 16.dp)
        ) {
            Spacer(Modifier.height(12.dp))

            Text(
                text = "Find Events",
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.Bold
            )

            Spacer(Modifier.height(6.dp))

            Text(
                text = "Filter campus events by date, time and duration.",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )

            Spacer(Modifier.height(16.dp))

            // Date filter choices
            Text(
                text = "Date Filter",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.SemiBold
            )

            Spacer(Modifier.height(8.dp))

            EventSearchFilter.entries.forEach { filter ->
                FilterChip(
                    selected = selectedFilter == filter,
                    onClick = {
                        selectedFilter = filter

                        if (filter == EventSearchFilter.CUSTOM_DATE) {
                            showDatePicker = true
                        }
                    },
                    label = { Text(filter.label) },
                    leadingIcon = {
                        Icon(
                            imageVector = Icons.Default.CalendarMonth,
                            contentDescription = null,
                            modifier = Modifier.size(18.dp)
                        )
                    },
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(Modifier.height(4.dp))
            }

            if (selectedFilter == EventSearchFilter.CUSTOM_DATE) {
                OutlinedButton(
                    onClick = { showDatePicker = true },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Icon(
                        Icons.Default.CalendarMonth,
                        contentDescription = null
                    )

                    Spacer(Modifier.width(8.dp))

                    Text(
                        text = selectedDateMillis?.let {
                            formatDate(it)
                        } ?: "Select a date"
                    )
                }
            }

            Spacer(Modifier.height(12.dp))

            HorizontalDivider()

            Spacer(Modifier.height(12.dp))

            // Optional start time
            Text(
                text = "Start Time (Optional)",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.SemiBold
            )

            Spacer(Modifier.height(6.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                OutlinedButton(
                    onClick = { showTimePicker = true },
                    modifier = Modifier.weight(1f)
                ) {
                    Icon(
                        Icons.Default.Schedule,
                        contentDescription = null
                    )

                    Spacer(Modifier.width(8.dp))

                    Text(
                        selectedTimeMinutes?.let {
                            formatTime(it)
                        } ?: "Any time"
                    )
                }

                if (selectedTimeMinutes != null) {
                    IconButton(
                        onClick = {
                            selectedTimeMinutes = null
                        }
                    ) {
                        Icon(
                            Icons.Default.Clear,
                            contentDescription = "Clear start time"
                        )
                    }
                }
            }

            Spacer(Modifier.height(12.dp))

            // Optional duration
            Text(
                text = "Event Duration (Optional)",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.SemiBold
            )

            Spacer(Modifier.height(6.dp))

            Box {
                OutlinedButton(
                    onClick = { showDurationMenu = true },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(
                        selectedDurationMinutes?.let {
                            formatDuration(it)
                        } ?: "Any duration"
                    )
                }

                DropdownMenu(
                    expanded = showDurationMenu,
                    onDismissRequest = {
                        showDurationMenu = false
                    }
                ) {
                    DropdownMenuItem(
                        text = { Text("Any duration") },
                        onClick = {
                            selectedDurationMinutes = null
                            showDurationMenu = false
                        }
                    )

                    listOf(30, 60, 90, 120, 180, 240).forEach { duration ->
                        DropdownMenuItem(
                            text = { Text(formatDuration(duration)) },
                            onClick = {
                                selectedDurationMinutes = duration
                                showDurationMenu = false
                            }
                        )
                    }
                }
            }

            Spacer(Modifier.height(16.dp))

            // Apply and clear
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Button(
                    onClick = {
                        appliedFilter = selectedFilter
                        appliedDateMillis = selectedDateMillis
                        appliedTimeMinutes = selectedTimeMinutes
                        appliedDurationMinutes = selectedDurationMinutes
                    },
                    modifier = Modifier.weight(1f)
                ) {
                    Text("Apply Filters")
                }

                OutlinedButton(
                    onClick = {
                        selectedFilter = EventSearchFilter.ALL_EVENTS
                        selectedDateMillis = null
                        selectedTimeMinutes = null
                        selectedDurationMinutes = null

                        appliedFilter = EventSearchFilter.ALL_EVENTS
                        appliedDateMillis = null
                        appliedTimeMinutes = null
                        appliedDurationMinutes = null
                    },
                    modifier = Modifier.weight(1f)
                ) {
                    Text("Clear")
                }
            }

            Spacer(Modifier.height(16.dp))

            Text(
                text = "${filteredEvents.size} events found",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.SemiBold
            )

            Spacer(Modifier.height(8.dp))

            if (filteredEvents.isEmpty()) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f),
                    contentAlignment = Alignment.Center
                ) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Icon(
                            imageVector = Icons.Default.Event,
                            contentDescription = null,
                            modifier = Modifier.size(52.dp),
                            tint = MaterialTheme.colorScheme.primary
                        )

                        Spacer(Modifier.height(12.dp))

                        Text(
                            text = "No events found",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.SemiBold
                        )

                        Text(
                            text = "Try changing your filters.",
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
            } else {
                LazyColumn(
                    modifier = Modifier.weight(1f),
                    verticalArrangement = Arrangement.spacedBy(12.dp),
                    contentPadding = PaddingValues(bottom = 16.dp)
                ) {
                    items(
                        items = filteredEvents,
                        key = { it.eventId }
                    ) { event ->
                        Card(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable {
                                    onEventClick(event.eventId)
                                },
                            shape = RoundedCornerShape(16.dp)
                        ) {
                            Column(
                                modifier = Modifier.padding(16.dp)
                            ) {
                                Text(
                                    text = event.eventTitle,
                                    style = MaterialTheme.typography.titleMedium,
                                    fontWeight = FontWeight.Bold
                                )

                                Spacer(Modifier.height(8.dp))

                                Row(
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Icon(
                                        Icons.Default.CalendarMonth,
                                        contentDescription = null,
                                        modifier = Modifier.size(18.dp)
                                    )

                                    Spacer(Modifier.width(8.dp))

                                    Text(
                                        text = formatEventDate(
                                            event.eventStartTime
                                        )
                                    )
                                }

                                Spacer(Modifier.height(6.dp))

                                Row(
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Icon(
                                        Icons.Default.LocationOn,
                                        contentDescription = null,
                                        modifier = Modifier.size(18.dp)
                                    )

                                    Spacer(Modifier.width(8.dp))

                                    Text(
                                        text = event.eventVenue.ifBlank {
                                            "Venue not specified"
                                        }
                                    )
                                }

                                Spacer(Modifier.height(8.dp))

                                Text(
                                    text = if (event.isPaid) {
                                        "₹${event.price}"
                                    } else {
                                        "Free"
                                    },
                                    color = MaterialTheme.colorScheme.primary,
                                    fontWeight = FontWeight.SemiBold
                                )
                            }
                        }
                    }
                }
            }
        }
    }

    // Date picker dialog
    if (showDatePicker) {
        val datePickerState = rememberDatePickerState(
            initialSelectedDateMillis = selectedDateMillis
        )

        DatePickerDialog(
            onDismissRequest = {
                showDatePicker = false
            },
            confirmButton = {
                TextButton(
                    onClick = {
                        selectedDateMillis =
                            datePickerState.selectedDateMillis

                        showDatePicker = false
                    }
                ) {
                    Text("Select")
                }
            },
            dismissButton = {
                TextButton(
                    onClick = {
                        showDatePicker = false
                    }
                ) {
                    Text("Cancel")
                }
            }
        ) {
            DatePicker(state = datePickerState)
        }
    }

    // Time picker dialog
    if (showTimePicker) {
        val currentMinutes = selectedTimeMinutes
            ?: Calendar.getInstance().let {
                it.get(Calendar.HOUR_OF_DAY) * 60 +
                        it.get(Calendar.MINUTE)
            }

        val timePickerState = rememberTimePickerState(
            initialHour = currentMinutes / 60,
            initialMinute = currentMinutes % 60,
            is24Hour = false
        )

        AlertDialog(
            onDismissRequest = {
                showTimePicker = false
            },
            title = {
                Text("Select Start Time")
            },
            text = {
                TimePicker(state = timePickerState)
            },
            confirmButton = {
                TextButton(
                    onClick = {
                        selectedTimeMinutes =
                            timePickerState.hour * 60 +
                                    timePickerState.minute

                        showTimePicker = false
                    }
                ) {
                    Text("Select")
                }
            },
            dismissButton = {
                TextButton(
                    onClick = {
                        showTimePicker = false
                    }
                ) {
                    Text("Cancel")
                }
            }
        )
    }
}

private fun formatDate(timestamp: Long): String {
    return SimpleDateFormat(
        "EEE, dd MMM yyyy",
        Locale.getDefault()
    ).format(Date(timestamp))
}

private fun formatEventDate(timestamp: Long): String {
    return SimpleDateFormat(
        "EEE, dd MMM • hh:mm a",
        Locale.getDefault()
    ).format(Date(timestamp))
}

private fun formatTime(totalMinutes: Int): String {
    val hour = totalMinutes / 60
    val minute = totalMinutes % 60

    val calendar = Calendar.getInstance().apply {
        set(Calendar.HOUR_OF_DAY, hour)
        set(Calendar.MINUTE, minute)
    }

    return SimpleDateFormat(
        "hh:mm a",
        Locale.getDefault()
    ).format(calendar.time)
}

private fun formatDuration(minutes: Int): String {
    val hours = minutes / 60
    val remainingMinutes = minutes % 60

    return when {
        hours == 0 -> "$minutes minutes"
        remainingMinutes == 0 -> "$hours hour${if (hours > 1) "s" else ""}"
        else -> "$hours hr $remainingMinutes min"
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
private fun EventsFilterScreenPreview() {
    val calendar = Calendar.getInstance()

    fun eventAt(
        id: String,
        title: String,
        daysFromNow: Int,
        venue: String,
        durationMinutes: Long = 120L
    ): EventEntity {
        val date = (calendar.clone() as Calendar).apply {
            add(Calendar.DAY_OF_MONTH, daysFromNow)
            set(Calendar.HOUR_OF_DAY, 15)
            set(Calendar.MINUTE, 0)
            set(Calendar.SECOND, 0)
            set(Calendar.MILLISECOND, 0)
        }

        return EventEntity(
            eventId = id,
            eventTitle = title,
            eventImageURL = null,
            eventDescription = "A campus event for students.",
            eventDate = date.timeInMillis,
            eventStartTime = date.timeInMillis,
            eventDuration = durationMinutes,
            eventVenue = venue,
            eventAddress = "Jaipur, Rajasthan",
            eventEligiblity = "All college students",
            eventLikes = 0,
            isPaid = false,
            price = 0,
            clubId = "club-1",
            organiserId = "organiser-1",
            categoryId = "category-1",
            registrationLink = null
        )
    }

    EventsAppTheme {
        EventsFilterScreen(
            events = listOf(
                eventAt(
                    "1",
                    "Android Development Workshop",
                    0,
                    "Seminar Hall"
                ),
                eventAt(
                    "2",
                    "Coding Competition",
                    2,
                    "Computer Lab",
                    60L
                ),
                eventAt(
                    "3",
                    "Annual Tech Fest",
                    8,
                    "College Auditorium",
                    180L
                )
            )
        )
    }
}
