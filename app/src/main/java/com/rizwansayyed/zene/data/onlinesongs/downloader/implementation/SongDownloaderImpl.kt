package com.rizwansayyed.zene.data.onlinesongs.downloader.implementation

import com.maxrave.kotlinyoutubeextractor.State
import com.maxrave.kotlinyoutubeextractor.YTExtractor
import com.maxrave.kotlinyoutubeextractor.getAudioOnly
import com.rizwansayyed.zene.data.onlinesongs.downloader.SongDownloaderService
import com.rizwansayyed.zene.di.ApplicationModule.Companion.context
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject

class SongDownloaderImpl @Inject constructor() : SongDownloaderInterface {

    override suspend fun download(songId: String) = flow {
        val yt = YTExtractor(context, true, LOGGING = true, retryCount = 2).apply {
            extract(songId)
        }

        if (yt.state == State.SUCCESS) {
            val url = yt.getYTFiles()?.getAudioOnly()?.maxBy { it.meta?.audioBitrate ?: 0 }
            emit(url?.url)
            return@flow
        }

        emit("")
        return@flow


//        val downloader = songDownloaderService.download(watchUrl = ytURL(songId))
//        val id = downloader.result!!.mp3_task_data!!.tid ?: ""
//        val convertor = songDownloaderService.converter(ytConvertor(id))
//        if (convertor.error?.message == null && convertor.result?.download_url != null) {
//            val playURL = "https://yt.fabdl.com${convertor.result.download_url}"
//            emit(playURL)
//            return@flow
//        }
//
//        val downloaderKV = keepVidScrap.getTsId(songId)
//        val convertorKV = songDownloaderService.convertorKeepVid(
//            referer = keepVidButtonBaseURL(songId), body = keepVidConvertor(downloaderKV)
//        )
//
//        val downloadResponse = songDownloaderService.downloaderKeepVid(
//            referer = keepVidButtonBaseURL(songId),
//            jobId = convertorKV.jobid, time = downloaderKV?.token_validto ?: ""
//        )
//
//        emit(downloadResponse.dlurl)
    }.flowOn(Dispatchers.IO)
}