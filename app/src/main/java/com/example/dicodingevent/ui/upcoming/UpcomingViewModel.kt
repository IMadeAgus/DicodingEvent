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

    private val _searchResults = MutableLiveData<com.example.dicodingevent.data.Result<List<EventsEntity>>>()
    val searchResults: LiveData<com.example.dicodingevent.data.Result<List<EventsEntity>>> get() = _searchResults

    fun searchEvents(query: String) {
        viewModelScope.launch {
            eventsRepository.searchEvents(query).observeForever {
                _searchResults.value = it
            }
        }
    }

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


}