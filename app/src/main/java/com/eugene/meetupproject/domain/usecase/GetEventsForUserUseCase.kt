package com.eugene.meetupproject.domain.usecase

import com.eugene.meetupproject.domain.model.Event
import com.eugene.meetupproject.domain.repository.EventRepository
import javax.inject.Inject

class GetEventsForUserUseCase @Inject constructor(
    private val repository: EventRepository
){
    suspend operator fun invoke(userId: String): Result<List<Event>> {
        return repository.getEventsForUser(userId)
    }
}