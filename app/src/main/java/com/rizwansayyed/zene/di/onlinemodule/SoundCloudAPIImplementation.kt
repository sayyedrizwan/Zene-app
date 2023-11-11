package com.rizwansayyed.zene.di.onlinemodule

import com.rizwansayyed.zene.data.onlinesongs.soundcloud.implementation.SoundCloudImpl
import com.rizwansayyed.zene.data.onlinesongs.soundcloud.implementation.SoundCloudImplInterface
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