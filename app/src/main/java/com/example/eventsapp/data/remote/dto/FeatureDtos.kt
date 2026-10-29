package com.example.eventsapp.data.remote.dto

data class RegistrationCreateRequest(
    val fullName: String,
    val email: String,
    val phone: String? = null,
    val collegeName: String? = null,
    val course: String? = null,
    val additionalDetails: String? = null
)

data class OpportunityApplicationRequest(
    val fullName: String,
    val email: String,
    val phone: String? = null,
    val resumeUrl: String? = null,
    val coverLetter: String? = null
)

data class GenericActionResponse(
    val success: Boolean,
    val message: String? = null,
    val data: Map<String, Any?>? = null
)

data class OpportunitiesResponseDto(
    val success: Boolean,
    val data: OpportunitiesDataDto? = null
)

data class OpportunitiesDataDto(
    val opportunities: List<OpportunityDto> = emptyList()
)

data class OpportunityDto(
    val id: String,
    val title: String,
    val organization: String? = null,
    val description: String? = null,
    val type: String? = null,
    val deadline: String? = null
)

data class ClubsResponseDto(
    val success: Boolean,
    val data: ClubsDataDto? = null
)

data class ClubsDataDto(
    val clubs: List<ClubDto> = emptyList()
)

data class ClubDto(
    val id: String,
    val name: String,
    val description: String? = null,
    val logoUrl: String? = null
)
