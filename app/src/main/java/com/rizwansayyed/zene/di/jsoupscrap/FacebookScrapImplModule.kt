package com.rizwansayyed.zene.di.jsoupscrap

import com.rizwansayyed.zene.data.onlinesongs.jsoupscrap.bing.BingScrapsImpl
import com.rizwansayyed.zene.data.onlinesongs.jsoupscrap.bing.BingScrapsInterface
import com.rizwansayyed.zene.data.onlinesongs.jsoupscrap.facebook.FacebookScrapsImpl
import com.rizwansayyed.zene.data.onlinesongs.jsoupscrap.facebook.FacebookScrapsImplInterface
import com.rizwansayyed.zene.data.onlinesongs.jsoupscrap.topartistsplaylists.TopArtistsPlaylistsScrapsImpl
import com.rizwansayyed.zene.data.onlinesongs.jsoupscrap.topartistsplaylists.TopArtistsPlaylistsScrapsInterface
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
abstract class FacebookScrapImplModule {

    @Binds
    @Singleton
    abstract fun facebookScrapImpl(facebookScrapsImpl: FacebookScrapsImpl): FacebookScrapsImplInterface

}
