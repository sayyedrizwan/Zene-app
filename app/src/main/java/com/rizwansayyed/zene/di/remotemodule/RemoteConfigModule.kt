package com.rizwansayyed.zene.di.remotemodule

import com.rizwansayyed.zene.data.onlinesongs.config.implementation.RemoteConfigInterface
import com.rizwansayyed.zene.data.onlinesongs.config.implementation.RemoteConfigManager
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
abstract class RemoteConfigModule {

    @Binds
    @Singleton
    abstract fun remoteConfigManager(remote: RemoteConfigManager): RemoteConfigInterface

}
