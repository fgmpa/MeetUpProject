package com.eugene.meetupproject.domain.usecase

import com.eugene.meetupproject.domain.repository.RoomRepository
import javax.inject.Inject

class DeleteRoomUseCase @Inject constructor(
    private val repository: RoomRepository
){
    suspend operator fun invoke(roomId: String): Result<Unit> {
        return repository.deleteRoom(roomId)
    }
}