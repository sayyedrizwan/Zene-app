package com.rizwansayyed.zene.di.jsoupscrap

import com.rizwansayyed.zene.data.onlinesongs.jsoupscrap.bing.BingScrapsImpl
import com.rizwansayyed.zene.data.onlinesongs.jsoupscrap.bing.BingScrapsInterface
import com.rizwansayyed.zene.data.onlinesongs.jsoupscrap.soundcloud.SoundcloudScrapImplInterface
import com.rizwansayyed.zene.data.onlinesongs.jsoupscrap.soundcloud.SoundcloudScrapsImpl
import com.rizwansayyed.zene.data.onlinesongs.jsoupscrap.topartistsplaylists.TopArtistsPlaylistsScrapsImpl
import com.rizwansayyed.zene.data.onlinesongs.jsoupscrap.topartistsplaylists.TopArtistsPlaylistsScrapsInterface
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
abstract class SoundcloudScrapImplementation {

    @Binds
    @Singleton
    abstract fun soundcloudScrapImpl(soundcloudScrapsImpl: SoundcloudScrapsImpl): SoundcloudScrapImplInterface

}
