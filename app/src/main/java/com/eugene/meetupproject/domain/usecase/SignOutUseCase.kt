package com.eugene.meetupproject.domain.usecase

import com.eugene.meetupproject.domain.model.User
import com.eugene.meetupproject.domain.repository.AuthRepository
import javax.inject.Inject

class SignOutUseCase @Inject constructor(
    private val repository: AuthRepository)
{
    suspend operator fun invoke(): Result<Unit?> {
        return repository.signOut()
    }
}