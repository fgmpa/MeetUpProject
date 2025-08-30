package com.eugene.meetupproject.presentation.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.eugene.meetupproject.domain.model.Event
import com.eugene.meetupproject.domain.usecase.AddEventUseCase
import com.eugene.meetupproject.domain.usecase.ChangeEventUseCase
import com.eugene.meetupproject.domain.usecase.DeleteEventUseCase
import com.eugene.meetupproject.domain.usecase.GetAllUsersUseCase
import com.eugene.meetupproject.domain.usecase.GetEventsForDayUseCase
import com.eugene.meetupproject.domain.usecase.GetEventsForRoomUseCase
import com.eugene.meetupproject.domain.usecase.GetEventsForUserUseCase
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.time.Duration
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.Calendar
import java.util.Locale
import javax.inject.Inject

class EventViewModel @Inject constructor(
    private val getEventsForDay: GetEventsForDayUseCase,
    private val getEventsForUser: GetEventsForUserUseCase,
    private val addEvent: AddEventUseCase,
    private val updateEvent: ChangeEventUseCase,
    private val deleteEvent: DeleteEventUseCase,
    private val getAllUsers : GetAllUsersUseCase,
    private val getEventsByRoom: GetEventsForRoomUseCase
) : ViewModel() {
    private val _events = MutableLiveData<List<Event>>()
    val events: LiveData<List<Event>> = _events

    private val _error = MutableLiveData<String?>()
    val error: LiveData<String?> = _error

    private val _selectedDate = MutableLiveData<String>()
    val selectedDate: LiveData<String> = _selectedDate

    private val _users = MutableLiveData<List<Pair<String, String>>>()
    val users: LiveData<List<Pair<String, String>>> = _users
    init {
        val today = getTodayDate()
        _selectedDate.value = today
        loadEventsForDay(today)
    }


    fun loadUsers() {
        viewModelScope.launch {
            try {
                _users.postValue(getAllUsers())
            } catch (e: Exception) {
                _error.postValue("Не удалось загрузить список пользователей")
                Log.e("EventVM", "Ошибка загрузки пользователей: ${e.message}")
            }
        }
    }

    fun selectDate(date: String) {
        _selectedDate.value = date
    }

    fun loadEventsForDay(date: String) {
        viewModelScope.launch {
            val result = getEventsForDay(date)
            result.onSuccess { events ->
                _events.value = events
            }.onFailure {
                _error.postValue("Не удалось загрузить события на выбранный день")
                Log.e("EventVM", "Ошибка загрузки событий: ${it.message}")
            }
        }
    }

    fun loadEventsForUser(userId: String) {
        viewModelScope.launch {
            val result = getEventsForUser(userId)
            result.onSuccess { _events.value = it }
                .onFailure {
                    _error.postValue("Не удалось загрузить события пользователя")
                    Log.e("EventVM", "Ошибка загрузки событий пользователя: ${it.message}")
                }
        }
    }

    fun updateEventWithValidation(event: Event) {
        viewModelScope.launch {
            try {
                val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")
                val start = LocalDateTime.parse(event.dateStart, formatter)
                val end = LocalDateTime.parse(event.dateEnd, formatter)

                val duration = Duration.between(start, end).toMinutes()
                if (duration < 30) {
                    _error.postValue("Событие должно длиться не менее 30 минут")
                    return@launch
                }
                if (duration > 24 * 60) {
                    _error.postValue("Событие не может длиться более 24 часов")
                    return@launch
                }

                val roomEventsResult = getEventsByRoom(event.roomId)
                roomEventsResult.onSuccess { roomEvents ->
                    val hasOverlap = roomEvents.any { existing ->
                        if (existing.id == event.id) return@any false
                        val exStart = LocalDateTime.parse(existing.dateStart, formatter)
                        val exEnd = LocalDateTime.parse(existing.dateEnd, formatter)
                        start.isBefore(exEnd) && end.isAfter(exStart)
                    }
                    if (hasOverlap) {
                        _error.postValue("В этой комнате уже есть событие на выбранное время")
                        return@launch
                    }

                    updateEvent(event).onFailure {
                        _error.postValue("Не удалось обновить событие")
                        Log.e("EventVM", "Ошибка обновления события: ${it.message}")
                    }
                }.onFailure {
                    _error.postValue("Не удалось проверить занятость комнаты")
                    Log.e("EventVM", "Ошибка получения событий комнаты: ${it.message}")
                }

            } catch (e: Exception) {
                _error.postValue("Ошибка обработки данных события")
                Log.e("EventVM", "Exception: ${e.message}")
            }
        }
    }

    fun addEventWithValidation(
        name: String,
        dateStart: String,
        dateEnd: String,
        roomId: String,
        contributors: List<String>
    ) {
        Log.d("EventVM", "addEventWithValidation called")
        val ownerId = FirebaseAuth.getInstance().currentUser?.uid ?: run {
            _error.postValue("Пользователь не авторизован")
            return
        }

        viewModelScope.launch {
            try {
                val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")
                val start = LocalDateTime.parse(dateStart, formatter)
                val end = LocalDateTime.parse(dateEnd, formatter)

                val duration = Duration.between(start, end).toMinutes()
                if (duration < 30) {
                    _error.postValue("Событие должно длиться не менее 30 минут")
                    return@launch
                }
                if (duration > 24 * 60) {
                    _error.postValue("Событие не может длиться более 24 часов")
                    return@launch
                }

                val roomEventsResult = getEventsByRoom(roomId)
                roomEventsResult.onSuccess { roomEvents ->
                    val hasOverlap = roomEvents.any { existing ->
                        val exStart = LocalDateTime.parse(existing.dateStart, formatter)
                        val exEnd = LocalDateTime.parse(existing.dateEnd, formatter)
                        start.isBefore(exEnd) && end.isAfter(exStart)
                    }
                    if (hasOverlap) {
                        _error.postValue("В этой комнате уже есть событие на выбранное время")
                        return@launch
                    }

                    val event = Event(
                        id = "",
                        name = name,
                        dateStart = dateStart,
                        dateEnd = dateEnd,
                        contributors = contributors,
                        roomId = roomId,
                        ownerId = ownerId
                    )

                    val result = addEvent(event)
                    result.onFailure {
                        _error.postValue("Не удалось создать событие")
                        Log.e("EventVM", "Ошибка создания события: ${it.message}")
                    }

                }.onFailure {
                    _error.postValue("Не удалось проверить занятость комнаты")
                    Log.e("EventVM", "Ошибка получения событий комнаты: ${it.message}")
                }

            } catch (e: Exception) {
                _error.postValue("Ошибка обработки данных нового события")
                Log.e("EventVM", "Exception: ${e.message}")
            }
        }
    }

    fun delete(eventId: String) {
        viewModelScope.launch {
            val result = deleteEvent(eventId)
            result.onFailure {
                _error.postValue("Не удалось удалить событие")
                Log.e("EventVM", "Ошибка удаления события: ${it.message}")
            }
        }
    }

    private fun getTodayDate(): String {
        val calendar = Calendar.getInstance()
        val sdf = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        return sdf.format(calendar.time)
    }

    fun clearError() {
        _error.value = null
    }

}