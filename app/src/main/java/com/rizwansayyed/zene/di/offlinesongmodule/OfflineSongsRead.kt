package com.rizwansayyed.zene.di.offlinesongmodule

import com.rizwansayyed.zene.data.offlinesongs.OfflineSongReadImpl
import com.rizwansayyed.zene.data.offlinesongs.OfflineSongsReadInterface
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
abstract class OfflineSongsRead {

    @Binds
    @Singleton
    abstract fun offlineSongs(offlineSongReadImpl: OfflineSongReadImpl): OfflineSongsReadInterface

}