package com.example.dicodingevent.data

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.liveData
import androidx.lifecycle.map
import com.example.dicodingevent.data.local.entity.EventEntity
import com.example.dicodingevent.data.local.room.EventsDao
import com.example.dicodingevent.data.remote.response.EventResponse
import com.example.dicodingevent.data.remote.retrofit.ApiService
import com.example.eventapp.utils.Result


class EventsRepository private constructor(
    private val apiService: ApiService,
    private val eventsDao: EventsDao,
    ){

    fun getEvents(active: Int): LiveData<Result<List<EventEntity>>> = liveData {
        emit(Result.Loading)
        try {
            val response = apiService.getEvents(active = active)
            val events = response.listEvents
            val eventList = events?.map { event ->
                val isFavorite = event.name?.let { eventsDao.isEventFavorite(it) }
                val isUpcoming = active == 1
                val isFinished = active == 0
                EventEntity(
                    event.id,
                    event.name,
                    event.summary,
                    event.description,
                    event.imageLogo,
                    event.mediaCover,
                    event.category,
                    event.ownerName,
                    event.cityName,
                    event.quota,
                    event.registrants,
                    event.beginTime,
                    event.endTime,
                    event.link,
                    isFavorite,
                    isUpcoming,
                    isFinished,
                )
            }
            eventsDao.insertEvents(eventList)
        } catch (e:Exception) {
            Log.d("EventRepository", "getEvents: ${e.message.toString()}")
            emit(Result.Error(e.message.toString()))
        }

        // Save to room
        val localData: LiveData<Result<List<EventEntity>>> = if (active == 1) {
            eventsDao.getUpcomingEvents().map { Result.Success(it) }
        } else {
            eventsDao.getFinishedEvents().map { Result.Success(it) }
        }
        emitSource(localData)
    }

    fun getFavoriteEvents(): LiveData<List<EventEntity>> {
        return eventsDao.getFavoriteEvents()
    }

    suspend fun setEventsFavorite(events: EventEntity, favotiteState: Boolean) {
        events.isFavorite = favotiteState
        eventsDao.updateEvents(events)
    }

    fun searchEvents(active: Int, query: String): LiveData<Result<List<EventEntity>>> = liveData {
        emit(Result.Loading)
        val localData: LiveData<Result<List<EventEntity>>>
        try {
            localData = when (active) {
                1 -> {
                    eventsDao.searchUpcomingEvents(query).map { Result.Success(it) }
                }
                0 -> {
                    eventsDao.searchFinishedEvents(query).map { Result.Success(it) }
                }
                else -> {
                    eventsDao.searchFavoriteEvents(query).map { Result.Success(it) }
                }
            }
            emitSource(localData)
        } catch (e: Exception) {
            emit(Result.Error(e.message.toString()))
        }
    }

    suspend fun getLatestEvent(): EventResponse? {
        return try {
            apiService.getEvents(active = -1, limit = 1)
        } catch (e: Exception) {
            Log.e("EventRepository", "Error fetching latest event: ${e.message}")
            null
        }
    }



    companion object {
        @Volatile
        private var instance: EventsRepository? = null
        fun getInstance(
            apiService: ApiService,
            eventsDao: EventsDao
        ) : EventsRepository =
            instance ?: synchronized(this) {
                instance ?: EventsRepository(apiService,eventsDao)
            }.also { instance = it }
    }
}