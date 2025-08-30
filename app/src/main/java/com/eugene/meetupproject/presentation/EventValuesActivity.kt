package com.eugene.meetupproject.presentation

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import android.widget.Button
import android.widget.EditText
import android.widget.MultiAutoCompleteTextView
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.ViewModelProvider
import com.eugene.meetupproject.R
import com.eugene.meetupproject.di.MyApp
import com.eugene.meetupproject.domain.model.Event
import com.eugene.meetupproject.presentation.viewmodel.EventViewModel
import com.eugene.meetupproject.presentation.viewmodel.RoomViewModel
import com.google.firebase.auth.FirebaseAuth
import java.util.Calendar
import javax.inject.Inject

class EventValuesActivity: AppCompatActivity() {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    private val viewModelEvent: EventViewModel by viewModels { viewModelFactory }
    private val viewModelRoom: RoomViewModel by viewModels { viewModelFactory }

    private var curEvent: Event? = null
    private val selectedUsers = mutableListOf<String>()

    override fun onCreate(savedInstanceState: Bundle?) {
        (application as MyApp).appComponent.inject(this)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_event_values)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.root)) { view, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            view.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val eventNameEdit = findViewById<EditText>(R.id.event_name_edit)
        val eventDateStartEdit = findViewById<EditText>(R.id.event_date_start_edit)
        val eventDateEndEdit = findViewById<EditText>(R.id.event_date_end_edit)
        val roomAutoComplete = findViewById<AutoCompleteTextView>(R.id.roomAutoComplete)
        val userAutoComplete = findViewById<MultiAutoCompleteTextView>(R.id.contrAutoComplete)
        val addButton = findViewById<Button>(R.id.event_save_button)
        val delButton = findViewById<Button>(R.id.event_delete_button)

        val eventId = intent.getStringExtra("eventId")
        viewModelEvent.error.observe(this) { message ->
            message?.let {
                Log.d("LiveDataDebug", "Error value: $message")
                Toast.makeText(this, it, Toast.LENGTH_LONG).show()
            }
        }
       // viewModelEvent.clearError()
        if (eventId != null) {
            delButton.visibility = View.VISIBLE
            viewModelEvent.events.observe(this) { events ->
                curEvent = events.find { it.id == eventId }
                curEvent?.let { event ->
                    eventNameEdit.setText(event.name)
                    eventDateStartEdit.setText(event.dateStart)
                    eventDateEndEdit.setText(event.dateEnd)

                    val roomNames = viewModelRoom.rooms.value?.map { it.name } ?: emptyList()

                    val adapterRoom = ArrayAdapter(
                        this,
                        android.R.layout.simple_dropdown_item_1line,
                        roomNames
                    )
                    roomAutoComplete.setAdapter(adapterRoom)
                    roomAutoComplete.setText(event.roomId, false)
                    val usersNames = viewModelEvent.users.value?.map { it.second }?.toMutableList() ?: mutableListOf()

                    val adapterUser = ArrayAdapter(
                        this,
                        android.R.layout.simple_dropdown_item_1line,
                        usersNames
                    )
                    userAutoComplete.setAdapter(adapterUser)
                    userAutoComplete.setTokenizer(MultiAutoCompleteTextView.CommaTokenizer())

                    userAutoComplete.setOnItemClickListener { parent, _, position, _ ->
                        val selected = parent.getItemAtPosition(position) as String

                        if (!selectedUsers.contains(selected)) {
                            selectedUsers.add(selected)
                            userAutoComplete.setText(selectedUsers.joinToString(", ") + ", ")
                            userAutoComplete.setSelection(userAutoComplete.text.length)
                        }

                        // удаляем выбранного из списка доступных
                        usersNames.remove(selected)
                        adapterUser.clear()
                        adapterUser.addAll(usersNames)
                        adapterUser.notifyDataSetChanged()
                    }
                    userAutoComplete.dismissDropDown()
                }
            }
            addButton.setOnClickListener {
                val name = eventNameEdit.text.toString()
                val dateStart = eventDateStartEdit.text.toString()
                val dateEnd = eventDateEndEdit.text.toString()
                val roomId = roomAutoComplete.text.toString()
                val contributorsId = selectedUsers.toList()
                val firebaseUser = FirebaseAuth.getInstance().currentUser
                if (firebaseUser == null) {
                    Toast.makeText(this, "Ошибка: пользователь не авторизован", Toast.LENGTH_SHORT).show()
                    return@setOnClickListener
                }

                val ownerId = firebaseUser.uid

                if (eventId != null) {
                    val updatedEvent = Event(
                        id = eventId,
                        name = name,
                        dateStart = dateStart,
                        dateEnd = dateEnd,
                        contributors = contributorsId,
                        roomId = roomId,
                        ownerId = curEvent?.ownerId ?: ownerId // сохраняем старого владельца, если был
                    )
                    viewModelEvent.updateEventWithValidation(updatedEvent)
                }

                startActivity(Intent(this, EventListActivity::class.java))
            }
            delButton.setOnClickListener {
                viewModelEvent.delete(eventId)
                startActivity(Intent(this, EventListActivity::class.java))
            }



        } else {
        eventDateStartEdit.setOnClickListener {
            dateOperation(eventDateStartEdit)
        }
        eventDateEndEdit.setOnClickListener {
            dateOperation(eventDateEndEdit)
        }

        viewModelRoom.loadRooms()
        viewModelEvent.loadUsers()

        viewModelRoom.rooms.observe(this) { rooms ->
            val roomNames = rooms.map { it.name}
            val adapter = ArrayAdapter(
                this,
                android.R.layout.simple_dropdown_item_1line,
                roomNames
            )
            roomAutoComplete.setAdapter(adapter)
        }
        viewModelEvent.users.observe(this) { users ->
            val usersNames = users.map { it.second }.toMutableList()
            val adapter = ArrayAdapter(
                this,
                android.R.layout.simple_dropdown_item_1line,
                usersNames
            )
            userAutoComplete.setAdapter(adapter)
            userAutoComplete.setOnItemClickListener { parent, _, position, _ ->
                val selected = parent.getItemAtPosition(position) as String
                if (!selectedUsers.contains(selected)) {
                    selectedUsers.add(selected)
                    userAutoComplete.setText(selectedUsers.joinToString(", ") + ", ")
                    userAutoComplete.setSelection(userAutoComplete.text.length)
                    // убираем выбранного
                    usersNames.remove(selected)
                    adapter.clear()
                    adapter.addAll(usersNames)
                    adapter.notifyDataSetChanged()
                }
                userAutoComplete.dismissDropDown()
            }
        }
        userAutoComplete.setTokenizer(MultiAutoCompleteTextView.CommaTokenizer())
        roomAutoComplete.setOnClickListener {
            roomAutoComplete.showDropDown()
        }
        userAutoComplete.setOnClickListener {
            userAutoComplete.showDropDown()
        }

        addButton.setOnClickListener {
            val name = eventNameEdit.text.toString()
            val dateStart = eventDateStartEdit.text.toString()
            val dateEnd = eventDateEndEdit.text.toString()
            val roomId =  roomAutoComplete.text.toString()
            val contributorsId =  selectedUsers.toList()

            viewModelEvent.addEventWithValidation(name,dateStart,dateEnd,roomId,contributorsId)


            startActivity(Intent(this, EventListActivity::class.java))

            }
            }
        }


    private fun dateOperation(eventDateEdit: EditText) {
        val calendar = Calendar.getInstance()
        val datePicker = DatePickerDialog(
            this,
            { _, year, month, dayOfMonth ->
                val timePicker = TimePickerDialog(
                    this,
                    { _, hour, minute ->
                        val selected = String.format(
                            "%04d-%02d-%02d %02d:%02d",
                            year, month + 1, dayOfMonth, hour, minute
                        )
                        eventDateEdit.setText(selected)
                    },
                    calendar.get(Calendar.HOUR_OF_DAY),
                    calendar.get(Calendar.MINUTE),
                    true
                )
                timePicker.show()
            },
            calendar.get(Calendar.YEAR),
            calendar.get(Calendar.MONTH),
            calendar.get(Calendar.DAY_OF_MONTH)
        )
        datePicker.show()
    }
}