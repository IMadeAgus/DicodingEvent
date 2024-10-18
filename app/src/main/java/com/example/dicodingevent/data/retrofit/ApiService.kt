package com.example.dicodingevent.data.retrofit

import com.example.dicodingevent.data.response.EventResponse
import com.example.dicodingevent.data.response.ResponseDetail
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface ApiService {
    @GET("events")
    fun searchEvent(
        @Query("active") active: Int,
        @Query("q") query: String,
    ): Call<EventResponse>

    @GET("events")
    fun getUpcomingEvent(
        @Query("active") active: Int,
        @Query("limit") limit: Int
    ): Call<EventResponse>

    @GET("events")
    fun getFinishedEvent(
        @Query("active") active: Int,
        @Query("limit") limit: Int = 5
    ): Call<EventResponse>

    @GET("events/{id}")
    fun getEventDetail(
        @Path("id") id: String
    ): Call<ResponseDetail>
}