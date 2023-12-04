package com.rizwansayyed.zene.di

import android.content.Context
import android.util.Log
import androidx.annotation.OptIn
import androidx.media3.common.AudioAttributes
import androidx.media3.common.C
import androidx.media3.common.util.UnstableApi
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.exoplayer.trackselection.DefaultTrackSelector
import androidx.media3.session.MediaSession
import com.rizwansayyed.zene.service.player.ArtistsThumbnailVideoPlayer
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton
import kotlin.time.Duration.Companion.seconds


@Module
@InstallIn(SingletonComponent::class)
object ExoPlayerModule {

    @Provides
    @Singleton
    fun audioAttributes(): AudioAttributes =
        AudioAttributes.Builder()
            .setContentType(C.AUDIO_CONTENT_TYPE_MOVIE)
            .setUsage(C.USAGE_MEDIA)
            .build()

    @Provides
    @Singleton
    @UnstableApi
    fun exoPlayer(@ApplicationContext c: Context, audio: AudioAttributes): ExoPlayer =
        ExoPlayer.Builder(c)
            .setAudioAttributes(audio, true)
            .setHandleAudioBecomingNoisy(true)
            .setSeekForwardIncrementMs(5.seconds.inWholeMilliseconds)
            .setSeekBackIncrementMs(5.seconds.inWholeMilliseconds)
            .setTrackSelector(DefaultTrackSelector(c)).build()

    @Provides
    @Singleton
    fun mediaSession(@ApplicationContext c: Context, e: ExoPlayer): MediaSession =
        MediaSession.Builder(c, e).build()

    @OptIn(UnstableApi::class)
    @Provides
    @Singleton
    fun androidExoPlayer(@ApplicationContext c: Context) = ArtistsThumbnailVideoPlayer(c)

}