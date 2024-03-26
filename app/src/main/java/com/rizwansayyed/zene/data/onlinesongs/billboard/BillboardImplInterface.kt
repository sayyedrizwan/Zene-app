package com.rizwansayyed.zene.data.onlinesongs.billboard

import com.rizwansayyed.zene.domain.MusicData
import com.rizwansayyed.zene.domain.instagram.SaveFromInstagramStoriesResponse
import kotlinx.coroutines.flow.Flow
import java.io.File

interface BillboardImplInterface {
    suspend fun topSongs(): Flow<List<MusicData>>
}