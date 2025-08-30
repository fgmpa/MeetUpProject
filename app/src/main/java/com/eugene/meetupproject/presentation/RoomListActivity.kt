package com.eugene.meetupproject.presentation

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.RecyclerView
import com.eugene.meetupproject.R
import com.eugene.meetupproject.di.MyApp
import com.eugene.meetupproject.presentation.adapters.EventAdapter
import com.eugene.meetupproject.presentation.adapters.RoomAdapter
import com.eugene.meetupproject.presentation.viewmodel.EventViewModel
import com.eugene.meetupproject.presentation.viewmodel.RoomViewModel
import javax.inject.Inject
import kotlin.getValue

class RoomListActivity: AppCompatActivity() {
    @Inject
    lateinit var viewModelFactory: DaggerViewModelFactory
    private lateinit var viewModel: RoomViewModel
    private lateinit var adapter: RoomAdapter


    override fun onCreate(savedInstanceState: Bundle?) {
        (application as MyApp).appComponent.inject(this)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_room_list)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.root)) { view, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            view.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        viewModel = ViewModelProvider(this, viewModelFactory).get(RoomViewModel::class.java)
        adapter = RoomAdapter { room ->
            val intent = Intent(this, RoomValuesActivity::class.java)
            intent.putExtra("roomId", room.id)
            startActivity(intent)
        }
        val recycler = findViewById<RecyclerView>(R.id.room_recycler_view)
        recycler.adapter = adapter
        setupUi()
    }

    private fun setupUi() {
        val addButton = findViewById<Button>(R.id.add_room_but)
        val returnButton = findViewById<Button>(R.id.done_room_but)
        addButton.setOnClickListener {
            startActivity(Intent(this, RoomValuesActivity::class.java))
        }
        returnButton.setOnClickListener {
            startActivity(Intent(this, EventListActivity::class.java))
        }
        viewModel.rooms.observe(this) { rooms ->
            adapter.setData(rooms)
        }

    }

}