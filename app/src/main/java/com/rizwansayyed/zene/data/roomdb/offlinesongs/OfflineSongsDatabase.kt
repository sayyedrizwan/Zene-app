package com.rizwansayyed.zene.data.roomdb.offlinesongs

import androidx.room.Database
import androidx.room.RoomDatabase
import com.rizwansayyed.zene.data.roomdb.offlinesongs.model.OfflineSongsData


@Database(entities = [OfflineSongsData::class], version = 1)
abstract class OfflineSongsDatabase : RoomDatabase() {
    abstract fun offlineSongsDao(): OfflineSongsDao
}