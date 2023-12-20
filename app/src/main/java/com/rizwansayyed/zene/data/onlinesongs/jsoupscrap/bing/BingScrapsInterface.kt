package com.rizwansayyed.zene.data.onlinesongs.jsoupscrap.bing

import com.rizwansayyed.zene.data.db.artistspin.PinnedArtistsEntity
import com.rizwansayyed.zene.domain.news.BingNews
import kotlinx.coroutines.flow.Flow

interface BingScrapsInterface {
    suspend fun bingOfficialVideo(a: String): Flow<String>

    suspend fun bingNews(artists: String): Flow<MutableList<BingNews>>

    suspend fun bingOfficialAccounts(info: PinnedArtistsEntity): Flow<PinnedArtistsEntity>
}