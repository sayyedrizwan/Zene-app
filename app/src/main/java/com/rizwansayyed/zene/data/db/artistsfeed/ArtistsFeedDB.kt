package com.rizwansayyed.zene.data.db.artistsfeed

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [ArtistsFeedEntity::class], version = 1)
abstract class ArtistsFeedDB : RoomDatabase() {
    abstract fun dao(): ArtistsFeedDao
}