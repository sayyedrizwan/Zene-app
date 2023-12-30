package com.rizwansayyed.zene.di.onlinemodule

import com.rizwansayyed.zene.data.onlinesongs.spotify.music.implementation.SpotifyAPIImpl
import com.rizwansayyed.zene.data.onlinesongs.spotify.music.implementation.SpotifyAPIImplInterface
import com.rizwansayyed.zene.data.onlinesongs.spotify.users.implementation.SpotifyUsersAPIImpl
import com.rizwansayyed.zene.data.onlinesongs.spotify.users.implementation.SpotifyUsersAPIImplInterface
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

    @Binds
    @Singleton
    abstract fun spotifyUserAPIImpl(spotifyUsersAPIImpl: SpotifyUsersAPIImpl): SpotifyUsersAPIImplInterface

}