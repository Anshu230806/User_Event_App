package com.example.eventsapp.data.repository.source.remote.firestore

import com.example.eventsapp.data.repository.source.remote.model.NetworkEvent

interface NetworkEventDataSource {

// Asynchronously fetches a list of events from a remote API and returns them as NetworkEvent data objects.
    suspend fun loadEvents() : List<NetworkEvent>


}