package com.rizwansayyed.zene.data.onlinesongs.jsoupscrap.facebook

import com.rizwansayyed.zene.data.db.artistspin.PinnedArtistsEntity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.Flow


interface FacebookScrapsImplInterface {

    suspend fun pageCreds(a: String): Flow<String>
}