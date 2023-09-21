package com.rizwansayyed.zene.di

import android.content.Context
import androidx.room.Room
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import com.rizwansayyed.zene.data.db.offlinedownload.OfflineDownloadedDB
import com.rizwansayyed.zene.data.db.offlinedownload.OfflineDownloadedDao
import com.rizwansayyed.zene.data.db.offlinedownload.OfflineDownloadedEntity
import com.rizwansayyed.zene.data.db.recentplay.RecentPlayedDao
import com.rizwansayyed.zene.data.db.recentplay.RecentPlayerDB
import com.rizwansayyed.zene.data.db.utils.DbName.offline_downloaded_songs_db
import com.rizwansayyed.zene.data.db.utils.DbName.recent_played_db
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
object RoomModule {

    @Provides
    @Singleton
    fun recentPlayedDao(
        @ApplicationContext context: Context
    ): RecentPlayedDao = Room.databaseBuilder(context, RecentPlayerDB::class.java, recent_played_db)
        .addMigrations(MIGRATION_1_2_RECENT_PLAYED).build().dao()

    private val MIGRATION_1_2_RECENT_PLAYED = object : Migration(1, 2) {
        override fun migrate(db: SupportSQLiteDatabase) {
            db.execSQL("ALTER TABLE $recent_played_db RENAME COLUMN img TO thumbnail")
        }
    }


    @Provides
    @Singleton
    fun offlineDownloadedDao(
        @ApplicationContext context: Context
    ): OfflineDownloadedDao = Room.databaseBuilder(context, OfflineDownloadedDB::class.java, offline_downloaded_songs_db)
        .addMigrations(MIGRATION_1_2_OFFLINE_DOWNLOADED).build().dao()

    private val MIGRATION_1_2_OFFLINE_DOWNLOADED = object : Migration(1, 2) {
        override fun migrate(db: SupportSQLiteDatabase) {
            db.execSQL("ALTER TABLE $offline_downloaded_songs_db RENAME COLUMN img TO thumbnail")
        }
    }

}