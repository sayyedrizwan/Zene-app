package com.rizwansayyed.zene.data.onlinesongs.jsoupscrap.soundcloud

import com.rizwansayyed.zene.domain.MusicPlayerList
import kotlinx.coroutines.flow.Flow

interface SoundcloudScrapImplInterface {
    suspend fun getClientId(): Flow<String>
}