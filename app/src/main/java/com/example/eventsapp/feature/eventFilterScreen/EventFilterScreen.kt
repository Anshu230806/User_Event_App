//
//package com.example.eventsapp.feature
//
//import androidx.compose.foundation.clickable
//import androidx.compose.foundation.layout.*
//import androidx.compose.foundation.lazy.LazyColumn
//import androidx.compose.foundation.lazy.items
//import androidx.compose.foundation.shape.RoundedCornerShape
//import androidx.compose.material.icons.Icons
//import androidx.compose.material.icons.filled.CalendarMonth
//import androidx.compose.material.icons.filled.Clear
//import androidx.compose.material.icons.filled.Event
//import androidx.compose.material.icons.filled.LocationOn
//import androidx.compose.material.icons.filled.Schedule
//import androidx.compose.material3.*
//import androidx.compose.runtime.*
//import androidx.compose.ui.Alignment
//import androidx.compose.ui.Modifier
//import androidx.compose.ui.text.font.FontWeight
//import androidx.compose.ui.tooling.preview.Preview
//import androidx.compose.ui.unit.dp
//import com.example.eventsapp.data.repository.source.local.entity.EventEntity
//import com.example.eventsapp.feature.theme.EventsAppTheme
//import java.text.SimpleDateFormat
//import java.util.Calendar
//import java.util.Date
//import java.util.Locale
//import java.util.TimeZone
//
//enum class EventSearchFilter(val label: String) {
//    TODAY_EVENTS("Today's Events"),
//    REGISTRATION_CLOSING_TODAY("Registration Closes Today"),
//    CUSTOM_DATE("Choose Date"),
//    ALL_EVENTS("All Events")
//}
//
//@OptIn(ExperimentalMaterial3Api::class)
//@Composable
//fun EventsFilterScreen(
//    events: List<EventEntity>,
//    onEventClick: (String) -> Unit = {},
//
//    // Return the event's registration deadline as epoch milliseconds.
//    // Return null when the deadline is unknown.
//    registrationDeadlineProvider: (EventEntity) -> Long? = { null }
//) {
//    var selectedFilter by remember {
//        mutableStateOf(EventSearchFilter.TODAY_EVENTS)
//    }
//
//    var selectedDateMillis by remember {
//        mutableStateOf<Long?>(null)
//    }
//
//    var selectedTimeMinutes by remember {
//        mutableStateOf<Int?>(null)
//    }
//
//    var selectedDurationMinutes by remember {
//        mutableStateOf<Int?>(null)
//    }
//
//    var showDatePicker by remember {
//        mutableStateOf(false)
//    }
//
//    var showTimePicker by remember {
//        mutableStateOf(false)
//    }
//
//    var showDurationMenu by remember {
//        mutableStateOf(false)
//    }
//
//    var appliedFilter by remember {
//        mutableStateOf(EventSearchFilter.TODAY_EVENTS)
//    }
//
//    var appliedDateMillis by remember {
//        mutableStateOf<Long?>(null)
//    }
//
//    var appliedTimeMinutes by remember {
//        mutableStateOf<Int?>(null)
//    }
//
//    var appliedDurationMinutes by remember {
//        mutableStateOf<Int?>(null)
//    }
//
//    fun startOfDay(timestamp: Long): Long {
//        return Calendar.getInstance().apply {
//            timeInMillis = timestamp
//            set(Calendar.HOUR_OF_DAY, 0)
//            set(Calendar.MINUTE, 0)
//            set(Calendar.SECOND, 0)
//            set(Calendar.MILLISECOND, 0)
//        }.timeInMillis
//    }
//
//    fun startOfNextDay(timestamp: Long): Long {
//        return Calendar.getInstance().apply {
//            timeInMillis = startOfDay(timestamp)
//            add(Calendar.DAY_OF_MONTH, 1)
//        }.timeInMillis
//    }
//
//    val filteredEvents = remember(
//        events,
//        appliedFilter,
//        appliedDateMillis,
//        appliedTimeMinutes,
//        appliedDurationMinutes
//    ) {
//        val now = System.currentTimeMillis()
//        val todayStart = startOfDay(now)
//        val tomorrowStart = startOfNextDay(now)
//
//        events.filter { event ->
//            val eventTime = event.eventStartTime
//
//            val matchesDate = when (appliedFilter) {
//                EventSearchFilter.TODAY_EVENTS ->
//                    eventTime >= todayStart &&
//                            eventTime < tomorrowStart
//
//                EventSearchFilter.REGISTRATION_CLOSING_TODAY -> {
//                    val deadline = registrationDeadlineProvider(event)
//                    deadline != null &&
//                            deadline >= todayStart &&
//                            deadline < tomorrowStart
//                }
//
//                EventSearchFilter.CUSTOM_DATE -> {
//                    val selectedDate = appliedDateMillis
//
//                    selectedDate != null &&
//                            eventTime >= startOfDay(selectedDate) &&
//                            eventTime < startOfNextDay(selectedDate)
//                }
//
//                EventSearchFilter.ALL_EVENTS -> true
//            }
//
//            val matchesTime = appliedTimeMinutes?.let { selectedMinutes ->
//                val eventCalendar = Calendar.getInstance().apply {
//                    timeInMillis = eventTime
//                }
//
//                val eventMinutes =
//                    eventCalendar.get(Calendar.HOUR_OF_DAY) * 60 +
//                            eventCalendar.get(Calendar.MINUTE)
//
//                eventMinutes == selectedMinutes
//            } ?: true
//
//            // Assumption: eventDuration is stored in minutes.
//            // Change this comparison if your backend uses another unit.
//            val matchesDuration = appliedDurationMinutes?.let { duration ->
//                event.eventDuration == duration.toLong()
//            } ?: true
//
//            matchesDate && matchesTime && matchesDuration
//        }.sortedBy { it.eventStartTime }
//    }
//
//    Scaffold(
//        topBar = {
//            TopAppBar(
//                title = { Text("Explore Events") }
//            )
//        }
//    ) { padding ->
//        Column(
//            modifier = Modifier
//                .fillMaxSize()
//                .padding(padding)
//                .padding(horizontal = 16.dp)
//        ) {
//            Spacer(Modifier.height(12.dp))
//
//            Text(
//                text = "Find Events",
//                style = MaterialTheme.typography.headlineSmall,
//                fontWeight = FontWeight.Bold
//            )
//
//            Spacer(Modifier.height(6.dp))
//
//            Text(
//                text = "Filter campus events by date, time and duration.",
//                style = MaterialTheme.typography.bodyMedium,
//                color = MaterialTheme.colorScheme.onSurfaceVariant
//            )
//
//            Spacer(Modifier.height(16.dp))
//
//            // Date filter choices
//            Text(
//                text = "Date Filter",
//                style = MaterialTheme.typography.titleMedium,
//                fontWeight = FontWeight.SemiBold
//            )
//
//            Spacer(Modifier.height(8.dp))
//
//            EventSearchFilter.entries.forEach { filter ->
//                FilterChip(
//                    selected = selectedFilter == filter,
//                    onClick = {
//                        selectedFilter = filter
//
//                        if (filter == EventSearchFilter.CUSTOM_DATE) {
//                            showDatePicker = true
//                        }
//                    },
//                    label = { Text(filter.label) },
//                    leadingIcon = {
//                        Icon(
//                            imageVector = Icons.Default.CalendarMonth,
//                            contentDescription = null,
//                            modifier = Modifier.size(18.dp)
//                        )
//                    },
//                    modifier = Modifier.fillMaxWidth()
//                )
//
//                Spacer(Modifier.height(4.dp))
//            }
//
//            if (selectedFilter == EventSearchFilter.CUSTOM_DATE) {
//                OutlinedButton(
//                    onClick = { showDatePicker = true },
//                    modifier = Modifier.fillMaxWidth()
//                ) {
//                    Icon(
//                        Icons.Default.CalendarMonth,
//                        contentDescription = null
//                    )
//
//                    Spacer(Modifier.width(8.dp))
//
//                    Text(
//                        text = selectedDateMillis?.let {
//                            formatDate(it)
//                        } ?: "Select a date"
//                    )
//                }
//            }
//
//            Spacer(Modifier.height(12.dp))
//
//            HorizontalDivider()
//
//            Spacer(Modifier.height(12.dp))
//
//            // Optional start time
//            Text(
//                text = "Start Time (Optional)",
//                style = MaterialTheme.typography.titleMedium,
//                fontWeight = FontWeight.SemiBold
//            )
//
//            Spacer(Modifier.height(6.dp))
//
//            Row(
//                modifier = Modifier.fillMaxWidth(),
//                verticalAlignment = Alignment.CenterVertically
//            ) {
//                OutlinedButton(
//                    onClick = { showTimePicker = true },
//                    modifier = Modifier.weight(1f)
//                ) {
//                    Icon(
//                        Icons.Default.Schedule,
//                        contentDescription = null
//                    )
//
//                    Spacer(Modifier.width(8.dp))
//
//                    Text(
//                        selectedTimeMinutes?.let {
//                            formatTime(it)
//                        } ?: "Any time"
//                    )
//                }
//
//                if (selectedTimeMinutes != null) {
//                    IconButton(
//                        onClick = {
//                            selectedTimeMinutes = null
//                        }
//                    ) {
//                        Icon(
//                            Icons.Default.Clear,
//                            contentDescription = "Clear start time"
//                        )
//                    }
//                }
//            }
//
//            Spacer(Modifier.height(12.dp))
//
//            // Optional duration
//            Text(
//                text = "Event Duration (Optional)",
//                style = MaterialTheme.typography.titleMedium,
//                fontWeight = FontWeight.SemiBold
//            )
//
//            Spacer(Modifier.height(6.dp))
//
//            Box {
//                OutlinedButton(
//                    onClick = { showDurationMenu = true },
//                    modifier = Modifier.fillMaxWidth()
//                ) {
//                    Text(
//                        selectedDurationMinutes?.let {
//                            formatDuration(it)
//                        } ?: "Any duration"
//                    )
//                }
//
//                DropdownMenu(
//                    expanded = showDurationMenu,
//                    onDismissRequest = {
//                        showDurationMenu = false
//                    }
//                ) {
//                    DropdownMenuItem(
//                        text = { Text("Any duration") },
//                        onClick = {
//                            selectedDurationMinutes = null
//                            showDurationMenu = false
//                        }
//                    )
//
//                    listOf(30, 60, 90, 120, 180, 240).forEach { duration ->
//                        DropdownMenuItem(
//                            text = { Text(formatDuration(duration)) },
//                            onClick = {
//                                selectedDurationMinutes = duration
//                                showDurationMenu = false
//                            }
//                        )
//                    }
//                }
//            }
//
//            Spacer(Modifier.height(16.dp))
//
//            // Apply and clear
//            Row(
//                modifier = Modifier.fillMaxWidth(),
//                horizontalArrangement = Arrangement.spacedBy(8.dp)
//            ) {
//                Button(
//                    onClick = {
//                        appliedFilter = selectedFilter
//                        appliedDateMillis = selectedDateMillis
//                        appliedTimeMinutes = selectedTimeMinutes
//                        appliedDurationMinutes = selectedDurationMinutes
//                    },
//                    modifier = Modifier.weight(1f)
//                ) {
//                    Text("Apply Filters")
//                }
//
//                OutlinedButton(
//                    onClick = {
//                        selectedFilter = EventSearchFilter.ALL_EVENTS
//                        selectedDateMillis = null
//                        selectedTimeMinutes = null
//                        selectedDurationMinutes = null
//
//                        appliedFilter = EventSearchFilter.ALL_EVENTS
//                        appliedDateMillis = null
//                        appliedTimeMinutes = null
//                        appliedDurationMinutes = null
//                    },
//                    modifier = Modifier.weight(1f)
//                ) {
//                    Text("Clear")
//                }
//            }
//
//            Spacer(Modifier.height(16.dp))
//
//            Text(
//                text = "${filteredEvents.size} events found",
//                style = MaterialTheme.typography.titleMedium,
//                fontWeight = FontWeight.SemiBold
//            )
//
//            Spacer(Modifier.height(8.dp))
//
//            if (filteredEvents.isEmpty()) {
//                Box(
//                    modifier = Modifier
//                        .fillMaxWidth()
//                        .weight(1f),
//                    contentAlignment = Alignment.Center
//                ) {
//                    Column(
//                        horizontalAlignment = Alignment.CenterHorizontally
//                    ) {
//                        Icon(
//                            imageVector = Icons.Default.Event,
//                            contentDescription = null,
//                            modifier = Modifier.size(52.dp),
//                            tint = MaterialTheme.colorScheme.primary
//                        )
//
//                        Spacer(Modifier.height(12.dp))
//
//                        Text(
//                            text = "No events found",
//                            style = MaterialTheme.typography.titleMedium,
//                            fontWeight = FontWeight.SemiBold
//                        )
//
//                        Text(
//                            text = "Try changing your filters.",
//                            style = MaterialTheme.typography.bodyMedium,
//                            color = MaterialTheme.colorScheme.onSurfaceVariant
//                        )
//                    }
//                }
//            } else {
//                LazyColumn(
//                    modifier = Modifier.weight(1f),
//                    verticalArrangement = Arrangement.spacedBy(12.dp),
//                    contentPadding = PaddingValues(bottom = 16.dp)
//                ) {
//                    items(
//                        items = filteredEvents,
//                        key = { it.eventId }
//                    ) { event ->
//                        Card(
//                            modifier = Modifier
//                                .fillMaxWidth()
//                                .clickable {
//                                    onEventClick(event.eventId)
//                                },
//                            shape = RoundedCornerShape(16.dp)
//                        ) {
//                            Column(
//                                modifier = Modifier.padding(16.dp)
//                            ) {
//                                Text(
//                                    text = event.eventTitle,
//                                    style = MaterialTheme.typography.titleMedium,
//                                    fontWeight = FontWeight.Bold
//                                )
//
//                                Spacer(Modifier.height(8.dp))
//
//                                Row(
//                                    verticalAlignment = Alignment.CenterVertically
//                                ) {
//                                    Icon(
//                                        Icons.Default.CalendarMonth,
//                                        contentDescription = null,
//                                        modifier = Modifier.size(18.dp)
//                                    )
//
//                                    Spacer(Modifier.width(8.dp))
//
//                                    Text(
//                                        text = formatEventDate(
//                                            event.eventStartTime
//                                        )
//                                    )
//                                }
//
//                                Spacer(Modifier.height(6.dp))
//
//                                Row(
//                                    verticalAlignment = Alignment.CenterVertically
//                                ) {
//                                    Icon(
//                                        Icons.Default.LocationOn,
//                                        contentDescription = null,
//                                        modifier = Modifier.size(18.dp)
//                                    )
//
//                                    Spacer(Modifier.width(8.dp))
//
//                                    Text(
//                                        text = event.eventVenue.ifBlank {
//                                            "Venue not specified"
//                                        }
//                                    )
//                                }
//
//                                Spacer(Modifier.height(8.dp))
//
//                                Text(
//                                    text = if (event.isPaid) {
//                                        "₹${event.price}"
//                                    } else {
//                                        "Free"
//                                    },
//                                    color = MaterialTheme.colorScheme.primary,
//                                    fontWeight = FontWeight.SemiBold
//                                )
//                            }
//                        }
//                    }
//                }
//            }
//        }
//    }
//
//    // Date picker dialog
//    if (showDatePicker) {
//        val datePickerState = rememberDatePickerState(
//            initialSelectedDateMillis = selectedDateMillis
//        )
//
//        DatePickerDialog(
//            onDismissRequest = {
//                showDatePicker = false
//            },
//            confirmButton = {
//                TextButton(
//                    onClick = {
//                        selectedDateMillis =
//                            datePickerState.selectedDateMillis
//
//                        showDatePicker = false
//                    }
//                ) {
//                    Text("Select")
//                }
//            },
//            dismissButton = {
//                TextButton(
//                    onClick = {
//                        showDatePicker = false
//                    }
//                ) {
//                    Text("Cancel")
//                }
//            }
//        ) {
//            DatePicker(state = datePickerState)
//        }
//    }
//
//    // Time picker dialog
//    if (showTimePicker) {
//        val currentMinutes = selectedTimeMinutes
//            ?: Calendar.getInstance().let {
//                it.get(Calendar.HOUR_OF_DAY) * 60 +
//                        it.get(Calendar.MINUTE)
//            }
//
//        val timePickerState = rememberTimePickerState(
//            initialHour = currentMinutes / 60,
//            initialMinute = currentMinutes % 60,
//            is24Hour = false
//        )
//
//        AlertDialog(
//            onDismissRequest = {
//                showTimePicker = false
//            },
//            title = {
//                Text("Select Start Time")
//            },
//            text = {
//                TimePicker(state = timePickerState)
//            },
//            confirmButton = {
//                TextButton(
//                    onClick = {
//                        selectedTimeMinutes =
//                            timePickerState.hour * 60 +
//                                    timePickerState.minute
//
//                        showTimePicker = false
//                    }
//                ) {
//                    Text("Select")
//                }
//            },
//            dismissButton = {
//                TextButton(
//                    onClick = {
//                        showTimePicker = false
//                    }
//                ) {
//                    Text("Cancel")
//                }
//            }
//        )
//    }
//}
//
//private fun formatDate(timestamp: Long): String {
//    return SimpleDateFormat(
//        "EEE, dd MMM yyyy",
//        Locale.getDefault()
//    ).format(Date(timestamp))
//}
//
//private fun formatEventDate(timestamp: Long): String {
//    return SimpleDateFormat(
//        "EEE, dd MMM • hh:mm a",
//        Locale.getDefault()
//    ).format(Date(timestamp))
//}
//
//private fun formatTime(totalMinutes: Int): String {
//    val hour = totalMinutes / 60
//    val minute = totalMinutes % 60
//
//    val calendar = Calendar.getInstance().apply {
//        set(Calendar.HOUR_OF_DAY, hour)
//        set(Calendar.MINUTE, minute)
//    }
//
//    return SimpleDateFormat(
//        "hh:mm a",
//        Locale.getDefault()
//    ).format(calendar.time)
//}
//
//private fun formatDuration(minutes: Int): String {
//    val hours = minutes / 60
//    val remainingMinutes = minutes % 60
//
//    return when {
//        hours == 0 -> "$minutes minutes"
//        remainingMinutes == 0 -> "$hours hour${if (hours > 1) "s" else ""}"
//        else -> "$hours hr $remainingMinutes min"
//    }
//}
//
//@Preview(showBackground = true, showSystemUi = true)
//@Composable
//private fun EventsFilterScreenPreview() {
//    val calendar = Calendar.getInstance()
//
//    fun eventAt(
//        id: String,
//        title: String,
//        daysFromNow: Int,
//        venue: String,
//        durationMinutes: Long = 120L
//    ): EventEntity {
//        val date = (calendar.clone() as Calendar).apply {
//            add(Calendar.DAY_OF_MONTH, daysFromNow)
//            set(Calendar.HOUR_OF_DAY, 15)
//            set(Calendar.MINUTE, 0)
//            set(Calendar.SECOND, 0)
//            set(Calendar.MILLISECOND, 0)
//        }
//
//        return EventEntity(
//            eventId = id,
//            eventTitle = title,
//            eventImageURL = null,
//            eventDescription = "A campus event for students.",
//            eventDate = date.timeInMillis,
//            eventStartTime = date.timeInMillis,
//            eventDuration = durationMinutes,
//            eventVenue = venue,
//            eventAddress = "Jaipur, Rajasthan",
//            eventEligiblity = "All college students",
//            eventLikes = 0,
//            isPaid = false,
//            price = 0,
//            clubId = "club-1",
//            organiserId = "organiser-1",
//            categoryId = "category-1",
//            registrationLink = null
//        )
//    }
//
//    EventsAppTheme {
//        EventsFilterScreen(
//            events = listOf(
//                eventAt(
//                    "1",
//                    "Android Development Workshop",
//                    0,
//                    "Seminar Hall"
//                ),
//                eventAt(
//                    "2",
//                    "Coding Competition",
//                    2,
//                    "Computer Lab",
//                    60L
//                ),
//                eventAt(
//                    "3",
//                    "Annual Tech Fest",
//                    8,
//                    "College Auditorium",
//                    180L
//                )
//            )
//        )
//    }
//}
@file:OptIn(ExperimentalMaterial3Api::class)

