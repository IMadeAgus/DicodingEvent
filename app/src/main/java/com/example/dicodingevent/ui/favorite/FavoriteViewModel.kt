package com.example.dicodingevent.ui.favorite

import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.dicodingevent.data.EventsRepository
import com.example.dicodingevent.data.local.entity.EventsEntity
import kotlinx.coroutines.launch

class FavoriteViewModel(private val eventsRepository: EventsRepository) : ViewModel() {
    private val _favoriteEvent = MediatorLiveData<List<EventsEntity>>()
    val favoriteEvent: LiveData<List<EventsEntity>> = _favoriteEvent

    init {
        _favoriteEvent.addSource(eventsRepository.getFavoritedEvents()) { events ->
            _favoriteEvent.value = events
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