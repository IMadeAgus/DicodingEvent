package com.example.dicodingevent.data.local.entity

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize

@Parcelize
@Entity(tableName = "events")
class EventEntity (
    @field:ColumnInfo(name = "id")
    @field:PrimaryKey
    val id: Int,
    @field:ColumnInfo(name = "name")
    val name: String?,

    @field:ColumnInfo(name = "summary")
    val summary: String?,

    @field:ColumnInfo(name = "description")
    val description: String?,

    @field:ColumnInfo(name = "image_logo")
    val imageLogo: String?,

    @field:ColumnInfo(name = "media_cover")
    val mediaCover: String?,

    @field:ColumnInfo(name = "category")
    val category: String?,

    @field:ColumnInfo(name = "owner_name")
    val ownerName: String?,

    @field:ColumnInfo(name = "city_name")
    val cityName: String?,

    @field:ColumnInfo(name = "quota")
    val quota: Int?,

    @field:ColumnInfo(name = "registrants")
    val registrants: Int?,

    @field:ColumnInfo(name = "begin_time")
    val beginTime: String?,

    @field:ColumnInfo(name = "end_time")
    val endTime: String?,

    @field:ColumnInfo(name = "link")
    val link: String?,

    @field:ColumnInfo(name = "is_favorite")
    var isFavorite: Boolean?,

    @field:ColumnInfo(name = "is_upcoming")
    var isUpcoming: Boolean?,

    @field:ColumnInfo(name = "is_finished")
    var isFinished: Boolean?
): Parcelable