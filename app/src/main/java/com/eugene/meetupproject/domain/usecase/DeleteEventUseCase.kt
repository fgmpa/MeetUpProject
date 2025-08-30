package com.eugene.meetupproject.domain.usecase

import com.eugene.meetupproject.domain.model.Event
import com.eugene.meetupproject.domain.repository.EventRepository
import javax.inject.Inject

class DeleteEventUseCase @Inject constructor(
    private val repository: EventRepository
){
    suspend operator fun invoke(eventId: String): Result<Unit> {
        return repository.deleteEvent(eventId)
    }
}