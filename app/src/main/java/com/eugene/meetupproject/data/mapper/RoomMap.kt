package com.eugene.meetupproject.data.mapper

import com.eugene.meetupproject.domain.model.Room
import com.google.firebase.firestore.DocumentSnapshot

interface RoomMap {
    fun DocumentSnapshot.toRoom(): Room {
        return Room(
            id = this.id,
            name = getString("name") ?: ""
        )
    }
}