package com.eugene.meetupproject.domain.usecase

import com.eugene.meetupproject.domain.repository.RoomRepository
import com.eugene.meetupproject.domain.repository.UserRepository
import javax.inject.Inject

class GetAllUsersUseCase @Inject constructor(
    private val repository: UserRepository
) {
    suspend operator fun invoke(): List<Pair<String, String>> {
        return repository.getAllUsers()
    }
}