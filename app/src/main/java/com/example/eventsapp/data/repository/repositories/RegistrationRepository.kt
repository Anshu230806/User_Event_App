package com.example.eventsapp.data.repository.repositories

import com.example.eventsapp.data.remote.RegistrationApi
import jakarta.inject.Inject
import jakarta.inject.Singleton

@Singleton
class RegistrationRepository @Inject constructor(
    private val registrationApi: RegistrationApi
) {

    suspend fun registerForEvent(eventId: String): Result<String> {
        return try {
            val response = registrationApi.registerForEvent(eventId)

            if (response.success) {
                Result.success(
                    response.message ?: "Successfully registered for the event"
                )
            } else {
                Result.failure(
                    Exception(response.message ?: "Registration failed")
                )
            }

        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