package com.example.eventsapp.feature.eventFilterScreen

import android.text.format.DateUtils.isToday
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material.icons.automirrored.filled.List
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.CalendarToday
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Event
import androidx.compose.material.icons.filled.EventAvailable
import androidx.compose.material.icons.filled.HourglassEmpty
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Schedule
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil3.compose.AsyncImage
import com.example.eventsapp.data.repository.source.local.entity.EventEntity
import com.example.eventsapp.feature.theme.BorderColor
import com.example.eventsapp.feature.theme.EventsAppTheme
import com.example.eventsapp.feature.theme.GrayBackground
import com.example.eventsapp.feature.theme.PurpleMain
import com.example.eventsapp.feature.theme.TextDark
import com.example.eventsapp.feature.theme.TextHint
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

// Status pill colours used only on this screen.
private val ClosingRed = Color(0xFFD32F2F)
private val ClosingRedBg = Color(0xFFFDECEE)
private val FreeGreen = Color(0xFF2E7D32)
private val FreeGreenBg = Color(0xFFE6F4EA)

// ======================================================================
// Route: owns the ViewModel, reacts to effects. Same shape as LoginRoute.
// ======================================================================
@Composable
fun EventFilterRoute(
    onBackClick: () -> Unit = {},
    onEventClick: (String) -> Unit = {},
    eventFilterViewModel: EventFilterViewModel = hiltViewModel()
) {
    val uiState by eventFilterViewModel.uiState.collectAsStateWithLifecycle()

    // Runs whenever uiState.effect changes, then consumes it so it only fires once.
    LaunchedEffect(uiState.effect) {
        when (val effect = uiState.effect) {
            is EventFilterUiEffect.NavigateToEventDetail -> {
                onEventClick(effect.eventId)
                eventFilterViewModel.consumeEffect()
            }

            null -> Unit
        }
    }

    EventFilterScreen(
        uiState = uiState,
        onBackClick = onBackClick,
        onFilterSelected = eventFilterViewModel::onFilterSelected,
        onDateSelected = eventFilterViewModel::onDateSelected,
        onTimeSelected = eventFilterViewModel::onTimeSelected,
        onDurationSelected = eventFilterViewModel::onDurationSelected,
        onApplyClick = eventFilterViewModel::onApplyFilters,
        onClearClick = eventFilterViewModel::onClearFilters,
        onUpdateFiltersClick = eventFilterViewModel::onEditFilters,
        onRemoveAppliedFilter = eventFilterViewModel::onRemoveAppliedFilter,
        onEventClick = eventFilterViewModel::onEventClick
    )
}

