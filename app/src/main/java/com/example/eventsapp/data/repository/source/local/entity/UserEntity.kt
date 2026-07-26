package com.example.eventsapp.data.repository.source.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity(tableName =  "Users")
data class UserEntity(
    @PrimaryKey(autoGenerate = true) val userId :String,

)
