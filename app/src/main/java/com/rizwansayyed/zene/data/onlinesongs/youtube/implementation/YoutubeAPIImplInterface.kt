package com.rizwansayyed.zene.data.onlinesongs.youtube.implementation

import kotlinx.coroutines.flow.Flow

interface YoutubeAPIImplInterface {
    suspend fun newReleaseMusic(): Flow<String?>
}