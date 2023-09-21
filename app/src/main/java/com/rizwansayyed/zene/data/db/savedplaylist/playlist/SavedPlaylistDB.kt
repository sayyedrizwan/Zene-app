package com.rizwansayyed.zene.data.db.savedplaylist.playlist

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [SavedPlaylistEntity::class], version = 1)
abstract class SavedPlaylistDB : RoomDatabase() {
    abstract fun dao(): SavedPlaylistDao
}