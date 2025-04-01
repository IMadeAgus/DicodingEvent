package com.example.dicodingevent.data.local.room

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.dicodingevent.data.local.entity.EventEntity
@Dao
interface EventsDao {
    @Query("SELECT * FROM events WHERE isUpcoming = 1")
    fun getUpcomingEvents(): LiveData<List<EventEntity>>

    @Query("SELECT * FROM events WHERE isFinished = 1")
    fun getFinishedEvents(): LiveData<List<EventEntity>>

    @Query("SELECT * FROM events WHERE isFavorited = 1")
    fun getFavoriteEvents(): LiveData<List<EventEntity>>

    @Query("""
    SELECT * FROM events 
    WHERE isUpcoming = 1
    AND (
        name LIKE '%' || :query || '%' OR 
        summary LIKE '%' || :query || '%' OR 
        description LIKE '%' || :query || '%' OR 
        category LIKE '%' || :query || '%' OR 
        ownerName LIKE '%' || :query || '%'
        )
    """)
    fun searchUpcomingEvents(query: String): LiveData<List<EventEntity>>

    @Query("""
    SELECT * FROM events 
    WHERE isFinished = 1
    AND (
        name LIKE '%' || :query || '%' OR 
        summary LIKE '%' || :query || '%' OR 
        description LIKE '%' || :query || '%' OR 
        category LIKE '%' || :query || '%' OR 
        ownerName LIKE '%' || :query || '%'
        )
    """)
    fun searchFinishedEvents(query: String): LiveData<List<EventEntity>>

    @Query("""
    SELECT * FROM events 
    WHERE isFavorited = 1
    AND (
        name LIKE '%' || :query || '%' OR 
        summary LIKE '%' || :query || '%' OR 
        description LIKE '%' || :query || '%' OR 
        category LIKE '%' || :query || '%' OR 
        ownerName LIKE '%' || :query || '%'
        )
    """)
    fun searchFavoriteEvents(query: String): LiveData<List<EventEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertEvents(events: List<EventEntity>?)

    @Update
    suspend fun updateEvents(events: EventEntity)

    @Query("DELETE FROM events WHERE isFavorited = 0")
    suspend fun deleteAll()

    @Query("SELECT EXISTS(SELECT * FROM events WHERE name = :name AND isFavorited = 1)")
    suspend fun isEventFavorite(name: String): Boolean

}