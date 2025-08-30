package com.eugene.meetupproject.domain.repository

import com.eugene.meetupproject.domain.model.User

interface AuthRepository {
    suspend fun signIn(login: String, password: String): Result<User?>
    suspend fun signOut(): Result<Unit>
}