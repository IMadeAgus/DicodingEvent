package com.example.dicodingevent.ui.upcoming

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.dicodingevent.data.EventsRepository
import com.example.dicodingevent.data.local.entity.EventsEntity
import com.example.dicodingevent.data.remote.response.EventResponse
import com.example.dicodingevent.data.remote.response.ListEventsItem
import com.example.dicodingevent.data.remote.retrofit.ApiConfig
import com.example.dicodingevent.util.Event
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class UpcomingViewModel(private val eventsRepository: EventsRepository) : ViewModel() {
    val upcomingEvents = eventsRepository.getUpcomingEvents()

    fun toggleFavorite(event: EventsEntity) {
        viewModelScope.launch {
            eventsRepository.setEventsFavorite(event, true)
        }
    }

    fun deleteFavoritedEvent(event: EventsEntity) {
        viewModelScope.launch {
            eventsRepository.setEventsFavorite(event,false)
        }
    }

    fun searchEvents(query: String) {
        viewModelScope.launch {
            // Search implementation
        }
    }


}