// ======================================================================
// Screen: stateless, only draws uiState and forwards user actions.
// ======================================================================
@Composable
fun EventFilterScreen(
    modifier: Modifier = Modifier,
    uiState: EventFilterUiState,
    onBackClick: () -> Unit,
    onFilterSelected: (EventSearchFilter) -> Unit,
    onDateSelected: (Long?) -> Unit,
    onTimeSelected: (Int?) -> Unit,
    onDurationSelected: (Int?) -> Unit,
    onApplyClick: () -> Unit,
    onClearClick: () -> Unit,
    onUpdateFiltersClick: () -> Unit,
    onRemoveAppliedFilter: (AppliedFilterType) -> Unit,
    onEventClick: (String) -> Unit
) {
    // Dialog visibility is pure UI state, so it lives here and survives rotation.
    var showDatePicker by rememberSaveable { mutableStateOf(false) }
    var showTimePicker by rememberSaveable { mutableStateOf(false) }

    val hasExtraCriteria = uiState.appliedDateMillis != null ||
            uiState.appliedTimeMinutes != null ||
            uiState.appliedDurationMinutes != null

    val resultsTitle = if (hasExtraCriteria) {
        "Events (${uiState.events.size})"
    } else {
        "${uiState.appliedFilter.label} (${uiState.events.size})"
    }

    Scaffold(
        modifier = modifier,
        containerColor = GrayBackground,
        topBar = {
            TopAppBar(
                title = {
                    Text(text = "Find Events", fontWeight = FontWeight.Bold)
                },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = PurpleMain,
                    titleContentColor = Color.White,
                    navigationIconContentColor = Color.White
                )
            )
        }
    ) { innerPadding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {

            // 1. Quick filter tiles (2 x 2)
            item {
                QuickFilterTiles(
                    selected = uiState.selectedFilter,
                    onTileClick = { filter ->
                        onFilterSelected(filter)
                        // "Choose Date" opens the calendar straight away.
                        if (filter == EventSearchFilter.CUSTOM) showDatePicker = true
                    }
                )
            }

            // 2. Form OR applied-filters panel
            item {
                if (uiState.isFilterFormExpanded) {
                    FilterForm(
                        uiState = uiState,
                        onDateClick = { showDatePicker = true },
                        onTimeClick = { showTimePicker = true },
                        onClearDate = { onDateSelected(null) },
                        onClearTime = { onTimeSelected(null) },
                        onDurationSelected = onDurationSelected,
                        onApplyClick = onApplyClick,
                        onClearClick = onClearClick
                    )
                } else {
                    AppliedFiltersPanel(
                        uiState = uiState,
                        onClearAllClick = onClearClick,
                        onRemove = onRemoveAppliedFilter,
                        onUpdateFiltersClick = onUpdateFiltersClick
                    )
                }
            }

            // 3. Errors (validation or loading)
            uiState.errorMessage?.let { message ->
                item {
                    Text(text = message, color = Color.Red, fontSize = 14.sp)
                }
            }

            // 4. Results
            item {
                Text(
                    text = resultsTitle,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    color = TextDark,
                    modifier = Modifier.padding(top = 4.dp)
                )
            }

            when {
                uiState.isLoading -> item {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 32.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        CircularProgressIndicator(color = PurpleMain)
                    }
                }

                uiState.events.isEmpty() -> item { EmptyResults() }

                else -> items(items = uiState.events, key = { it.eventId }) { event ->
                    EventResultCard(
                        event = event,
                        onClick = { onEventClick(event.eventId) }
                    )
                }
            }
        }
    }

    // ---------- Date picker ----------
    if (showDatePicker) {
        val datePickerState = rememberDatePickerState(
            initialSelectedDateMillis = uiState.selectedDateMillis
                ?.let(::localDayStartToPickerUtcMillis)
        )

        DatePickerDialog(
            onDismissRequest = { showDatePicker = false },
            confirmButton = {
                TextButton(
                    onClick = {
                        // The ViewModel converts the picker's UTC value to a local day.
                        onDateSelected(datePickerState.selectedDateMillis)
                        showDatePicker = false
                    }
                ) { Text("OK", color = PurpleMain, fontWeight = FontWeight.Bold) }
            },
            dismissButton = {
                TextButton(onClick = { showDatePicker = false }) {
                    Text("Cancel", color = PurpleMain)
                }
            }
        ) {
            DatePicker(
                state = datePickerState,
                title = {
                    Text(
                        text = "Select Date",
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(start = 24.dp, top = 16.dp)
                    )
                }
            )
        }
    }

    // ---------- Time picker ----------
    if (showTimePicker) {
        val initialMinutes = uiState.selectedTimeMinutes ?: minutesOfDay(System.currentTimeMillis())
        val timePickerState = rememberTimePickerState(
            initialHour = initialMinutes / 60,
            initialMinute = initialMinutes % 60,
            is24Hour = false
        )

        AlertDialog(
            onDismissRequest = { showTimePicker = false },
            title = { Text("Select Time", fontWeight = FontWeight.Bold) },
            text = { TimePicker(state = timePickerState) },
            confirmButton = {
                TextButton(
                    onClick = {
                        onTimeSelected(timePickerState.hour * 60 + timePickerState.minute)
                        showTimePicker = false
                    }
                ) { Text("OK", color = PurpleMain, fontWeight = FontWeight.Bold) }
            },
            dismissButton = {
                TextButton(onClick = { showTimePicker = false }) {
                    Text("Cancel", color = PurpleMain)
                }
            }
        )
    }
}

