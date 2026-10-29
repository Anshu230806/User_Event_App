//package com.example.eventsapp.data.repository.repositories
//
//import com.example.eventsapp.data.repository.source.local.dao.EventDao
//import com.example.eventsapp.data.repository.source.local.entity.EventEntity
//import com.example.eventsapp.data.repository.source.remote.firestore.EventFirebaseDataSource
//import kotlinx.coroutines.flow.Flow
//import kotlinx.coroutines.flow.first
//import javax.inject.Inject
//import javax.inject.Singleton
//import com.example.eventsapp.data.repository.source.toEntity
//
//@Singleton
//class EventsRepository @Inject constructor(
//    private val eventDao: EventDao,
//    private val firebaseDataSource: EventFirebaseDataSource
//) {
//
//    fun observeAllEvents(): Flow<List<EventEntity>> {
//        return eventDao.observeEvents()
//    }
//
//    suspend fun initializeEvents() {
//
//        // Gets the current list from Room without continuously collecting the Flow.
//        val localEvents = eventDao
//            .observeEvents()
//            .first()
//
//        // Stops here when Room already contains event data.
//        if (localEvents.isNotEmpty()) {
//            return
//        }
//
//        // Fetches remote events from Firestore because Room is empty.
//        val remoteEvents = firebaseDataSource.fetchEvents()
//
//        // Converts every FirestoreEvent into the Room-compatible EventEntity.
//        val localEntities = remoteEvents.map { firestoreEvent ->
//            firestoreEvent.toEntity()
//        }
//
//        // Saves Firebase data into Room so Room becomes the source of truth.
//        if (localEntities.isNotEmpty()) {
//            eventDao.insertEvents(localEntities)
//        }
//    }
//
//
//    //Forces synchronization between Firebase and Room.
//     // This can later be used for pull-to-refresh.
//
//    suspend fun refreshEvents() {
//
//        val remoteEvents = firebaseDataSource.fetchEvents()
//
//        // Converts Firebase models into Room entities.
//        val localEntities = remoteEvents.map { firestoreEvent ->
//            firestoreEvent.toEntity()
//        }
//
//        // Replaces the local data with the latest remote data.
//        if (localEntities.isNotEmpty()) {
//            eventDao.insertEvents(localEntities)
//        }
//    }
//}
//

package com.example.eventsapp.data.repository.repositories

import com.example.eventsapp.data.remote.EventApi
import com.example.eventsapp.data.repository.source.local.dao.EventDao
import com.example.eventsapp.data.repository.source.local.entity.EventEntity
import com.example.eventsapp.data.repository.source.toEntity
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class EventsRepository @Inject constructor(
    private val eventDao: EventDao,
    private val eventApi: EventApi
) {

    // The UI continues observing Room, not Retrofit directly.
    fun observeAllEvents(): Flow<List<EventEntity>> {
        return eventDao.observeEvents()
    }

    // Fetches events from Node.js and refreshes the local cache.
    suspend fun refreshEvents() {
        val all = mutableListOf<EventEntity>()
        var page = 1
        do {
            val response = eventApi.getEvents(page = page, limit = 50)
            check(response.success) {
                "The server returned an unsuccessful response"
            }
            all += response.data.events.map { event ->
                event.toEntity()
            }
            page++
        } while (response.pagination.hasNextPage)

        // Room emits the updated list automatically to the UI.
        eventDao.replaceEvents(all)
    }

    // Performs the initial remote synchronization.
    suspend fun initializeEvents() {
        refreshEvents()
    }
}

