package com.example.eventsapp.data.repository.source.local.entity

import android.accessibilityservice.GestureDescription
import androidx.room.Entity
import androidx.room.PrimaryKey
import  java.util.Date
import java.sql.Time

// use COIL ( coroutine image loader )  for image   ( make sure internet permissions are given to the app)
//?  Indicates the variable can explicitly accept a null value.
// ?: "Guest"   elvis operator used to provide the default value in case value is null
// ?.length    safe call operator , only performs action if variable is not null

@Entity(tableName = "events")
data class EventEntity(

    @PrimaryKey(autoGenerate = true) val eventId: String ,
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
