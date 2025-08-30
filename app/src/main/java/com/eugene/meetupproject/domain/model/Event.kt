package com.eugene.meetupproject.domain.model

import javax.inject.Inject

data class Event(
    val id: String,
    val name: String,
    val dateStart: String,
    val dateEnd: String,
    val contributors: List<String>,
    val roomId: String,
    val ownerId: String
)
