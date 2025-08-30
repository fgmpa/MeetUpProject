package com.eugene.meetupproject.presentation.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.eugene.meetupproject.domain.model.User
import com.eugene.meetupproject.domain.usecase.SignInUseCase
import com.eugene.meetupproject.domain.usecase.SignOutUseCase
import kotlinx.coroutines.launch
import javax.inject.Inject

class AuthViewModel @Inject constructor(
    private val signOutUseCase: SignOutUseCase,
    private val signInUseCase: SignInUseCase
) : ViewModel() {

    private val _userState = MutableLiveData<User?>()
    val userState: LiveData<User?> = _userState

    private val _error = MutableLiveData<String?>()
    val error: LiveData<String?> = _error

    fun signIn(login: String, password: String) {
        viewModelScope.launch {
            val result = signInUseCase(login, password)
            result.onSuccess { user ->
                _userState.value = user
            }.onFailure { e ->
                _error.value = e.message
            }
        }
    }

    fun signOut() {
        viewModelScope.launch {
            val result = signOutUseCase()
            result.onSuccess {
                _userState.value = null
            }.onFailure { e ->
                _error.value = e.message
            }
        }
    }

}