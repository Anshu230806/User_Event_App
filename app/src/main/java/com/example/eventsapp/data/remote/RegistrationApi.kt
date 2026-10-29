package com.example.eventsapp.data.remote

import com.example.eventsapp.data.remote.dto.RegistrationCreateRequest
import com.example.eventsapp.data.remote.dto.GenericActionResponse
import retrofit2.http.Body
import retrofit2.http.POST
import retrofit2.http.Path

interface RegistrationApi {
    @POST("events/{eventId}/registrations")
    suspend fun registerForEvent(
        @Path("eventId") eventId: String,
        @Body request: RegistrationCreateRequest
    ): GenericActionResponse
}
