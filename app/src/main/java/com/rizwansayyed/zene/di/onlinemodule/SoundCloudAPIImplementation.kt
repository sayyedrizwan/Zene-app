package com.rizwansayyed.zene.di.onlinemodule

import com.rizwansayyed.zene.data.onlinesongs.soundcloud.implementation.SoundCloudImpl
import com.rizwansayyed.zene.data.onlinesongs.soundcloud.implementation.SoundCloudImplInterface
import com.rizwansayyed.zene.data.onlinesongs.spotify.implementation.SpotifyAPIImpl
import com.rizwansayyed.zene.data.onlinesongs.spotify.implementation.SpotifyAPIImplInterface
import com.rizwansayyed.zene.data.onlinesongs.youtube.implementation.YoutubeAPIImpl
import com.rizwansayyed.zene.data.onlinesongs.youtube.implementation.YoutubeAPIImplInterface
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
abstract class SoundCloudAPIImplementation {

    @Binds
    @Singleton
    abstract fun soundCloudAPIImpl(soundCloudImpl: SoundCloudImpl): SoundCloudImplInterface

}