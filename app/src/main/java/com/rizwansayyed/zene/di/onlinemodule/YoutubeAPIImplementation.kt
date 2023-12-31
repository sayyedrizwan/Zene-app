package com.rizwansayyed.zene.di.onlinemodule

import com.rizwansayyed.zene.data.onlinesongs.spotify.music.implementation.SpotifyAPIImpl
import com.rizwansayyed.zene.data.onlinesongs.spotify.music.implementation.SpotifyAPIImplInterface
import com.rizwansayyed.zene.data.onlinesongs.youtube.implementation.YoutubeAPIImpl
import com.rizwansayyed.zene.data.onlinesongs.youtube.implementation.YoutubeAPIImplInterface
import com.rizwansayyed.zene.data.onlinesongs.youtube.implementation.userplaylist.YoutubeMusicPlaylistImpl
import com.rizwansayyed.zene.data.onlinesongs.youtube.implementation.userplaylist.YoutubeMusicPlaylistImplInterface
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
abstract class YoutubeAPIImplementation {

    @Binds
    @Singleton
    abstract fun youtubeAPIImpl(youtubeAPIImpl: YoutubeAPIImpl): YoutubeAPIImplInterface

    @Binds
    @Singleton
    abstract fun youtubeMusicPlaylistImpl(youtubeMusicPlaylistImpl: YoutubeMusicPlaylistImpl): YoutubeMusicPlaylistImplInterface

}