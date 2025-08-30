package com.eugene.meetupproject.domain.repository

import com.eugene.meetupproject.domain.model.Room

interface RoomRepository {
    suspend fun addRoom(room: Room): Result<Unit>
    suspend fun deleteRoom(roomId: String): Result<Unit>
    suspend fun changeRoom(room: Room): Result<Unit>
    suspend fun getAllRooms(): Result<List<Room>>
}