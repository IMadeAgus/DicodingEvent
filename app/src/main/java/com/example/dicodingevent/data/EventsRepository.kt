package com.example.dicodingevent.data

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.liveData
import androidx.lifecycle.map
import com.example.dicodingevent.data.local.entity.EventsEntity
import com.example.dicodingevent.data.local.room.EventsDao
import com.example.dicodingevent.data.remote.retrofit.ApiService

class EventsRepository private constructor(
    private val apiService: ApiService,
    private val eventsDao: EventsDao,
    ){

    fun getUpcomingEvents(): LiveData<Result<List<EventsEntity>>> = liveData {
        emit(Result.Loading)
        try {
            val response = apiService.getUpcomingEvent(1, 40)
            val status = "upcoming"
            val listEvents = response.listEvents
            val eventList = listEvents.map { listEvent ->
                val isFavorited = eventsDao.isEventsFavorited(listEvent.id)
                EventsEntity(
                    listEvent.id,
                    listEvent.name,
                    listEvent.ownerName,
                    listEvent.description,
                    listEvent.mediaCover,
                    listEvent.link,
                    listEvent.registrants,
                    listEvent.quota,
                    listEvent.beginTime,
                    listEvent.endTime,
                    status,
                    isFavorited
                )
            }
            eventsDao.deleteUpcomingAll()
            eventsDao.insertEvents(eventList)
        } catch (e:Exception) {
            Log.d("EventRepository", "getUpcomingEvents: ${e.message.toString()}")
            emit(Result.Error(e.message.toString()))
        }
        val localData: LiveData<Result<List<EventsEntity>>> = eventsDao.getUpcomingEvents().map { Result.Success(it) }
        emitSource(localData)
    }

    fun getFinishedEvents(): LiveData<Result<List<EventsEntity>>> = liveData {
        emit(Result.Loading)
        try {
            val response = apiService.getFinishedEvent(0, 40)
            val listEvents = response.listEvents
            val eventList = listEvents.map { listEvent ->
                val isFavorited = eventsDao.isEventsFavorited(listEvent.id)
                val status = "finished"
                EventsEntity(
                    listEvent.id,
                    listEvent.name,
                    listEvent.ownerName,
                    listEvent.description,
                    listEvent.mediaCover,
                    listEvent.link,
                    listEvent.registrants,
                    listEvent.quota,
                    listEvent.beginTime,
                    listEvent.endTime,
                    status,
                    isFavorited
                )
            }
            eventsDao.deleteFinishedAll()
            eventsDao.insertEvents(eventList)
        } catch (e:Exception) {
            Log.d("EventRepository", "getFinishedEvent: ${e.message.toString()}")
            emit(Result.Error(e.message.toString()))
        }
        val localData: LiveData<Result<List<EventsEntity>>> = eventsDao.getFinishedEvents().map { Result.Success(it) }
        emitSource(localData)
    }

    fun getFavoritedEvents(): LiveData<List<EventsEntity>> {
        return eventsDao.getFavoritedEvents()
    }

    fun get5FinishedEvents(): LiveData<Result<List<EventsEntity>>> = liveData {
        emit(Result.Loading)
        try {
            val response = apiService.getFinishedEvent(0, 5)
            val listEvents = response.listEvents
            val eventList = listEvents.map { listEvent ->
                val isFavorited = eventsDao.isEventsFavorited(listEvent.id)
                val status = "finished"
                EventsEntity(
                    listEvent.id,
                    listEvent.name,
                    listEvent.ownerName,
                    listEvent.description,
                    listEvent.mediaCover,
                    listEvent.link,
                    listEvent.registrants,
                    listEvent.quota,
                    listEvent.beginTime,
                    listEvent.endTime,
                    status,
                    isFavorited
                )
            }
            eventsDao.deleteFinishedAll()
            eventsDao.insertEvents(eventList)
        } catch (e:Exception) {
            Log.d("EventRepository", "getFinishedEvent: ${e.message.toString()}")
            emit(Result.Error(e.message.toString()))
        }
        val localData: LiveData<Result<List<EventsEntity>>> = eventsDao.get5FinishedEvents().map { Result.Success(it) }
        emitSource(localData)
    }

    fun searchEvents(query: String): LiveData<Result<List<EventsEntity>>> = liveData {
        emit(Result.Loading)
        try {
            val searchResults = eventsDao.searchUpcomingEvents(query)
            emit(Result.Success(searchResults))
        } catch (e: Exception) {
            emit(Result.Error(e.message.toString()))
        }
    }

    fun getTopUpcomingEvent(): LiveData<Result<List<EventsEntity>>> = liveData {
        emit(Result.Loading)
        try {
            val localData: LiveData<Result<List<EventsEntity>>> =
                eventsDao.getTopUpcomingEvents().map { Result.Success(it) }
            emitSource(localData)
        } catch (e: Exception) {
            emit(Result.Error(e.message.toString()))
        }
    }

    fun getDetailEvent(id: Int): LiveData<Result<EventsEntity>> = liveData {
        emit(Result.Loading)
        try {
            val localData: LiveData<Result<EventsEntity>> = eventsDao.getDetailEvent(id).map { Result.Success(it) }
            emitSource(localData)
        } catch (e: Exception) {
            emit(Result.Error(e.message.toString()))
        }
    }

    suspend fun setEventsFavorite(events: EventsEntity, favotiteState: Boolean) {
        events.isFavorited = favotiteState
        eventsDao.updateEvents(events)
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