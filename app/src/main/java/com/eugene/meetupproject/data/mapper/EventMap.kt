package com.eugene.meetupproject.data.mapper

import com.eugene.meetupproject.domain.model.Event
import com.google.firebase.firestore.DocumentSnapshot

interface EventMap {
     fun DocumentSnapshot.toEvent(): Event {
        return Event(
            id = this.id,
            name = getString("name") ?: "",
            dateStart = getString("dateStart") ?: "",
            dateEnd = getString("dateEnd") ?: "",
            contributors = (get("contributors") as? List<*>) ?.filterIsInstance<String>() ?: emptyList(),
            roomId = getString("roomId") ?: "",
            ownerId = getString("ownerId") ?: ""
        )
    }
}