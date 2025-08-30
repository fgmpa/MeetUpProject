package com.eugene.meetupproject.domain.usecase

import com.eugene.meetupproject.domain.model.Event
import com.eugene.meetupproject.domain.repository.EventRepository
import javax.inject.Inject

class GetEventsForDayUseCase @Inject constructor(
    private val repository: EventRepository
){
    suspend operator fun invoke(date: String): Result<List<Event>> {
        return repository.getEventsForDay(date)
    }
}