// ======================================================================
// Sections
// ======================================================================

@Composable
private fun QuickFilterTiles(
    selected: EventSearchFilter,
    onTileClick: (EventSearchFilter) -> Unit
) {
    Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
        EventSearchFilter.entries.chunked(2).forEach { rowFilters ->
            Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                rowFilters.forEach { filter ->
                    FilterTile(
                        label = filter.label,
                        icon = filter.icon(),
                        selected = filter == selected,
                        onClick = { onTileClick(filter) },
                        modifier = Modifier.weight(1f)
                    )
                }
            }
        }
    }
}

@Composable
private fun FilterForm(
    uiState: EventFilterUiState,
    onDateClick: () -> Unit,
    onTimeClick: () -> Unit,
    onClearDate: () -> Unit,
    onClearTime: () -> Unit,
    onDurationSelected: (Int?) -> Unit,
    onApplyClick: () -> Unit,
    onClearClick: () -> Unit
) {
    var showDurationMenu by rememberSaveable { mutableStateOf(false) }

    Column {
        SectionLabel(icon = Icons.Default.CalendarMonth, text = "Custom Date")
        FilterField(
            text = uiState.selectedDateMillis?.let(::formatDate),
            placeholder = "Select date",
            trailingIcon = Icons.Default.CalendarMonth,
            onClick = onDateClick,
            onClear = if (uiState.selectedDateMillis != null) onClearDate else null
        )

        Spacer(Modifier.height(16.dp))

        SectionLabel(icon = Icons.Default.Schedule, text = "Start Time (optional)")
        FilterField(
            text = uiState.selectedTimeMinutes?.let(::formatTimeOfDay),
            placeholder = "Select time",
            trailingIcon = Icons.Default.Schedule,
            onClick = onTimeClick,
            onClear = if (uiState.selectedTimeMinutes != null) onClearTime else null
        )

        Spacer(Modifier.height(16.dp))

        SectionLabel(icon = Icons.Default.HourglassEmpty, text = "Duration (optional)")
        Box {
            FilterField(
                text = uiState.selectedDurationMinutes?.let(::formatDuration),
                placeholder = "Select duration",
                trailingIcon = Icons.Default.KeyboardArrowDown,
                onClick = { showDurationMenu = true },
                onClear = if (uiState.selectedDurationMinutes != null) {
                    { onDurationSelected(null) }
                } else null
            )

            DropdownMenu(
                expanded = showDurationMenu,
                onDismissRequest = { showDurationMenu = false }
            ) {
                DropdownMenuItem(
                    text = { Text("Any duration") },
                    onClick = {
                        onDurationSelected(null)
                        showDurationMenu = false
                    }
                )
                EVENT_DURATION_OPTIONS_MINUTES.forEach { minutes ->
                    DropdownMenuItem(
                        text = { Text(formatDuration(minutes)) },
                        onClick = {
                            onDurationSelected(minutes)
                            showDurationMenu = false
                        }
                    )
                }
            }
        }

        Spacer(Modifier.height(20.dp))

        Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
            Button(
                onClick = onApplyClick,
                modifier = Modifier
                    .weight(1f)
                    .height(48.dp),
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.buttonColors(containerColor = PurpleMain)
            ) {
                Text("Apply Filters", color = Color.White, fontWeight = FontWeight.Bold)
            }

            OutlinedButton(
                onClick = onClearClick,
                modifier = Modifier
                    .weight(1f)
                    .height(48.dp),
                shape = RoundedCornerShape(12.dp),
                border = BorderStroke(1.dp, PurpleMain)
            ) {
                Text("Clear", color = PurpleMain, fontWeight = FontWeight.Bold)
            }
        }
    }
}

