package com.example.dicodingevent.ui.upcoming

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.dicodingevent.data.EventsRepository
import com.example.dicodingevent.data.local.entity.EventEntity
import com.example.eventapp.utils.Result
import kotlinx.coroutines.launch

class UpcomingViewModel(private val eventRepository: EventsRepository) : ViewModel() {

    fun getUpcomingEvents() = eventRepository.getEvents(active = 1)

    fun searchUpcomingEvents(query: String): LiveData<Result<List<EventEntity>>> =
        eventRepository.searchEvents(1, query)

    fun saveEvents(events: EventEntity) {
        viewModelScope.launch {
            eventRepository.setFavoriteEvents(events, true)
        }
    }

    fun deleteEvents(events: EventEntity) {
        viewModelScope.launch {
            eventRepository.setFavoriteEvents(events, false)
        }
    }


}