package com.example.eventsapp.data.repository.source.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "Users")
data class UserEntity(
    // PostgreSQL application user UUID, not the Firebase UID.
    @PrimaryKey
    val userId: String,

    val email: String? = null,
    val role: String? = null,
    val fullName: String? = null
)



