package com.example.eventsapp.data.repository.source.remote.model

import java.sql.Time
import java.util.Date

data class NetworkEvent(
                        val eventId: String ,
                        var eventTitle: String,
                        var eventImageURL: String?,
                        var eventDescription: String,

                        var eventDate: Date,
                        var eventTime: Time,
                        val eventDuration: Long,

                        var eventVenue: String,
                        var eventAddress: String,

                        var eventEligiblity: String,
                        var eventLikes: Int=0,

                        var isPaid: Boolean,
                        var price: Int = 0,

                        val clubId:String,
                        val organiserId: Int,
                        val categoryId: Int
)
