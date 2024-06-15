package com.rizwansayyed.zene.data.onlinesongs.downloader.implementation

import android.util.Log
import com.rizwansayyed.zene.data.db.datastore.DataStorageSettingsManager.songQualitySettings
import com.rizwansayyed.zene.data.db.datastore.SongsQualityInfo
import com.rizwansayyed.zene.service.youtubedownloader.State
import com.rizwansayyed.zene.service.youtubedownloader.YTExtractor
import com.rizwansayyed.zene.service.youtubedownloader.getAudioOnly
import com.rizwansayyed.zene.service.youtubedownloader.getVideoOnly
import com.rizwansayyed.zene.data.onlinesongs.downloader.SaveFromDownloaderService
import com.rizwansayyed.zene.data.utils.SongDownloader.ytURL
import com.rizwansayyed.zene.data.utils.VideoDownloaderAPI.SAVE_FROM_BASE_URL
import com.rizwansayyed.zene.di.ApplicationModule.Companion.context
import com.rizwansayyed.zene.utils.Utils.isConnectedToWifi
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.jsoup.Connection
import org.jsoup.Jsoup
import javax.inject.Inject
import kotlin.coroutines.coroutineContext
import kotlin.time.Duration.Companion.seconds

class SongDownloaderImpl @Inject constructor(private val saveFromDownloader: SaveFromDownloaderService) :
    SongDownloaderInterface {

    override var downloadJob: Job? = null
    override suspend fun download(songId: String) = flow {
        emit("https://zenemusic.co/api/sduapp?id=${songId}")
//        val yt = YTExtractor(context, true, LOGGING = false, retryCount = 2).apply {
//            extract(songId)
//        }
//        if (yt.state == State.SUCCESS) {
//            val ytData = yt.getYTFiles()?.getAudioOnly()
//            val url = when (songQualitySettings.first()) {
//                SongsQualityInfo.LOW_QUALITY.v -> ytData?.minBy {
//                    it.meta?.audioBitrate ?: 0
//                }?.url
//
//                SongsQualityInfo.HIGH_QUALITY_WIFI.v -> if (isConnectedToWifi())
//                    ytData?.maxBy { it.meta?.audioBitrate ?: 0 }?.url
//                else
//                    ytData?.minBy { it.meta?.audioBitrate ?: 0 }?.url
//
//                else -> ytData?.maxBy { it.meta?.audioBitrate ?: 0 }?.url
//
//            }
//            emit(url)
//        } else
//            emit("")
    }.flowOn(Dispatchers.IO)


    override suspend fun downloadVideo(vId: String) = flow {
        val yt = YTExtractor(context, true, LOGGING = false, retryCount = 2).apply {
            extract(vId)
        }

        if (yt.state == State.SUCCESS) {
            val url = yt.getYTFiles()?.getVideoOnly()?.maxBy { it.meta?.audioBitrate ?: 0 }
            emit(url?.url)
            return@flow
        }
    }.flowOn(Dispatchers.IO)


    override suspend fun downloadVideoHD(vId: String) = flow {
        val res = Jsoup
            .connect(SAVE_FROM_BASE_URL).method(Connection.Method.GET).execute()
        val jsoup = Jsoup.connect(SAVE_FROM_BASE_URL).cookies(res.cookies()).get()
        val token = jsoup.select("#token").attr("value")

        emit(saveFromDownloader.download(ytURL(vId), token))
    }.flowOn(Dispatchers.IO)
}