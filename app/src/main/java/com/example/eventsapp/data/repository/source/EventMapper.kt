//package com.example.eventsapp.data.repository.source
//
//
//import com.example.eventsapp.data.repository.source.local.entity.EventEntity
//import com.example.eventsapp.data.repository.source.remote.model.FirestoreEvent
//
////Converts a FirestoreEvent remote model into an EventEntity
// //that can be stored in the Room database.
//
//import com.google.firebase.Timestamp
//
//fun FirestoreEvent.toEntity(): EventEntity {
//
//    return EventEntity(
//        eventId = eventId,
//        eventTitle = eventTitle,
//        eventImageURL = eventImageURL,
//        eventDescription = eventDescription,
//
//        // Converts java.util.Date into epoch milliseconds for Room.
//        eventDate = eventDate?.time ?: 0L,
//
//        eventStartTime = eventStartTime,
//        eventDuration = eventDuration,
//        eventVenue = eventVenue,
//        eventAddress = eventAddress,
//        eventEligiblity = eventEligiblity,
//        eventLikes = eventLikes,
//        isPaid = isPaid,
//        price = price,
//        clubId = clubId,
//        organiserId = organiserId,
//        categoryId = categoryId,
//        registrationLink = registrationLink
//    )
//}

package com.example.eventsapp.data.repository.source

import android.os.Build
import com.example.eventsapp.data.remote.dto.EventDto
import com.example.eventsapp.data.repository.source.local.entity.EventEntity
import java.text.SimpleDateFormat
import java.time.OffsetDateTime
import java.util.Locale
import java.util.TimeZone

private fun parseIsoToMillis(dateString: String?): Long? {
    if (dateString.isNullOrBlank()) return null
    return try {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            OffsetDateTime.parse(dateString).toInstant().toEpochMilli()
        } else {
            val format = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.US)
            format.timeZone = TimeZone.getTimeZone("UTC")
            format.parse(dateString)?.time
        }
    } catch (_: Exception) {
        null
    }
}

// Converts the backend DTO into the existing Room entity.
fun EventDto.toEntity(): EventEntity {
    val startMillis = parseIsoToMillis(startsAt) ?: 0L
    val endMillis = parseIsoToMillis(endsAt)

    return EventEntity(
        eventId = id,
        eventTitle = title,
        eventImageURL = posterUrl,
        eventDescription = description.orEmpty(),

        eventDate = startMillis,
        eventStartTime = startMillis,
        eventDuration = endMillis?.minus(startMillis) ?: 0L,

        eventVenue = venue.orEmpty(),
        eventAddress = "",
        eventEligiblity = eligibility.orEmpty(),
        eventLikes = likesCount,

        // The current API model does not provide pricing information.
        isPaid = false,
        price = 0,

        clubId = clubId.orEmpty(),
        organiserId = organizerId.orEmpty(),
        categoryId = "",
        registrationLink = null,
        clubName = clubName,
        registrationDeadline = parseIsoToMillis(registrationDeadline),
    )
}
