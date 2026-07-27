package com.example.eventsapp.data.repository.source.local.dao

import androidx.room.Dao
import androidx.room.Query
import com.example.eventsapp.data.repository.source.local.entity.UserEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface UserDao {

@Query("select * from Users where userId = :id")
fun observeProfile(id: String): Flow<UserEntity>

@Query("select * from Users where userId = :id")
suspend fun getProfile(id: String): UserEntity?

}