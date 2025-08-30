package com.eugene.meetupproject.domain.usecase

import com.eugene.meetupproject.domain.model.User
import com.eugene.meetupproject.domain.repository.AuthRepository
import javax.inject.Inject

class SignInUseCase @Inject constructor(
    private val repository: AuthRepository
) {
    suspend operator fun invoke(login: String, password: String): Result<User?> {
        return repository.signIn(login, password)
    }
}