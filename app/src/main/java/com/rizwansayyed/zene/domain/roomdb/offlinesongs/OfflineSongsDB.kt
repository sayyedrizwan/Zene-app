package com.rizwansayyed.zene.domain.roomdb.offlinesongs

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [OfflineSongsEntity::class], version = 1)
abstract class OfflineSongsDB : RoomDatabase() {
    abstract fun offlineSongs(): OfflineSongsDao
}