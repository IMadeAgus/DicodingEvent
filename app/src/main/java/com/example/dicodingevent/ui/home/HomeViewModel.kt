package com.example.dicodingevent.ui.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.dicodingevent.data.EventsRepository
import com.example.dicodingevent.data.local.entity.EventEntity
import kotlinx.coroutines.launch

class HomeViewModel(private val eventRepository: EventsRepository) : ViewModel() {
    fun getUpcomingEvents() = eventRepository.getEvents(active = 1)
    fun getFinishedEvents() = eventRepository.getEvents(active = 0)

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