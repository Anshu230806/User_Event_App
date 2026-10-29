package com.example.eventsapp.data.remote

import com.example.eventsapp.data.remote.dto.ClubsResponseDto
import retrofit2.http.GET

interface ClubApi {
    @GET("clubs")
    suspend fun getClubs(): ClubsResponseDto
}
