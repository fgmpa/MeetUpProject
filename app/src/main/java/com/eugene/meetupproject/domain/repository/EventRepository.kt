package com.eugene.meetupproject.domain.repository

import com.eugene.meetupproject.domain.model.Event


interface EventRepository {
    suspend fun getEventsForDay(date: String): Result<List<Event>>
    suspend fun getEventsForUser(userId: String): Result<List<Event>>
    suspend fun deleteEvent(eventId: String): Result<Unit>
    suspend fun changeEvent(event: Event): Result<Unit>
    suspend fun addEvent(event: Event): Result<Unit>
    suspend fun getRoomIdByName(name: String): String?
    suspend fun getEventsForRoom(roomId: String): Result<List<Event>>
}