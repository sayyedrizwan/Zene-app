package com.rizwansayyed.zene.di.jsoupscrap

import com.rizwansayyed.zene.data.onlinesongs.jsoupscrap.topartistsplaylists.TopArtistsPlaylistsScrapsImpl
import com.rizwansayyed.zene.data.onlinesongs.jsoupscrap.topartistsplaylists.TopArtistsPlaylistsScrapsInterface
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
abstract class JsoupScrapImplementation {

    @Binds
    @Singleton
    abstract fun jsoupScrapImpl(jsoupScrapTopArtistsImpl: TopArtistsPlaylistsScrapsImpl): TopArtistsPlaylistsScrapsInterface

}
