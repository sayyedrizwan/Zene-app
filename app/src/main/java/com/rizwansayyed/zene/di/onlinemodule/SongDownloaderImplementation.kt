package com.rizwansayyed.zene.di.onlinemodule

import com.rizwansayyed.zene.data.onlinesongs.downloader.implementation.SongDownloaderImpl
import com.rizwansayyed.zene.data.onlinesongs.downloader.implementation.SongDownloaderInterface
import com.rizwansayyed.zene.data.onlinesongs.youtube.implementation.YoutubeAPIImpl
import com.rizwansayyed.zene.data.onlinesongs.youtube.implementation.YoutubeAPIImplInterface
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class SongDownloaderImplementation {

    @Binds
    @Singleton
    abstract fun songDownloaderImpl(songDownloaderImpl: SongDownloaderImpl): SongDownloaderInterface
}