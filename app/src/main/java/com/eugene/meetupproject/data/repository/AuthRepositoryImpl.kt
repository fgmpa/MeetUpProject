package com.eugene.meetupproject.data.repository

import android.app.Application
import com.eugene.meetupproject.domain.model.User
import com.eugene.meetupproject.domain.repository.AuthRepository
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class AuthRepositoryImpl @Inject constructor(
    private val auth: FirebaseAuth,
    private val firestore: FirebaseFirestore
): AuthRepository {

    override suspend fun signIn(login: String, password: String): Result<User?> {
        return try {
            val result = auth.signInWithEmailAndPassword(login, password).await()
            val firebaseUser = result.user
            if (firebaseUser != null) {
                val userEntity = User(
                    id = firebaseUser.uid,
                    login = firebaseUser.email.toString(),
                    password = password
                )
                val docRef = firestore.collection("users").document(firebaseUser.uid)
                val snapshot = docRef.get().await()
                if (!snapshot.exists()) {
                    docRef.set(userEntity).await()
                }
                Result.success(User(firebaseUser.uid, firebaseUser.email ?: login, password))
            } else {
                Result.success(null)
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun signOut(): Result<Unit> {
        return try {
            auth.signOut()
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
