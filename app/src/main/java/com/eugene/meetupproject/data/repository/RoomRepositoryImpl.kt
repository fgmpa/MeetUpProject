package com.eugene.meetupproject.data.repository


import com.eugene.meetupproject.domain.model.Room
import com.eugene.meetupproject.domain.repository.RoomRepository
import com.google.firebase.firestore.FirebaseFirestore
import javax.inject.Inject
import kotlinx.coroutines.tasks.await
import com.eugene.meetupproject.data.mapper.RoomMap

class RoomRepositoryImpl @Inject constructor(
    private val firestore: FirebaseFirestore
): RoomRepository, RoomMap {
    override suspend fun addRoom(room: Room): Result<Unit> = try {
        val docId = firestore.collection("rooms").document().id
        val newRoom = room.copy(id = docId)
        firestore.collection("rooms").document(docId).set(newRoom).await()
        Result.success(Unit)
    } catch (e: Exception){
        Result.failure(e)
    }

    override suspend fun changeRoom(room: Room): Result<Unit> = try {
        firestore.collection("rooms").document(room.id).set(room).await()
        Result.success(Unit)
    } catch (e: Exception){
        Result.failure(e)
    }


    override suspend fun deleteRoom(roomId: String): Result<Unit> = try {
        firestore.collection("rooms").document(roomId).delete().await()
        Result.success(Unit)
    } catch (e: Exception){
        Result.failure(e)
    }

    override suspend fun getAllRooms(): Result<List<Room>> = try {
        val snapshot = firestore.collection("rooms").get().await()
        val rooms = snapshot.documents.map { it.toRoom() }
        Result.success(rooms)
    } catch (e: Exception) {
        Result.failure(e)
    }

}