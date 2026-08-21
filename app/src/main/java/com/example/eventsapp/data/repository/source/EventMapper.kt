package com.example.eventsapp.data.repository.source


import com.example.eventsapp.data.repository.source.local.entity.EventEntity
import com.example.eventsapp.data.repository.source.remote.model.FirestoreEvent

/**
 * Converts a FirestoreEvent remote model into an EventEntity
 * that can be stored in the Room database.
 *
 * Firestore:
 * Date/Time -> Java Date/Time
 *
 * Room:
 * Date/Time -> Long (epoch milliseconds)
 */

import com.google.firebase.Timestamp

fun FirestoreEvent.toEntity(): EventEntity {

    return EventEntity(
        eventId = eventId,
        eventTitle = eventTitle,
        eventImageURL = eventImageURL,
        eventDescription = eventDescription,

        // Converts java.util.Date into epoch milliseconds for Room.
        eventDate = eventDate?.time ?: 0L,

        eventStartTime = eventStartTime,
        eventDuration = eventDuration,
        eventVenue = eventVenue,
        eventAddress = eventAddress,
        eventEligiblity = eventEligiblity,
        eventLikes = eventLikes,
        isPaid = isPaid,
        price = price,
        clubId = clubId,
        organiserId = organiserId,
        categoryId = categoryId,
        registrationLink = registrationLink
    )
}
