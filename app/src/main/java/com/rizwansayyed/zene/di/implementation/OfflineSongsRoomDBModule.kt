package com.rizwansayyed.zene.di.implementation

import com.rizwansayyed.zene.data.roomdb.offlinesongs.implementation.OfflineSongsDBInterface
import com.rizwansayyed.zene.data.roomdb.offlinesongs.implementation.OfflineSongsRoomDBImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent

@Module
@InstallIn(ViewModelComponent::class)
abstract class OfflineSongsRoomDBModule {

    @Binds
    abstract fun offlineDBRoomDBImplementation(impl: OfflineSongsRoomDBImpl): OfflineSongsDBInterface
}