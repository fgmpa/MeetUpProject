package com.eugene.meetupproject.presentation

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.PopupMenu
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.ViewModelProvider
import com.eugene.meetupproject.R
import com.eugene.meetupproject.di.MyApp
import com.eugene.meetupproject.presentation.viewmodel.AuthViewModel
import com.eugene.meetupproject.presentation.viewmodel.EventViewModel
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.firebase.auth.FirebaseAuth
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale
import javax.inject.Inject

class EventListActivity : AppCompatActivity() {
    @Inject
    lateinit var viewModelFactory: DaggerViewModelFactory
    private lateinit var eventViewModel: EventViewModel
    private lateinit var  authViewModel: AuthViewModel

    private var isCalendarMode = false


    override fun onCreate(savedInstanceState: Bundle?) {
        (application as MyApp).appComponent.inject(this)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_event_list)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.root)) { view, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            view.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        eventViewModel = ViewModelProvider(this, viewModelFactory).get(EventViewModel::class.java)
        authViewModel = ViewModelProvider(this, viewModelFactory).get(AuthViewModel::class.java)

        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                .replace(R.id.container_view, MeetingListFragment())
                .commit()
            isCalendarMode = false
        }
        setupUI()
    }

    private fun setupUI() {
        val addButton = findViewById<FloatingActionButton>(R.id.add_event_but)
        val roomButton = findViewById<Button>(R.id.rooms_but)
        val calendarMode = findViewById<TextView>(R.id.calendar_mode)
        var todayButton = findViewById<Button>(R.id.today_event_but)
        var userEventsButton = findViewById<Button>(R.id.users_event_but)
        val currentUserId = FirebaseAuth.getInstance().currentUser?.uid
        val settingsButton = findViewById<FloatingActionButton>(R.id.settings_but)

        settingsButton.setOnClickListener { view ->
            val popup = PopupMenu(this, view) // "привязываем" к FAB
            popup.menuInflater.inflate(R.menu.settings_menu, popup.menu)

            popup.setOnMenuItemClickListener { item ->
                when (item.itemId) {
                    R.id.exit -> {
                        authViewModel.signOut()
                        startActivity(Intent(this, LoginActivity::class.java))
                        true
                    }
                    else -> false
                }
            }
            popup.show()
        }
        todayButton.setOnClickListener {
            val transaction = supportFragmentManager.beginTransaction()
           if (isCalendarMode) {
               transaction.replace(R.id.container_view, MeetingListFragment())
               isCalendarMode = !isCalendarMode
           }
            val calendar = Calendar.getInstance()
            val sdf = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
            val todayString = sdf.format(calendar.time)
            eventViewModel.selectDate(todayString)
            eventViewModel.loadEventsForDay(todayString)
            transaction.commit()
        }
        userEventsButton.setOnClickListener {
            val transaction = supportFragmentManager.beginTransaction()
            if (isCalendarMode) {
                transaction.replace(R.id.container_view, MeetingListFragment())
                isCalendarMode = !isCalendarMode
            }
            eventViewModel.loadEventsForUser(currentUserId.toString())
            transaction.commit()

        }
        addButton.setOnClickListener {
            startActivity(Intent(this, EventValuesActivity::class.java))
        }
        roomButton.setOnClickListener {
            startActivity(Intent(this, RoomListActivity::class.java))
        }
        calendarMode.setOnClickListener {
            val transaction = supportFragmentManager.beginTransaction()
            if (!isCalendarMode) {
                transaction.replace(R.id.container_view, CalendarFragment())
                isCalendarMode = !isCalendarMode

            } else {
                transaction.replace(R.id.container_view, MeetingListFragment())
                isCalendarMode = !isCalendarMode
            }
            transaction.commit()
        }
        eventViewModel.selectedDate.observe(this) { date ->
            calendarMode.text = date
        }

    }

}