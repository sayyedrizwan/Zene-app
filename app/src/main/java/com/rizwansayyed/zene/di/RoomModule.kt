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
import com.rizwansayyed.zene.data.db.savedplaylist.playlistsongs.PlaylistSongsDB
import com.rizwansayyed.zene.data.db.savedplaylist.playlistsongs.PlaylistSongsDao
import com.rizwansayyed.zene.data.utils.DBNAME.OFFLINE_DOWNLOADED_SONGS_DB
import com.rizwansayyed.zene.data.utils.DBNAME.PLAYLIST_SONGS_DB
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
    ): RecentPlayedDao = synchronized(Unit) {
        Room.databaseBuilder(context, RecentPlayerDB::class.java, RECENT_PLAYED_DB)
            .fallbackToDestructiveMigration().build().dao()
    }


    @Provides
    @Singleton
    fun offlineDownloadedDao(
        @ApplicationContext context: Context
    ): OfflineDownloadedDao =
        Room.databaseBuilder(context, OfflineDownloadedDB::class.java, OFFLINE_DOWNLOADED_SONGS_DB)
            .fallbackToDestructiveMigration().build().dao()


    @Provides
    @Singleton
    fun savedPlaylistDao(
        @ApplicationContext context: Context
    ): SavedPlaylistDao =
        Room.databaseBuilder(context, SavedPlaylistDB::class.java, SAVED_PLAYLIST_DB)
            .fallbackToDestructiveMigration().build().dao()


    @Provides
    @Singleton
    fun playlistSongsDao(
        @ApplicationContext context: Context
    ): PlaylistSongsDao =
        Room.databaseBuilder(context, PlaylistSongsDB::class.java, PLAYLIST_SONGS_DB)
            .fallbackToDestructiveMigration().build().dao()

}