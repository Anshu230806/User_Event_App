package com.example.eventsapp.data.repository.source.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "clubs")
data class ClubEntity(
    @PrimaryKey val clubId :String ,
    val clubName: String,
    val clubLogoUrl:String?= null,
    val description: String
)
