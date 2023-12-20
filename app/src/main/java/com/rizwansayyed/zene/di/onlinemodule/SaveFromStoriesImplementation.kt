package com.rizwansayyed.zene.di.onlinemodule

import com.rizwansayyed.zene.data.onlinesongs.instagram.stories.SaveFromStoriesImpl
import com.rizwansayyed.zene.data.onlinesongs.instagram.stories.SaveFromStoriesImplInterface
import com.rizwansayyed.zene.data.onlinesongs.ip.implementation.IpJsonImpl
import com.rizwansayyed.zene.data.onlinesongs.ip.implementation.IpJsonImplInterface
import com.rizwansayyed.zene.data.onlinesongs.radio.implementation.OnlineRadioImpl
import com.rizwansayyed.zene.data.onlinesongs.radio.implementation.OnlineRadioImplInterface
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
abstract class SaveFromStoriesImplementation {

    @Binds
    @Singleton
    abstract fun saveFromStoriesImpl(saveFromStoriesImpl: SaveFromStoriesImpl): SaveFromStoriesImplInterface

}