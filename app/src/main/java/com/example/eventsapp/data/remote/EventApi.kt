package com.example.eventsapp.data.remote

import com.example.eventsapp.data.remote.dto.EventsResponseDto
import retrofit2.http.GET

interface EventApi {

    @GET("events")
    suspend fun getEvents(): EventsResponseDto
}
