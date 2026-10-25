
package com.example.eventsapp.data.repository.source.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.eventsapp.data.repository.source.local.entity.UserEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface UserDao {

    // The UI observes the local application-user record.
    @Query("SELECT * FROM Users WHERE userId = :id")
    fun observeProfile(id: String): Flow<UserEntity>

    // Reads the current cached application-user record once.
    @Query("SELECT * FROM Users WHERE userId = :id")
    suspend fun getProfile(id: String): UserEntity?

    // Saves or replaces the cached application-user record.
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsertProfile(user: UserEntity)

    // Clears the cached account after logout, if desired by your session policy.
    @Query("DELETE FROM Users WHERE userId = :id")
    suspend fun deleteProfile(id: String)
}
