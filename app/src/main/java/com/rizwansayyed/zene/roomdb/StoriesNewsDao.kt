package com.rizwansayyed.zene.roomdb

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import kotlinx.coroutines.flow.Flow

@Dao
interface StoriesNewsDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertArtist(item: StoriesNewsEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertArtists(items: List<StoriesNewsEntity>)

    @Query("SELECT * FROM $STORIES_NEWS_DB WHERE artistId IN (:ids)")
    suspend fun getAllArtists(ids: List<String>): List<StoriesNewsEntity>

    @Query("DELETE FROM $STORIES_NEWS_DB")
    suspend fun clearAll()
}