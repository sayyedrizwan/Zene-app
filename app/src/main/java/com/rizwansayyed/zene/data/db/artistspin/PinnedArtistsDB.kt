package com.rizwansayyed.zene.data.db.artistspin

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [PinnedArtistsEntity::class], version = 1)
abstract class PinnedArtistsDB : RoomDatabase() {
    abstract fun dao(): PinnedArtistsDao
}