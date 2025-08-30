package com.eugene.meetupproject.presentation.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.eugene.meetupproject.domain.model.Event
import com.eugene.meetupproject.domain.model.Room
import com.eugene.meetupproject.domain.usecase.AddRoomUseCase
import com.eugene.meetupproject.domain.usecase.ChangeRoomUseCase
import com.eugene.meetupproject.domain.usecase.DeleteRoomUseCase
import com.eugene.meetupproject.domain.usecase.GetAllRoomsUseCase
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.launch
import javax.inject.Inject

class RoomViewModel @Inject constructor(
    private val addRoom: AddRoomUseCase,
    private val getAllRooms: GetAllRoomsUseCase,
    private val deleteRoom: DeleteRoomUseCase,
    private val changeRoom: ChangeRoomUseCase
): ViewModel() {

    private val _rooms = MutableLiveData<List<Room>>()
    val rooms: LiveData<List<Room>> = _rooms

    private val _error = MutableLiveData<String?>()
    val error: LiveData<String?> = _error

    init {
        loadRooms()
    }

    fun loadRooms() {
        viewModelScope.launch {
            val result = getAllRooms()
            result
                .onSuccess { _rooms.value = it }
                .onFailure { _error.value = it.message }

        }
    }

    fun add(name: String) {
        val room = Room(
            id = "",
            name = name
        )
        viewModelScope.launch {
            val result = addRoom(room)
            result.onFailure { _error.value = it.message }
        }
    }
    fun delete(roomId: String) {
        viewModelScope.launch {
            val result = deleteRoom(roomId)
            result.onFailure { _error.value = it.message }
        }
    }
    fun change(room: Room) {
        viewModelScope.launch {
            val result = changeRoom(room)
            result.onFailure { _error.value = it.message }
        }
    }

}