package com.rizwansayyed.zene.data.onlinesongs.auddrecognition.implementation

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
import com.rizwansayyed.zene.domain.MusicData
import com.rizwansayyed.zene.domain.auddSongRecognition.AuddSongRecognitionResponse
import com.rizwansayyed.zene.utils.Utils.isConnectedToWifi
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.jsoup.Connection
import org.jsoup.Jsoup
import java.io.File
import javax.inject.Inject
import kotlin.coroutines.coroutineContext
import kotlin.time.Duration.Companion.seconds

interface AuddSongRecognitionInterface {
    suspend fun sendSongToRecognition(file: File): Flow<MusicData?>
}