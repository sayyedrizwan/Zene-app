package com.rizwansayyed.zene.di.onlinemodule

import com.rizwansayyed.zene.data.onlinesongs.spotify.implementation.SpotifyAPIImpl
import com.rizwansayyed.zene.data.onlinesongs.spotify.implementation.SpotifyAPIImplInterface
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
abstract class SpotifyAPIImplementation {

    @Binds
    @Singleton
    abstract fun spotifyAPIImpl(spotifyAPIImpl: SpotifyAPIImpl): SpotifyAPIImplInterface

}