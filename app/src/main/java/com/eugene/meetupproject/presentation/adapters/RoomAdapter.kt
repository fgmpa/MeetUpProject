package com.eugene.meetupproject.presentation.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.eugene.meetupproject.R
import com.eugene.meetupproject.domain.model.Event
import com.eugene.meetupproject.domain.model.Room

class RoomAdapter(
    private val onItemClick: (Room) -> Unit
): RecyclerView.Adapter<RoomAdapter.RoomViewHolder>() {

    private var rooms: List<Room> = emptyList()

    inner class RoomViewHolder(
        itemView: View
    ) : RecyclerView.ViewHolder(itemView) {
        val nameTextview: TextView = itemView.findViewById(R.id.room_name)
        fun bind(room: Room) {
            nameTextview.text = room.name
            itemView.setOnClickListener {
                onItemClick(room) // вызов коллбэка
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RoomViewHolder {
        val itemView =
            LayoutInflater.from(parent.context)
                .inflate(R.layout.item_room_info, parent, false)
        return RoomViewHolder(itemView)
    }
    override fun onBindViewHolder(holder: RoomViewHolder, position: Int) {
        val room = rooms[position]
        holder.bind(room)
    }
    override fun getItemCount(): Int = rooms.size
    fun setData(newRooms: List<Room>) {
        rooms = newRooms
        notifyDataSetChanged()
    }
}