@Composable
private fun AppliedFiltersPanel(
    uiState: EventFilterUiState,
    onClearAllClick: () -> Unit,
    onRemove: (AppliedFilterType) -> Unit,
    onUpdateFiltersClick: () -> Unit
) {
    Surface(
        shape = RoundedCornerShape(12.dp),
        color = Color.White,
        border = BorderStroke(1.dp, BorderColor)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Applied Filters",
                    fontWeight = FontWeight.Bold,
                    color = TextDark,
                    modifier = Modifier.weight(1f)
                )
                Text(
                    text = "Clear All",
                    color = PurpleMain,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.SemiBold,
                    modifier = Modifier.clickable(onClick = onClearAllClick)
                )
            }

            Spacer(Modifier.height(12.dp))

            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                uiState.appliedChipTypes.forEach { type ->
                    AppliedChip(
                        icon = chipIcon(type),
                        label = chipLabel(type, uiState),
                        onRemove = { onRemove(type) }
                    )
                }
            }

            Spacer(Modifier.height(16.dp))

            Button(
                onClick = onUpdateFiltersClick,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(48.dp),
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.buttonColors(containerColor = PurpleMain)
            ) {
                Text("Update Filters", color = Color.White, fontWeight = FontWeight.Bold)
            }
        }
    }
}

