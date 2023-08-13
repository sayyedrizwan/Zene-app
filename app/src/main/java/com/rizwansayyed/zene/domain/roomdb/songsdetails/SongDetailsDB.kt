package com.rizwansayyed.zene.domain.roomdb.songsdetails

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [SongDetailsEntity::class], version = 1)
abstract class SongDetailsDB : RoomDatabase() {
    abstract fun songDetails(): SongDetailsDao
}