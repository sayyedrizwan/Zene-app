package com.rizwansayyed.zene.data.onlinesongs.downloader.implementation

import com.rizwansayyed.zene.domain.download.SaveFromVideoResponse
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.Flow

interface SongDownloaderInterface {
    var downloadJob: Job?

    suspend fun download(songId: String): Flow<String?>
    suspend fun downloadVideo(vId: String): Flow<String?>
    suspend fun downloadVideoHD(vId: String): Flow<SaveFromVideoResponse>
}