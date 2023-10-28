package com.rizwansayyed.zene.di

import android.content.Context
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.session.MediaSession
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
object ExoPlayerModule {

    @Provides
    @Singleton
    fun exoPlayer(@ApplicationContext c: Context): ExoPlayer =
        ExoPlayer.Builder(c).build()

    @Provides
    @Singleton
    fun mediaSession(@ApplicationContext c: Context, e: ExoPlayer): MediaSession =
        MediaSession.Builder(c, e).build()
}