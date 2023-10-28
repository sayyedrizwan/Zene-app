package com.rizwansayyed.zene.di

import android.content.Context
import androidx.media3.common.AudioAttributes
import androidx.media3.common.C
import androidx.media3.common.util.UnstableApi
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.exoplayer.trackselection.DefaultTrackSelector
import androidx.media3.session.MediaSession
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
object ExoPlayerModule {

    private val audioAttributes = AudioAttributes.Builder()
        .setContentType(C.AUDIO_CONTENT_TYPE_MOVIE)
        .setUsage(C.USAGE_MEDIA)
        .build()


    @Provides
    @Singleton
    @UnstableApi
    fun exoPlayer(@ApplicationContext c: Context): ExoPlayer =
        ExoPlayer.Builder(c)
            .setAudioAttributes(audioAttributes, true)
            .setHandleAudioBecomingNoisy(true)
            .setTrackSelector(DefaultTrackSelector(c)).build()

    @Provides
    @Singleton
    fun mediaSession(@ApplicationContext c: Context, e: ExoPlayer): MediaSession =
        MediaSession.Builder(c, e).build()
}