@Composable
private fun EmptyResults() {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 32.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Icon(
            imageVector = Icons.Default.Event,
            contentDescription = null,
            modifier = Modifier.size(48.dp),
            tint = PurpleMain
        )
        Spacer(Modifier.height(12.dp))
        Text("No events found", fontWeight = FontWeight.SemiBold, color = TextDark)
        Text("Try changing your filters.", fontSize = 14.sp, color = TextHint)
    }
}

// ======================================================================
// Building blocks
// ======================================================================

@Composable
private fun FilterTile(
    label: String,
    icon: ImageVector,
    selected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Surface(
        onClick = onClick,
        modifier = modifier.height(56.dp),
        shape = RoundedCornerShape(12.dp),
        color = if (selected) PurpleMain else Color.White,
        border = BorderStroke(1.dp, if (selected) PurpleMain else BorderColor)
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                modifier = Modifier.size(20.dp),
                tint = if (selected) Color.White else PurpleMain
            )
            Spacer(Modifier.width(10.dp))
            Text(
                text = label,
                fontSize = 14.sp,
                fontWeight = FontWeight.SemiBold,
                maxLines = 2,
                color = if (selected) Color.White else TextDark
            )
        }
    }
}

@Composable
private fun SectionLabel(icon: ImageVector, text: String) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.padding(bottom = 8.dp)
    ) {
        Icon(icon, contentDescription = null, modifier = Modifier.size(18.dp), tint = PurpleMain)
        Spacer(Modifier.width(8.dp))
        Text(text, fontSize = 14.sp, fontWeight = FontWeight.SemiBold, color = TextDark)
    }
}

