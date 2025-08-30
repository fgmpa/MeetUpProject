package com.eugene.meetupproject.domain.usecase

import com.eugene.meetupproject.domain.model.Room
import com.eugene.meetupproject.domain.repository.RoomRepository
import javax.inject.Inject

class AddRoomUseCase @Inject constructor(
    private val repository: RoomRepository
){
    suspend operator fun invoke(room: Room): Result<Unit> {
        return repository.addRoom(room)
    }
}