package com.eugene.meetupproject.presentation

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.eugene.meetupproject.R
import com.eugene.meetupproject.presentation.adapters.EventAdapter
import com.eugene.meetupproject.presentation.viewmodel.EventViewModel


class MeetingListFragment : Fragment(R.layout.fragment_meeting_list) {


    private lateinit var  viewModel: EventViewModel

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = ViewModelProvider(
            requireActivity(),
            (requireActivity() as EventListActivity).viewModelFactory
        )
            .get(EventViewModel::class.java)
        val adapter = EventAdapter { event ->
            val intent = Intent(requireContext(), EventValuesActivity::class.java)
            intent.putExtra("eventId", event.id)
            startActivity(intent)
        }
        val recycler = view.findViewById<RecyclerView>(R.id.event_recycler_view)
        recycler.layoutManager = LinearLayoutManager(requireContext())
        recycler.adapter = adapter

        viewModel.events.observe(viewLifecycleOwner) { events ->
            adapter.setData(events)
        }
        viewModel.selectedDate.observe(viewLifecycleOwner) { date ->
            viewModel.loadEventsForDay(date)
        }
        viewModel.selectedDate.value?.let { viewModel.loadEventsForDay(it) }
    }
}