// Looks like the outlined fields in the design, but is a plain clickable box
// (an OutlinedTextField would swallow the click meant to open the picker).
@Composable
private fun FilterField(
    text: String?,
    placeholder: String,
    trailingIcon: ImageVector,
    onClick: () -> Unit,
    onClear: (() -> Unit)? = null
) {
    Surface(
        onClick = onClick,
        modifier = Modifier
            .fillMaxWidth()
            .height(52.dp),
        shape = RoundedCornerShape(12.dp),
        color = Color.White,
        border = BorderStroke(1.dp, BorderColor)
    ) {
        Row(
            modifier = Modifier.padding(start = 16.dp, end = 4.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = text ?: placeholder,
                color = if (text != null) TextDark else TextHint,
                fontSize = 15.sp,
                modifier = Modifier.weight(1f)
            )

            if (text != null && onClear != null) {
                IconButton(onClick = onClear) {
                    Icon(Icons.Default.Close, contentDescription = "Clear", tint = TextHint)
                }
            } else {
                Icon(
                    imageVector = trailingIcon,
                    contentDescription = null,
                    tint = TextHint,
                    modifier = Modifier.padding(end = 12.dp)
                )
            }
        }
    }
}

@Composable
private fun AppliedChip(icon: ImageVector, label: String, onRemove: () -> Unit) {
    Surface(
        shape = RoundedCornerShape(8.dp),
        color = PurpleMain.copy(alpha = 0.06f),
        border = BorderStroke(1.dp, BorderColor)
    ) {
        Row(
            modifier = Modifier.padding(start = 10.dp, top = 6.dp, bottom = 6.dp, end = 8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(icon, contentDescription = null, modifier = Modifier.size(16.dp), tint = PurpleMain)
            Spacer(Modifier.width(8.dp))
            Text(label, fontSize = 13.sp, color = TextDark)
            Spacer(Modifier.width(10.dp))
            Icon(
                imageVector = Icons.Default.Close,
                contentDescription = "Remove $label",
                modifier = Modifier
                    .size(16.dp)
                    .clickable(onClick = onRemove),
                tint = TextHint
            )
        }
    }
}

@Composable
private fun EventResultCard(event: EventEntity, onClick: () -> Unit) {
    val closesToday = event.registrationDeadline?.let(::isToday) == true
    val durationMinutes = (event.eventDuration / 60_000L).toInt()

    Card(
        onClick = onClick,
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Row(
            modifier = Modifier.padding(10.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Poster (falls back to an icon when the event has no image)
            Box(
                modifier = Modifier
                    .size(width = 88.dp, height = 112.dp)
                    .clip(RoundedCornerShape(12.dp))
                    .background(PurpleMain.copy(alpha = 0.10f)),
                contentAlignment = Alignment.Center
            ) {
                if (!event.eventImageURL.isNullOrBlank()) {
                    AsyncImage(
                        model = event.eventImageURL,
                        contentDescription = event.eventTitle,
                        contentScale = ContentScale.Crop,
                        modifier = Modifier.fillMaxSize()
                    )
                } else {
                    Icon(Icons.Default.Event, contentDescription = null, tint = PurpleMain)
                }
            }

            Spacer(Modifier.width(12.dp))

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = event.eventTitle,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    color = TextDark,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )

                event.clubName?.takeIf { it.isNotBlank() }?.let { clubName ->
                    Text(
                        text = clubName,
                        fontSize = 13.sp,
                        color = TextHint,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                }

                Spacer(Modifier.height(4.dp))

                InfoRow(Icons.Default.CalendarMonth, formatEventDateTime(event.eventStartTime))
                if (event.eventVenue.isNotBlank()) {
                    InfoRow(Icons.Default.LocationOn, event.eventVenue)
                }
                if (durationMinutes > 0) {
                    InfoRow(Icons.Default.HourglassEmpty, formatDuration(durationMinutes))
                }

                Spacer(Modifier.height(8.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = if (closesToday) {
                        Arrangement.SpaceBetween
                    } else {
                        Arrangement.End
                    },
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    if (closesToday) {
                        StatusPill(
                            text = "Registration closes today",
                            textColor = ClosingRed,
                            backgroundColor = ClosingRedBg,
                            modifier = Modifier.weight(1f, fill = false)
                        )
                        Spacer(Modifier.width(6.dp))
                    }

                    if (event.isPaid) {
                        StatusPill("₹${event.price}", PurpleMain, PurpleMain.copy(alpha = 0.10f))
                    } else {
                        StatusPill("Free", FreeGreen, FreeGreenBg)
                    }
                }
            }

            Icon(
                imageVector = Icons.AutoMirrored.Filled.KeyboardArrowRight,
                contentDescription = null,
                tint = PurpleMain
            )
        }
    }
}

@Composable
private fun InfoRow(icon: ImageVector, text: String) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.padding(top = 3.dp)
    ) {
        Icon(icon, contentDescription = null, modifier = Modifier.size(14.dp), tint = PurpleMain)
        Spacer(Modifier.width(6.dp))
        Text(
            text = text,
            fontSize = 12.sp,
            color = TextDark.copy(alpha = 0.75f),
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
        )
    }
}

