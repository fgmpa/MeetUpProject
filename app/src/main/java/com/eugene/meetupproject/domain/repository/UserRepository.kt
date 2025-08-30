package com.eugene.meetupproject.domain.repository

interface UserRepository {
    suspend fun getAllUsers(): List<Pair<String, String>>
}