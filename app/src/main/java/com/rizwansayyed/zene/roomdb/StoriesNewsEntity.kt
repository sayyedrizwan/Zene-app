package com.rizwansayyed.zene.roomdb

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

const val STORIES_NEWS_DB = "stories_name"
@Entity(tableName = STORIES_NEWS_DB, indices = [Index("artistId")])
data class StoriesNewsEntity(
    @PrimaryKey
    val artistId: String,
    val latestUrl: String?,
    val updatedAt: Long = System.currentTimeMillis(),
    val isViewed: Boolean = false
)