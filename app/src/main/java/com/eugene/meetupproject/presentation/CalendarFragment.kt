package com.eugene.meetupproject.presentation

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.applandeo.materialcalendarview.CalendarView
import com.applandeo.materialcalendarview.EventDay
import com.applandeo.materialcalendarview.listeners.OnDayClickListener
import com.eugene.meetupproject.R
import com.eugene.meetupproject.presentation.viewmodel.EventViewModel
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale


class CalendarFragment : Fragment(R.layout.fragment_calendar) {

    private lateinit var viewModel: EventViewModel

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = ViewModelProvider(
            requireActivity(),
            (requireActivity() as EventListActivity).viewModelFactory
        )
            .get(EventViewModel::class.java)
        val calendarView = view.findViewById<CalendarView>(R.id.calendar_view)
        calendarView.setOnDayClickListener(object : OnDayClickListener {
            override fun onDayClick(eventDay: EventDay) {
                val clickedDayCalendar: Calendar = eventDay.calendar


                val sdf = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
                val dateOnly = sdf.format(clickedDayCalendar.time)


                viewModel.selectDate(dateOnly)


                parentFragmentManager.beginTransaction()
                    .replace(R.id.container_view, MeetingListFragment())
                    .addToBackStack(null)
                    .commit()
            }

        })
    }

}