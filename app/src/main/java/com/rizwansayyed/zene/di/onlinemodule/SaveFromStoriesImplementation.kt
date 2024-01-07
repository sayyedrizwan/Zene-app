package com.rizwansayyed.zene.di.onlinemodule

import com.rizwansayyed.zene.data.onlinesongs.instagram.implementation.SaveFromStoriesImpl
import com.rizwansayyed.zene.data.onlinesongs.instagram.implementation.SaveFromStoriesImplInterface
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