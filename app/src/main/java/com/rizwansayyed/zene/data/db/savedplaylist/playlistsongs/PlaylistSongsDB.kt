package com.rizwansayyed.zene.data.db.savedplaylist.playlistsongs

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [PlaylistSongsEntity::class], version = 1)
abstract class PlaylistSongsDB : RoomDatabase() {
    abstract fun dao(): PlaylistSongsDao
}