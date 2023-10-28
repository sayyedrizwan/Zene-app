package com.rizwansayyed.zene.data.onlinesongs.downloader.implementation

import com.rizwansayyed.zene.domain.SongDownloadResponse
import kotlinx.coroutines.flow.Flow

interface SongDownloaderInterface {

    suspend fun download(songId: String): Flow<String?>
}