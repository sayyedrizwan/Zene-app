package com.rizwansayyed.zene.di.implementation

import com.rizwansayyed.zene.data.roomdb.offlinesongs.implementation.OfflineSongsDBInterface
import com.rizwansayyed.zene.data.roomdb.offlinesongs.implementation.OfflineSongsRoomDBImpl
import com.rizwansayyed.zene.data.roomdb.zeneconnect.implementation.ZeneConnectRoomDBImpl
import com.rizwansayyed.zene.data.roomdb.zeneconnect.implementation.ZeneConnectRoomDBInterface
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent

@Module
@InstallIn(ViewModelComponent::class)
abstract class ZeneConnectRoomDBModule {

    @Binds
    abstract fun zeneConnectRoomDBImplementation(impl: ZeneConnectRoomDBImpl): ZeneConnectRoomDBInterface
}