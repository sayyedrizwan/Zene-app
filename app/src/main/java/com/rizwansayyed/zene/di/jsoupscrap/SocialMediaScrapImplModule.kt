package com.rizwansayyed.zene.di.jsoupscrap

import com.rizwansayyed.zene.data.onlinesongs.jsoupscrap.social.SocialMediaScrapsImpl
import com.rizwansayyed.zene.data.onlinesongs.jsoupscrap.social.SocialMediaScrapsImplInterface
import com.rizwansayyed.zene.data.onlinesongs.jsoupscrap.topartistsplaylists.TopArtistsPlaylistsScrapsImpl
import com.rizwansayyed.zene.data.onlinesongs.jsoupscrap.topartistsplaylists.TopArtistsPlaylistsScrapsInterface
import com.rizwansayyed.zene.data.onlinesongs.jsoupscrap.youtubescrap.YoutubeScrapInterface
import com.rizwansayyed.zene.data.onlinesongs.jsoupscrap.youtubescrap.YoutubeScrapsImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
abstract class SocialMediaScrapImplModule {

    @Binds
    @Singleton
    abstract fun socialMediaScrapImpl(socialMediaScrapsImpl: SocialMediaScrapsImpl): SocialMediaScrapsImplInterface

}
