package com.example.eventsapp.data.repository.source.remote.model

import java.sql.Time
import java.util.Date

data class FirestoreEvent(
    val eventId: String = "",
    var eventTitle: String = "",
    var eventImageURL: String? = null,
    var eventDescription: String = "",
    var eventDate: Date? = null,
    val eventStartTime: Long = 0L,
    val eventDuration: Long = 0L,  // Can be Long or Timestamp
    var eventVenue: String = "",
    var eventAddress: String = "",
    var eventEligiblity: String = "",
    var eventLikes: Int = 0,
    var isPaid: Boolean = false,
    var price: Int = 0,
    val clubId: String = "",
    val organiserId: String = "",
    val categoryId: String = "",
    val registrationLink: String? = null
)



