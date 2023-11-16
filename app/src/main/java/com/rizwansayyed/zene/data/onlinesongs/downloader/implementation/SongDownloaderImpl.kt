package com.rizwansayyed.zene.data.onlinesongs.downloader.implementation

import android.util.Log
import com.maxrave.kotlinyoutubeextractor.State
import com.maxrave.kotlinyoutubeextractor.YTExtractor
import com.maxrave.kotlinyoutubeextractor.getAudioOnly
import com.maxrave.kotlinyoutubeextractor.getVideoOnly
import com.rizwansayyed.zene.data.onlinesongs.downloader.SaveFromDownloaderService
import com.rizwansayyed.zene.data.onlinesongs.jsoupscrap.jsoupResponseData
import com.rizwansayyed.zene.data.utils.SongDownloader.ytURL
import com.rizwansayyed.zene.data.utils.VideoDownloaderAPI.SAVE_FROM_BASE_URL
import com.rizwansayyed.zene.di.ApplicationModule.Companion.context
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import org.jsoup.Connection
import org.jsoup.Jsoup
import javax.inject.Inject

class SongDownloaderImpl @Inject constructor(private val saveFromDownloader: SaveFromDownloaderService) :
    SongDownloaderInterface {

    override suspend fun download(songId: String) = flow {
        val yt = YTExtractor(context, true, LOGGING = false, retryCount = 2).apply {
            extract(songId)
        }

        if (yt.state == State.SUCCESS) {
            val url = yt.getYTFiles()?.getAudioOnly()?.maxBy { it.meta?.audioBitrate ?: 0 }
            emit(url?.url)
            return@flow
        }

        emit("")
    }.flowOn(Dispatchers.IO)


    override suspend fun downloadVideo(vId: String) = flow {
        val yt = YTExtractor(context, true, LOGGING = true, retryCount = 2).apply {
            extract(vId)
        }

        if (yt.state == State.SUCCESS) {
            val url = yt.getYTFiles()?.getVideoOnly()?.maxBy { it.meta?.audioBitrate ?: 0 }
            emit(url?.url)
            return@flow
        }

//        val res = Jsoup
//            .connect(SAVE_FROM_BASE_URL).method(Connection.Method.GET).execute()
//        val jsoup = Jsoup.connect(SAVE_FROM_BASE_URL).cookies(res.cookies()).get()
//        val token = jsoup.select("#token").attr("value")
//
//        emit(saveFromDownloader.download(ytURL(vId), token))
    }.flowOn(Dispatchers.IO)


    override suspend fun downloadVideoHD(vId: String) = flow {
        val res = Jsoup
            .connect(SAVE_FROM_BASE_URL).method(Connection.Method.GET).execute()
        val jsoup = Jsoup.connect(SAVE_FROM_BASE_URL).cookies(res.cookies()).get()
        val token = jsoup.select("#token").attr("value")

        emit(saveFromDownloader.download(ytURL(vId), token))
    }.flowOn(Dispatchers.IO)
}