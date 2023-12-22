package com.rizwansayyed.zene.data.onlinesongs.jsoupscrap.social

import com.rizwansayyed.zene.data.db.artistspin.PinnedArtistsEntity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.withContext


interface SocialMediaScrapsImplInterface {

    suspend fun getAllArtistsData(a: PinnedArtistsEntity): Any
}