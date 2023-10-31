package com.rizwansayyed.zene.data.onlinesongs.downloader.implementation

import android.util.Log
import com.rizwansayyed.zene.data.onlinesongs.downloader.SongDownloaderService
import com.rizwansayyed.zene.data.onlinesongs.jsoupscrap.keepvid.KeepVidScrapInfo
import com.rizwansayyed.zene.data.utils.SongDownloader
import com.rizwansayyed.zene.data.utils.SongDownloader.keepVidButtonBaseURL
import com.rizwansayyed.zene.data.utils.SongDownloader.ytConvertor
import com.rizwansayyed.zene.data.utils.SongDownloader.ytURL
import com.rizwansayyed.zene.data.utils.YoutubeAPI.keepVidConvertor
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject

class SongDownloaderImpl @Inject constructor(
    private val songDownloaderService: SongDownloaderService,
    private val keepVidScrap: KeepVidScrapInfo
) : SongDownloaderInterface {

    override suspend fun download(songId: String) = flow {
        val downloader = songDownloaderService.download(watchUrl = ytURL(songId))
        val id = downloader.result!!.mp3_task_data!!.tid ?: ""
        val convertor = songDownloaderService.converter(ytConvertor(id))
        if (convertor.error?.message == null) {
            val playURL = "https://yt.fabdl.com${convertor.result?.download_url}"
            emit(playURL)

            return@flow
        }

        val downloaderKV = keepVidScrap.getTsId(songId)
        val convertorKV = songDownloaderService.convertorKeepVid(
            referer = keepVidButtonBaseURL(songId), body = keepVidConvertor(downloaderKV)
        )

        val downloadResponse = songDownloaderService.downloaderKeepVid(
            referer = keepVidButtonBaseURL(songId),
            jobId = convertorKV.jobid, time = downloaderKV?.token_validto ?: ""
        )
        emit(downloadResponse.dlurl)
    }.flowOn(Dispatchers.IO)
}