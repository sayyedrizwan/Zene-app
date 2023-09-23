package com.rizwansayyed.zene.di.remotemodule

import com.rizwansayyed.zene.data.db.impl.RoomDBImpl
import com.rizwansayyed.zene.data.db.impl.RoomDBInterface
import com.rizwansayyed.zene.data.offlinesongs.OfflineSongReadImpl
import com.rizwansayyed.zene.data.offlinesongs.OfflineSongsReadInterface
import com.rizwansayyed.zene.data.utils.config.RemoteConfigInterface
import com.rizwansayyed.zene.data.utils.config.RemoteConfigManager
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
