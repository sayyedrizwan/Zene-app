package com.rizwansayyed.zene.data.onlinesongs.downloader.implementation

import android.util.Log
import com.rizwansayyed.zene.data.onlinesongs.downloader.SongDownloaderService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject

class SongDownloaderImpl @Inject constructor(private val songDownloaderService: SongDownloaderService) :
    SongDownloaderInterface {

    override suspend fun download(songId: String) = flow {
        val downloader = songDownloaderService.download(songId)
         val url = downloader.audioStreams
            ?.filter { it?.mimeType?.lowercase()?.trim() == "audio/mp4" }
            ?.maxByOrNull { it?.bitrate ?: 0 }
        emit(url?.url)
    }.flowOn(Dispatchers.IO)
}