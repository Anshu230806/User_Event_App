package com.example.eventsapp.data.repository.source.local.dao

import androidx.lifecycle.Lifecycle
import androidx.room.Dao
import androidx.room.Query
import com.example.eventsapp.data.repository.source.local.entity.EventEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface EventDao {

    // events here is table name in EventEntity
    /*
    Flow<...> : An asynchronous stream of values provided by Kotlin Coroutines.
    It continuously emits new results whenever the underlying database table changes.
     If a task is added, updated, or deleted, the Flow instantly emits the updated list to your UI or repository.
     List: Specifies that the data stream emits a Kotlin List.
     Each item in this list is an instance of LocalTask.
     LocalTask: The specific Entity class (representing a table row) defined in your code that maps directly to the columns in your database.
     */



  //  observeAll() (Flow): Updates automatically. If data in the task table changes, the Flow emits a new list immediately.
    // getAll() (Suspend): Never updates automatically. You must call the function again to get new data

@Query("select * from events")
fun observeAllEvents(): Flow<List<EventEntity>>

@Query("select * from events")
suspend fun getAllEvents(): List<EventEntity>

// eventId is a actual column name of events table and id is parameter of fun  observeEventById
//Room automatically matches the function parameter name id with the SQL placeholder :id at compile time.
//When you call observeById("123"), Room takes the value "123" and safely injects it into the SQL query where :taskId is located.
// This prevents SQL injection attacks.

@Query("select * from events where eventId = :id")
fun observeEventById(id: Int): Flow<EventEntity>

@Query("select * from events where eventId = :id")
suspend fun getEventById(id: Int) : EventEntity?


@Query("select * from events where clubId = :id")
fun getClubEvents(id: String): Flow<List<EventEntity>>

@Query("select * from events where categoryId = :id)
fun getCategotyEvents(id: String): Flow<List<EventEntity>>

@Query("select * from events where eventTitle Like '%' || :query || '%' "  )
fun searchEvents(query: String): Flow<List<EventEntity>>






}