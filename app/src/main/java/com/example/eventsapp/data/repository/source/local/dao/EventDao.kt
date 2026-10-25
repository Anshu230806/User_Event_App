package com.example.eventsapp.data.repository.source.local.dao

import androidx.lifecycle.Lifecycle
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import com.example.eventsapp.data.repository.source.local.entity.EventEntity
import kotlinx.coroutines.flow.Flow


@Dao
interface EventDao {

     //Observes all locally stored events.
     // Room automatically emits a new list whenever the table changes.
    @Query("SELECT * FROM events")
    fun observeEvents(): Flow<List<EventEntity>>


    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertEvents(events: List<EventEntity>)


    @Query("DELETE FROM events")
    suspend fun deleteAllEvents()


    @Query("SELECT COUNT(*) FROM events")
    suspend fun getEventCount(): Int

    @Transaction
    suspend fun replaceEvents(events: List<EventEntity>) {
        deleteAllEvents()
        insertEvents(events)
    }
}

