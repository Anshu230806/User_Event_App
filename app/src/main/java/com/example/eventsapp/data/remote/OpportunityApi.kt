package com.example.eventsapp.data.remote

import com.example.eventsapp.data.remote.dto.GenericActionResponse
import com.example.eventsapp.data.remote.dto.OpportunitiesResponseDto
import com.example.eventsapp.data.remote.dto.OpportunityApplicationRequest
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Body

interface OpportunityApi {
    @GET("opportunities")
    suspend fun getOpportunities(): OpportunitiesResponseDto

    @POST("opportunities/{opportunityId}/applications")
    suspend fun apply(
        @Path("opportunityId") opportunityId: String,
        @Body request: OpportunityApplicationRequest
    ): GenericActionResponse
}