@Composable
private fun StatusPill(
    text: String,
    textColor: Color,
    backgroundColor: Color,
    modifier: Modifier = Modifier
) {
    Text(
        text = text,
        color = textColor,
        fontSize = 11.sp,
        fontWeight = FontWeight.Medium,
        maxLines = 1,
        overflow = TextOverflow.Ellipsis,
        modifier = modifier
            .background(backgroundColor, RoundedCornerShape(6.dp))
            .padding(horizontal = 8.dp, vertical = 4.dp)
    )
}

// ======================================================================
// Small helpers
// ======================================================================

private fun EventSearchFilter.icon(): ImageVector = when (this) {
    EventSearchFilter.TODAY_EVENTS -> Icons.Default.CalendarToday
    EventSearchFilter.REGISTRATION_CLOSING_TODAY -> Icons.Default.EventAvailable
    EventSearchFilter.CUSTOM -> Icons.Default.CalendarMonth
    EventSearchFilter.ALL -> Icons.AutoMirrored.Filled.List
}

private fun chipIcon(type: AppliedFilterType): ImageVector = when (type) {
    AppliedFilterType.QUICK -> Icons.Default.CalendarToday
    AppliedFilterType.DATE -> Icons.Default.CalendarMonth
    AppliedFilterType.TIME -> Icons.Default.Schedule
    AppliedFilterType.DURATION -> Icons.Default.HourglassEmpty
}

private fun chipLabel(type: AppliedFilterType, state: EventFilterUiState): String = when (type) {
    AppliedFilterType.QUICK -> state.appliedFilter.chipLabel.orEmpty()
    AppliedFilterType.DATE -> "Date: ${state.appliedDateMillis?.let(::formatDate).orEmpty()}"
    AppliedFilterType.TIME -> "Time: ${state.appliedTimeMinutes?.let(::formatTimeOfDay).orEmpty()}"
    AppliedFilterType.DURATION -> "Duration: ${state.appliedDurationMinutes?.let(::formatDuration).orEmpty()}"
}

// 24 Oct 2026
private fun formatDate(millis: Long): String =
    SimpleDateFormat("dd MMM yyyy", Locale.getDefault()).format(Date(millis))

// 24 Oct 2026, 3:30 PM
private fun formatEventDateTime(millis: Long): String =
    SimpleDateFormat("dd MMM yyyy, h:mm a", Locale.getDefault()).format(Date(millis))

// 930 -> 3:30 PM
private fun formatTimeOfDay(totalMinutes: Int): String {
    val calendar = Calendar.getInstance().apply {
        set(Calendar.HOUR_OF_DAY, totalMinutes / 60)
        set(Calendar.MINUTE, totalMinutes % 60)
    }
    return SimpleDateFormat("h:mm a", Locale.getDefault()).format(calendar.time)
}

// 120 -> "2 hours", 90 -> "1 hr 30 min", 30 -> "30 minutes"
private fun formatDuration(minutes: Int): String {
    val hours = minutes / 60
    val rest = minutes % 60
    return when {
        hours == 0 -> "$rest minutes"
        rest == 0 -> if (hours == 1) "1 hour" else "$hours hours"
        else -> "$hours hr $rest min"
    }
}

// ======================================================================
// Previews
// ======================================================================

private fun previewEvent(
    id: String,
    title: String,
    club: String,
    venue: String,
    hour: Int,
    durationHours: Int,
    closesToday: Boolean,
    paid: Boolean
): EventEntity {
    val start = Calendar.getInstance().apply {
        set(Calendar.HOUR_OF_DAY, hour)
        set(Calendar.MINUTE, 30)
        set(Calendar.SECOND, 0)
        set(Calendar.MILLISECOND, 0)
    }.timeInMillis

    return EventEntity(
        eventId = id,
        eventTitle = title,
        eventImageURL = null,
        eventDescription = "",
        eventDate = start,
        eventStartTime = start,
        eventDuration = durationHours * 60 * 60_000L,
        eventVenue = venue,
        eventAddress = "",
        eventEligiblity = "",
        isPaid = paid,
        price = if (paid) 300 else 0,
        clubId = "",
        organiserId = "",
        categoryId = "",
        clubName = club,
        registrationDeadline = if (closesToday) start - 60 * 60_000L else null
    )
}

private val previewEvents = listOf(
    previewEvent("1", "Smart India Hackathon", "Coding Club", "Main Auditorium", 15, 2, true, false),
    previewEvent("2", "Android App Development", "Android Club", "Lab 1, CSE Block", 15, 2, true, true)
)

@Preview(showBackground = true, showSystemUi = true)
@Composable
private fun EventFilterScreenFormPreview() {
    EventsAppTheme {
        EventFilterScreen(
            uiState = EventFilterUiState(events = previewEvents, isLoading = false),
            onBackClick = {},
            onFilterSelected = {},
            onDateSelected = {},
            onTimeSelected = {},
            onDurationSelected = {},
            onApplyClick = {},
            onClearClick = {},
            onUpdateFiltersClick = {},
            onRemoveAppliedFilter = {},
            onEventClick = {}
        )
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
private fun EventFilterScreenAppliedPreview() {
    val today = System.currentTimeMillis()
    EventsAppTheme {
        EventFilterScreen(
            uiState = EventFilterUiState(
                events = previewEvents,
                isLoading = false,
                selectedFilter = EventSearchFilter.REGISTRATION_CLOSING_TODAY,
                appliedFilter = EventSearchFilter.REGISTRATION_CLOSING_TODAY,
                appliedDateMillis = startOfDay(today),
                appliedTimeMinutes = 15 * 60 + 30,
                appliedDurationMinutes = 120,
                isFilterFormExpanded = false
            ),
            onBackClick = {},
            onFilterSelected = {},
            onDateSelected = {},
            onTimeSelected = {},
            onDurationSelected = {},
            onApplyClick = {},
            onClearClick = {},
            onUpdateFiltersClick = {},
            onRemoveAppliedFilter = {},
            onEventClick = {}
        )
    }
}