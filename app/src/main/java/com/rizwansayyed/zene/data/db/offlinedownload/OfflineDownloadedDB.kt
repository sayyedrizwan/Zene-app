package com.rizwansayyed.zene.data.db.offlinedownload

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [OfflineDownloadedEntity::class], version = 1)
abstract class OfflineDownloadedDB : RoomDatabase() {
    abstract fun dao(): OfflineDownloadedDao
}