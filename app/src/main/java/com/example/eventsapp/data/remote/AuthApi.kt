
package com.example.eventsapp.data.remote

import com.example.eventsapp.data.remote.dto.AuthMeResponseDto
import retrofit2.http.GET

interface AuthApi {

    // The Firebase ID token is added by the OkHttp interceptor.
    // The backend verifies it and returns the PostgreSQL user and role.
    @GET("auth/me")
    suspend fun getCurrentUser(): AuthMeResponseDto
}
