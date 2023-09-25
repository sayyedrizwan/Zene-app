package com.rizwansayyed.zene.data.onlinesongs.youtube.implementation

import com.rizwansayyed.zene.domain.MusicData
import kotlinx.coroutines.flow.Flow

interface YoutubeAPIImplInterface {

    suspend fun newReleaseMusic(): Flow<List<MusicData>>
}