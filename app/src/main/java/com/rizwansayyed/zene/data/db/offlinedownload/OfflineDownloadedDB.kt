package com.rizwansayyed.zene.data.db.offlinedownload

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import com.rizwansayyed.zene.data.utils.DBNAME.OFFLINE_DOWNLOADED_SONGS_DB

@Database(entities = [OfflineDownloadedEntity::class], version = 2)
abstract class OfflineDownloadedDB : RoomDatabase() {
    abstract fun dao(): OfflineDownloadedDao
}


val MIGRATION_1_2_OFFLINE_DB = object : Migration(1, 2) {
    override fun migrate(db: SupportSQLiteDatabase) {
        db.execSQL("ALTER TABLE $OFFLINE_DOWNLOADED_SONGS_DB ADD COLUMN lyrics TEXT DEFAULT ''")
        db.execSQL("ALTER TABLE $OFFLINE_DOWNLOADED_SONGS_DB ADD COLUMN subtitles BOOLEAN")
    }
}
