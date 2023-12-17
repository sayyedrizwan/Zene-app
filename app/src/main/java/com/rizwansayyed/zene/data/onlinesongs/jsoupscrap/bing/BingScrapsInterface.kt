package com.rizwansayyed.zene.data.onlinesongs.jsoupscrap.bing

import com.rizwansayyed.zene.data.db.artistspin.PinnedArtistsEntity
import kotlinx.coroutines.flow.Flow

interface BingScrapsInterface {
    suspend fun bingOfficialVideo(a: String): Flow<String>
    suspend fun bingOfficialAccounts(a: String): Flow<PinnedArtistsEntity>
}