package com.example.dicodingevent.ui.detail

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.dicodingevent.data.EventsRepository
import com.example.dicodingevent.data.Result
import com.example.dicodingevent.data.local.entity.EventsEntity

import kotlinx.coroutines.launch


class DetailViewModel(private val eventsRepository: EventsRepository) : ViewModel() {
    private val _eventDetail = MutableLiveData<Result<EventsEntity>>()
    val eventDetail: LiveData<Result<EventsEntity>> = _eventDetail


    fun getEventDetail(id: Int) {
        viewModelScope.launch {
            eventsRepository.getDetailEvent(id).observeForever { result ->
                _eventDetail.value = result
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
            eventsRepository.setEventsFavorite(event, false)
        }
    }
}