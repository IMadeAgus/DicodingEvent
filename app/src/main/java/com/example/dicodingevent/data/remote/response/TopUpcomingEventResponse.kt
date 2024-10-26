package com.example.dicodingevent.data.remote.response

import com.squareup.moshi.Json

data class TopUpcomingEventResponse(
    val error: Boolean,
    val message: String,
    val listEvents: List<UpcomingEvent>
)

data class UpcomingEvent(
    val id: Int,
    val name: String,
    val beginTime: String,
    val endTime: String,
    val description: String,
    val quota: Int,
    val registrants: Int
)
