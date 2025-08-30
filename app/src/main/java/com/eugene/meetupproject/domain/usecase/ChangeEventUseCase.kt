package com.eugene.meetupproject.domain.usecase

import com.eugene.meetupproject.domain.model.Event
import com.eugene.meetupproject.domain.repository.EventRepository
import javax.inject.Inject

class ChangeEventUseCase @Inject constructor(
    private val repository: EventRepository
){
    suspend operator fun invoke(event: Event): Result<Unit> {
        return repository.changeEvent(event)
    }
}