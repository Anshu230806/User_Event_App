package com.example.eventsapp.data.repository.source.local.dao

import androidx.room.Dao
import androidx.room.Query
import com.example.eventsapp.data.repository.source.local.entity.UserEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface UserDao {

@Query("select * from users where userId = :id")
fun observeProfile(id: Int): Flow<UserEntity>

@Query("select * from users where userId= :id")
suspend fun getProfile(id : Int): UserEntity?

}