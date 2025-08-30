package com.eugene.meetupproject.domain.usecase

import com.eugene.meetupproject.domain.model.Room
import com.eugene.meetupproject.domain.repository.EventRepository
import com.eugene.meetupproject.domain.repository.RoomRepository
import javax.inject.Inject

class GetAllRoomsUseCase @Inject constructor(
    private val repository: RoomRepository
) {
    suspend operator fun invoke(): Result<List<Room>> {
        return repository.getAllRooms()
    }
}