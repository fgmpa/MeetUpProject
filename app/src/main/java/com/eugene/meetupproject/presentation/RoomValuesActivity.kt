package com.eugene.meetupproject.presentation

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.ViewModelProvider
import com.eugene.meetupproject.R
import com.eugene.meetupproject.di.MyApp
import com.eugene.meetupproject.domain.model.Event
import com.eugene.meetupproject.domain.model.Room
import com.eugene.meetupproject.presentation.viewmodel.RoomViewModel
import com.google.firebase.auth.FirebaseAuth
import javax.inject.Inject

class RoomValuesActivity: AppCompatActivity() {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory
    private val viewModel: RoomViewModel by viewModels { viewModelFactory }

    override fun onCreate(savedInstanceState: Bundle?) {
        (application as MyApp).appComponent.inject(this)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_room_values)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.root)) { view, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            view.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val roomNameEdit = findViewById<EditText>(R.id.room_name_edit)
        val roomSaveButton = findViewById<Button>(R.id.room_save_button)
        val delButton = findViewById<Button>(R.id.room_delete_button)
        val roomId = intent.getStringExtra("roomId")
        if (roomId != null) {
            delButton.visibility = View.VISIBLE
            viewModel.rooms.observe(this) { rooms ->
                val curRoom = rooms.find { it.id == roomId }
                curRoom?.let { event ->
                    roomNameEdit.setText(event.name)
                }
            }
            roomSaveButton.setOnClickListener {
                val name = roomNameEdit.text.toString()
                val firebaseUser = FirebaseAuth.getInstance().currentUser
                if (firebaseUser == null) {
                    Toast.makeText(this, "Ошибка: пользователь не авторизован", Toast.LENGTH_SHORT).show()
                    return@setOnClickListener
                }

                // если мы редактируем событие
                if (roomId != null) {
                    val updatedRoom = Room(
                        id = roomId,
                        name = name
                    )
                    viewModel.change(updatedRoom)
                }

                startActivity(Intent(this, RoomListActivity::class.java))
            }
            delButton.setOnClickListener {
                viewModel.delete(roomId)
                startActivity(Intent(this, RoomListActivity::class.java))
            }
        } else {
        roomSaveButton.setOnClickListener {
            val name = roomNameEdit.text.toString()
            if (name.isNotBlank()) {
                viewModel.add(name)
            }
            startActivity(Intent(this, RoomListActivity::class.java))
            }
        }
    }

}
