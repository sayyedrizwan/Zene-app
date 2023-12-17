package com.rizwansayyed.zene.data.onlinesongs.jsoupscrap.social

import com.rizwansayyed.zene.data.db.artistspin.PinnedArtistsEntity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job


interface SocialMediaScrapsImplInterface {

    val job: CoroutineScope get() = CoroutineScope(Dispatchers.IO)

    suspend fun getAllArtistsData(a: PinnedArtistsEntity): Job
}