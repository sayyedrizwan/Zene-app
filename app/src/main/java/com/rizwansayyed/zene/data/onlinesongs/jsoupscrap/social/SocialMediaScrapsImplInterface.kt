package com.rizwansayyed.zene.data.onlinesongs.jsoupscrap.social

import com.rizwansayyed.zene.domain.ArtistsEvents
import kotlinx.coroutines.flow.Flow


interface SocialMediaScrapsImplInterface {
    suspend fun getAllArtistsData(a: String): Flow<String>
}