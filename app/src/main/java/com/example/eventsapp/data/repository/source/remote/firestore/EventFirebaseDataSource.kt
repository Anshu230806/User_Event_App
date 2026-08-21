package com.example.eventsapp.data.repository.source.remote.firestore

import com.example.eventsapp.data.repository.source.remote.model.FirestoreEvent
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Handles all direct communication with Firebase Firestore
 * for event-related operations.
 *
 * Repository should use this class instead of accessing Firestore directly.
 */

@Singleton
class EventFirebaseDataSource @Inject constructor(
    private val firestore: FirebaseFirestore
) {

    /**
     * Retrieves all events from the Firestore events collection.
     */
    suspend fun fetchEvents(): List<FirestoreEvent> {
        return try {
            val snapshot = firestore
                .collection("users")
                .get()
                .await()

            snapshot.documents.mapNotNull { document ->
                try {
                    document.toObject(FirestoreEvent::class.java)?.copy(
                        eventId = document.id
                    )
                } catch (e: Exception) {
                    null
                }
            }
        } catch (e: Exception) {
            emptyList()
        }
    }
}

