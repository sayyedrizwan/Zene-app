package com.rizwansayyed.zene.data.onlinesongs.downloader.implementation

import com.rizwansayyed.zene.data.onlinesongs.downloader.SongDownloaderService
import com.rizwansayyed.zene.data.utils.SongDownloader.ytConvertor
import com.rizwansayyed.zene.data.utils.SongDownloader.ytURL
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject

class SongDownloaderImpl @Inject constructor(private val songDownloaderService: SongDownloaderService) :
    SongDownloaderInterface {

    override suspend fun download(songId: String) = flow {
        val downloader = songDownloaderService.download(watchUrl = ytURL(songId))
        val id = downloader.result!!.mp3_task_data!!.tid ?: ""
        val convertor = songDownloaderService.converter(ytConvertor(id))
        val playURL = "https://yt.fabdl.com${convertor.result?.download_url}"
        emit(playURL)
    }.flowOn(Dispatchers.IO)
}