package com.eugene.meetupproject.presentation.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.eugene.meetupproject.R
import com.eugene.meetupproject.domain.model.Event


class EventAdapter(
    private val onItemClick: (Event) -> Unit
): RecyclerView.Adapter<EventAdapter.EventViewHolder>() {

    private var events: List<Event> = emptyList()

    inner class EventViewHolder(
        itemView: View
    ) : RecyclerView.ViewHolder(itemView) {
        val nameTextview: TextView = itemView.findViewById(R.id.event_name)
        val timeTextview: TextView = itemView.findViewById(R.id.event_time)
        val roomTextView: TextView = itemView.findViewById(R.id.room_name)
        val contributorsTextView: TextView = itemView.findViewById(R.id.event_contributors)

        fun bind(event: Event) {
            nameTextview.text = event.name
            timeTextview.text = "${event.dateStart} - ${event.dateEnd}"
            roomTextView.text = event.roomId
            contributorsTextView.text = event.contributors.joinToString(" ")
            itemView.setOnClickListener {
                onItemClick(event) // вызов коллбэка
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EventViewHolder {
        val itemView =
            LayoutInflater.from(parent.context)
                .inflate(R.layout.item_event_info, parent, false)
        return EventViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: EventViewHolder, position: Int) {
        val event = events[position]
        holder.bind(event)
    }

    override fun getItemCount(): Int = events.size

    fun setData(newEvents: List<Event>) {
        events = newEvents
        notifyDataSetChanged()
    }

}