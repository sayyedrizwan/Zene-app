package com.rizwansayyed.zene.di.onlinemodule

import com.rizwansayyed.zene.data.onlinesongs.applemusic.implementation.AppleMusicAPIImpl
import com.rizwansayyed.zene.data.onlinesongs.applemusic.implementation.AppleMusicAPIImplInterface
import com.rizwansayyed.zene.data.onlinesongs.giphy.implementation.GiphyImpl
import com.rizwansayyed.zene.data.onlinesongs.giphy.implementation.GiphyImplInterface
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
abstract class GiphyImplementation {

    @Binds
    @Singleton
    abstract fun giphyImpl(giphyImpl: GiphyImpl): GiphyImplInterface

}