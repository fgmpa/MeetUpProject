package com.eugene.meetupproject.data.repository

import com.eugene.meetupproject.data.mapper.EventMap
import com.eugene.meetupproject.domain.model.Event
import com.eugene.meetupproject.domain.repository.EventRepository
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Source
import kotlinx.coroutines.tasks.await
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import javax.inject.Inject

class EventRepositoryImpl @Inject constructor(
    private val firestore: FirebaseFirestore
): EventRepository, EventMap {
    override suspend fun addEvent(event: Event): Result<Unit> = try {
        val docId = firestore.collection("events").document().id
        val newEvent = event.copy(id = docId)
        firestore.collection("events").document(docId).set(newEvent).await()
            Result.success(Unit)
        } catch (e: Exception){
            Result.failure(e)
        }


    override suspend fun changeEvent(event: Event): Result<Unit> = try {
        firestore.collection("events").document(event.id).set(event).await()
        Result.success(Unit)
    } catch (e: Exception){
        Result.failure(e)
    }

    override suspend fun deleteEvent(eventId: String): Result<Unit> = try {
        firestore.collection("events").document(eventId).delete().await()
        Result.success(Unit)
    } catch (e: Exception){
        Result.failure(e)
    }

    override suspend fun getEventsForDay(date: String): Result<List<Event>> {
        return try {
            // сначала пробуем взять из кэша
            val snapshot = firestore.collection("events").get(Source.CACHE).await()

            val allEvents = snapshot.documents.map { it.toEvent() }
            val selectedDate = LocalDate.parse(date, DateTimeFormatter.ofPattern("yyyy-MM-dd"))

            val events = allEvents.filter { event ->
                val startDateTime = LocalDateTime.parse(event.dateStart, DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"))
                val endDateTime = LocalDateTime.parse(event.dateEnd, DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"))
                val startDate = startDateTime.toLocalDate()
                val endDate = endDateTime.toLocalDate()
                (selectedDate.isEqual(startDate) || selectedDate.isAfter(startDate)) &&
                        (selectedDate.isEqual(endDate) || selectedDate.isBefore(endDate))
            }

            Result.success(events)
        } catch (e: Exception) {
            try {
                val snapshot = firestore.collection("events").get(Source.SERVER).await()
                val allEvents = snapshot.documents.map { it.toEvent() }
                Result.success(allEvents)
            } catch (ex: Exception) {
                Result.failure(ex)
            }
        }
    }
    override suspend fun getEventsForUser(userId: String): Result<List<Event>> {
        return try {
            val userDoc = firestore.collection("users")
                .document(userId)
                .get().await()

            val userName = userDoc.getString("login")
                ?: return Result.failure(Exception("User name not found"))

            val eventsSnapshot = firestore.collection("events")
                .whereArrayContains("contributors", userName)
                .get().await()

            val events = eventsSnapshot.documents.map { it.toEvent() }
            Result.success(events)

        } catch (e: Exception) {
            Result.failure(e)
        }
    }
    override suspend fun getRoomIdByName(name: String): String? {
        val snapshot = firestore.collection("rooms")
            .whereEqualTo("name", name)
            .get()
            .await()

        return snapshot.documents.firstOrNull()?.id
    }
    override suspend fun getEventsForRoom(roomId: String): Result<List<Event>> = try {
        val snapshot = firestore.collection("events")
            .whereEqualTo("roomId", roomId)
            .get()
            .await()
        val event =snapshot.documents.map { it.toEvent() }
        Result.success(event)

    } catch (e: Exception) {
        Result.failure(e)
    }
}
