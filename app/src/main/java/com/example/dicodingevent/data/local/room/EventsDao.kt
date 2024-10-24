package com.example.dicodingevent.data.local.room

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.dicodingevent.data.local.entity.EventsEntity
@Dao
interface EventsDao {
    @Query("SELECT * FROM events")
    fun getEvents(): LiveData<List<EventsEntity>>

    @Query("SELECT * FROM events where status = 'upcoming'")
    fun getUpcomingEvents(): LiveData<List<EventsEntity>>

    @Query("SELECT * FROM events where status = 'finished'")
    fun getFinishedEvents(): LiveData<List<EventsEntity>>

    @Query("SELECT * FROM events WHERE status = 'finished' LIMIT 5")
    fun get5FinishedEvents(): LiveData<List<EventsEntity>>

    @Query("SELECT * FROM events WHERE status = 'upcoming' LIMIT 1")
    fun getTopUpcomingEvents(): LiveData<List<EventsEntity>>

    @Query("SELECT * FROM events where favorited = 1")
    fun getFavoritedEvents(): LiveData<List<EventsEntity>>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertEvents(news: List<EventsEntity>)

    @Update
    suspend fun updateEvents(news: EventsEntity)

    @Query("DELETE FROM events WHERE status = 'upcoming'")
    suspend fun deleteUpcomingAll()

    @Query("DELETE FROM events WHERE status = 'finished'")
    suspend fun deleteFinishedAll()

    @Query("DELETE FROM events WHERE favorited = 0")
    suspend fun deleteAll()

    @Query("SELECT EXISTS(SELECT * FROM events WHERE id = :id AND favorited = 1)")
    suspend fun isEventsFavorited(id: Int): Boolean

    @Query("SELECT * FROM events WHERE status = 'upcoming' AND name LIKE '%' || :query || '%'")
    suspend fun searchUpcomingEvents(query: String): List<EventsEntity>

}