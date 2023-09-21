package com.rizwansayyed.zene.di

import android.content.Context
import androidx.room.Room
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import com.rizwansayyed.zene.data.db.offlinedownload.OfflineDownloadedDB
import com.rizwansayyed.zene.data.db.offlinedownload.OfflineDownloadedDao
import com.rizwansayyed.zene.data.db.recentplay.RecentPlayedDao
import com.rizwansayyed.zene.data.db.recentplay.RecentPlayerDB
import com.rizwansayyed.zene.data.db.savedplaylist.playlist.SavedPlaylistDB
import com.rizwansayyed.zene.data.db.savedplaylist.playlist.SavedPlaylistDao
import com.rizwansayyed.zene.data.utils.DBNAME.OFFLINE_DOWNLOADED_SONGS_DB
import com.rizwansayyed.zene.data.utils.DBNAME.RECENT_PLAYED_DB
import com.rizwansayyed.zene.data.utils.DBNAME.SAVED_PLAYLIST_DB
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
    ): RecentPlayedDao = Room.databaseBuilder(context, RecentPlayerDB::class.java, RECENT_PLAYED_DB)
        .addMigrations(MIGRATION_2_3_RECENT_PLAYED).build().dao()

    private val MIGRATION_2_3_RECENT_PLAYED = object : Migration(2, 3) {
        override fun migrate(db: SupportSQLiteDatabase) {
            try {
                db.execSQL("ALTER TABLE $RECENT_PLAYED_DB RENAME COLUMN img TO thumbnail")
            } catch (e: Exception) {
                e.message
            }
        }
    }


    @Provides
    @Singleton
    fun offlineDownloadedDao(
        @ApplicationContext context: Context
    ): OfflineDownloadedDao =
        Room.databaseBuilder(context, OfflineDownloadedDB::class.java, OFFLINE_DOWNLOADED_SONGS_DB)
            .addMigrations(MIGRATION_1_2_OFFLINE_DOWNLOADED).build().dao()

    private val MIGRATION_1_2_OFFLINE_DOWNLOADED = object : Migration(1, 2) {
        override fun migrate(db: SupportSQLiteDatabase) {
            try {
                db.execSQL("ALTER TABLE $OFFLINE_DOWNLOADED_SONGS_DB RENAME COLUMN img TO thumbnail")
                db.execSQL("ALTER TABLE $OFFLINE_DOWNLOADED_SONGS_DB ADD COLUMN viewed INTEGER NOT NULL DEFAULT 0")
            } catch (e: Exception) {
                e.message
            }
        }
    }


    @Provides
    @Singleton
    fun savedPlaylistDao(
        @ApplicationContext context: Context
    ): SavedPlaylistDao =
        Room.databaseBuilder(context, SavedPlaylistDB::class.java, SAVED_PLAYLIST_DB)
            .build().dao()

}