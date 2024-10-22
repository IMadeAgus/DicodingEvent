package com.example.dicodingevent.di

import android.content.Context
import com.example.dicodingevent.data.EventsRepository
import com.example.dicodingevent.data.local.room.EventsDatabase
import com.example.dicodingevent.data.remote.retrofit.ApiConfig

object Injection {
    fun provideRepository(context: Context): EventsRepository {
        val apiService = ApiConfig.getApiService()
        val database = EventsDatabase.getInstance(context)
        val dao = database.eventsDao()
        return EventsRepository.getInstance(apiService,dao)
    }
}