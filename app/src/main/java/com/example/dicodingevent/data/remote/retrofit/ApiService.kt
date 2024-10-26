package com.example.dicodingevent.data.remote.retrofit

import com.example.dicodingevent.data.remote.response.EventResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiService {
//    @GET("events")
//    fun searchEvent(
//        @Query("active") active: Int,
//        @Query("q") query: String,
//    ): Call<EventResponse>

    @GET("events")
    suspend fun getUpcomingEvent(
        @Query("active") active: Int,
        @Query("limit") limit: Int
    ): EventResponse

    @GET("events")
    suspend fun getFinishedEvent(
        @Query("active") active: Int,
        @Query("limit") limit: Int = 5
    ): EventResponse

//    @GET("events/{id}")
//    fun getEventDetail(
//        @Path("id") id: String
//    ): Call<ResponseDetail>
}