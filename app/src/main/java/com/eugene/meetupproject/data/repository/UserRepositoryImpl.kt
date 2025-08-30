package com.eugene.meetupproject.data.repository


import com.eugene.meetupproject.domain.repository.UserRepository
import com.google.firebase.firestore.FirebaseFirestore
import javax.inject.Inject
import kotlinx.coroutines.tasks.await

class UserRepositoryImpl @Inject constructor(
    private val firestore: FirebaseFirestore
): UserRepository{
    override suspend fun getAllUsers(): List<Pair<String, String>> {
        val snapshot = firestore.collection("users").get().await()
        return snapshot.documents.map { it.id to (it.getString("login") ?: "") }
    }
}