package com.eugene.meetupproject.domain.usecase

import com.eugene.meetupproject.domain.model.Event
import com.eugene.meetupproject.domain.model.Room
import com.eugene.meetupproject.domain.repository.EventRepository
import javax.inject.Inject

class GetEventsForRoomUseCase @Inject constructor(
    private val repository: EventRepository
){
    suspend operator fun invoke(roomId: String): Result<List<Event>> {
        return repository.getEventsForRoom(roomId)
    }
}