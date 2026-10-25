package com.example.eventsapp.data.remote.dto

data class AuthMeResponseDto(
    val success: Boolean,
    val data: AuthMeDataDto
)

data class AuthMeDataDto(
    val user: AuthUserDto
)

data class AuthUserDto(
    val id: String,
    val email: String,
    val role: String,
    val status: String,
    val fullName: String?,
    val emailVerifiedAt: String?
)
