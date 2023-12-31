package com.rizwansayyed.zene.di.onlinemodule

import com.rizwansayyed.zene.data.onlinesongs.applemusic.implementation.AppleMusicAPIImpl
import com.rizwansayyed.zene.data.onlinesongs.applemusic.implementation.AppleMusicAPIImplInterface
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
abstract class AppleMusicImplementation {

    @Binds
    @Singleton
    abstract fun appleMusicAPIImpl(appleMusicAPIImpl: AppleMusicAPIImpl): AppleMusicAPIImplInterface

}