package com.rizwansayyed.zene.data.onlinesongs.auddrecognition.implementation

import android.util.Log
import com.rizwansayyed.zene.data.db.datastore.DataStorageManager
import com.rizwansayyed.zene.data.db.datastore.DataStorageManager.userIpDetails
import com.rizwansayyed.zene.data.db.datastore.DataStorageSettingsManager.songQualitySettings
import com.rizwansayyed.zene.data.db.datastore.SongsQualityInfo
import com.rizwansayyed.zene.data.onlinesongs.auddrecognition.AuddSongRecognitionService
import com.rizwansayyed.zene.service.youtubedownloader.State
import com.rizwansayyed.zene.service.youtubedownloader.YTExtractor
import com.rizwansayyed.zene.service.youtubedownloader.getAudioOnly
import com.rizwansayyed.zene.service.youtubedownloader.getVideoOnly
import com.rizwansayyed.zene.data.onlinesongs.downloader.SaveFromDownloaderService
import com.rizwansayyed.zene.data.onlinesongs.youtube.implementation.YoutubeAPIImplInterface
import com.rizwansayyed.zene.data.utils.SongDownloader.ytURL
import com.rizwansayyed.zene.data.utils.VideoDownloaderAPI.SAVE_FROM_BASE_URL
import com.rizwansayyed.zene.data.utils.config.RemoteConfigInterface
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
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.asRequestBody
import org.jsoup.Connection
import org.jsoup.Jsoup
import java.io.File
import javax.inject.Inject
import kotlin.coroutines.coroutineContext
import kotlin.time.Duration.Companion.seconds

class AuddSongRecognitionImpl @Inject constructor(
    private val audd: AuddSongRecognitionService,
    private val youtubeAPI: YoutubeAPIImplInterface,
    private val remoteConfig: RemoteConfigInterface
) : AuddSongRecognitionInterface {

    override suspend fun sendSongToRecognition(file: File) = flow {
        val requestFile = file.asRequestBody("application/octet-stream".toMediaTypeOrNull())
        val filePart = MultipartBody.Part.createFormData("file", file.name, requestFile)
        val response = audd.recognition(file = filePart)

        if (response.result == null) {
            emit(null)
            return@flow
        }
        val ip = userIpDetails.first()
        val key = remoteConfig.allApiKeys()?.music ?: ""
        val searchName = "${response.result.title} - ${response.result.artist}"
        emit(youtubeAPI.musicInfoSearch(searchName, ip, key))
    }.flowOn(Dispatchers.IO)
}