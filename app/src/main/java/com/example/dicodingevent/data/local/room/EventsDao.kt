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

    @Query("SELECT * FROM events where favorited = 1")
    fun getFavoritedEvents(): LiveData<List<EventsEntity>>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertEvents(news: List<EventsEntity>)

    @Update
    suspend fun updateEvents(news: EventsEntity)

    @Query("DELETE FROM events WHERE favorited = 0")
    suspend fun deleteAll()

    @Query("SELECT EXISTS(SELECT * FROM events WHERE id = :id AND favorited = 1)")
    suspend fun isEventsFavorited(id: Int): Boolean
}