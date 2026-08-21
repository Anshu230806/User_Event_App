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
///**
// * Repository coordinates data between the local Room database
// * and the remote Firebase data source.
// *
// * Room is the local source of truth for the UI.
// *
// * Firebase is used when Room does not contain the required data.
//
// */
//@Singleton
//class EventsRepository @Inject constructor(
//    private val eventDao: EventDao,
//
//    private val firebaseDataSource: EventFirebaseDataSource
//) {
//
//    /**
//     * Observes events from Room.
//     *
//     * The UI/ViewModel observes this Flow.
//     * Firebase data is never directly exposed to the UI.
//     */
//    fun observeAllEvents(): Flow<List<EventEntity>> {
//
//        // Room remains the single source of truth for the UI.
//        return eventDao.observeEvents()
//    }
//
//
//    /**
//     * Initializes the event data.
//     *
//     * 1. Check Room first.
//     * 2. If Room contains data, use it.
//     * 3. If Room is empty, fetch Firebase.
//     * 4. Convert FirestoreEvent into EventEntity.
//     * 5. Save the converted data into Room.
//     * 6. Room automatically emits the new data.
//     */
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
//    /**
//     * Forces synchronization between Firebase and Room.
//     *
//     * This can later be used for pull-to-refresh.
//     */
//    suspend fun refreshEvents() {
//
//        // Retrieves the latest events from Firestore.
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








package com.example.eventsapp.data.repository.repositories

import com.example.eventsapp.data.repository.source.local.dao.EventDao
import com.example.eventsapp.data.repository.source.local.entity.EventEntity
import com.example.eventsapp.data.repository.source.remote.firestore.EventFirebaseDataSource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import javax.inject.Inject
import javax.inject.Singleton
import com.example.eventsapp.data.repository.source.toEntity

/**
 * Repository coordinates data between the local Room database
 * and the remote Firebase data source.
 *
 * Room is the local source of truth for the UI.
 *
 * Firebase is used when Room does not contain the required data.

 */
@Singleton
class EventsRepository @Inject constructor(
    private val eventDao: EventDao,

    private val firebaseDataSource: EventFirebaseDataSource
) {

    /**
     * Observes events from Room.
     *
     * The UI/ViewModel observes this Flow.
     * Firebase data is never directly exposed to the UI.
     */
    fun observeAllEvents(): Flow<List<EventEntity>> {

        // Room remains the single source of truth for the UI.
        return eventDao.observeEvents()
    }


    /**
     * Initializes the event data.
     *
     * 1. Check Room first.
     * 2. If Room contains data, use it.
     * 3. If Room is empty, fetch Firebase.
     * 4. Convert FirestoreEvent into EventEntity.
     * 5. Save the converted data into Room.
     * 6. Room automatically emits the new data.
     */
    suspend fun initializeEvents() {

        // Gets the current list from Room without continuously collecting the Flow.
        val localEvents = eventDao
            .observeEvents()
            .first()

        // Stops here when Room already contains event data.
        if (localEvents.isNotEmpty()) {
            return
        }

        // Fetches remote events from Firestore because Room is empty.
        val remoteEvents = firebaseDataSource.fetchEvents()

        // Converts every FirestoreEvent into the Room-compatible EventEntity.
        val localEntities = remoteEvents.map { firestoreEvent ->
            firestoreEvent.toEntity()
        }

        // Saves Firebase data into Room so Room becomes the source of truth.
        if (localEntities.isNotEmpty()) {
            eventDao.insertEvents(localEntities)
        }
    }


    /**
     * Forces synchronization between Firebase and Room.
     *
     * This can later be used for pull-to-refresh.
     */
    suspend fun refreshEvents() {

        // Retrieves the latest events from Firestore.
        val remoteEvents = firebaseDataSource.fetchEvents()

        // Converts Firebase models into Room entities.
        val localEntities = remoteEvents.map { firestoreEvent ->
            firestoreEvent.toEntity()
        }

        // Replaces the local data with the latest remote data.
        if (localEntities.isNotEmpty()) {
            eventDao.insertEvents(localEntities)
        }
    }
}

