package com.rizwansayyed.zene.data.onlinesongs.billboard

import android.util.Log
import com.rizwansayyed.zene.data.onlinesongs.cache.responseCache
import com.rizwansayyed.zene.data.onlinesongs.cache.returnFromCache1Days
import com.rizwansayyed.zene.data.onlinesongs.cache.returnFromCache2Days
import com.rizwansayyed.zene.data.onlinesongs.cache.writeToCacheFile
import com.rizwansayyed.zene.data.onlinesongs.jsoupscrap.jsoupResponseData
import com.rizwansayyed.zene.data.onlinesongs.youtube.implementation.YoutubeAPIImplInterface
import com.rizwansayyed.zene.data.utils.CacheFiles
import com.rizwansayyed.zene.data.utils.FileUploader0x0.FILE_UPLOADER_BASE_URL
import com.rizwansayyed.zene.data.utils.RentAdvisorSubtitles
import com.rizwansayyed.zene.data.utils.ScrapURL
import com.rizwansayyed.zene.domain.MusicData
import com.rizwansayyed.zene.domain.MusicDataCache
import com.rizwansayyed.zene.domain.toTxtCache
import com.rizwansayyed.zene.utils.Utils.daysOldTimestamp
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.withContext
import okhttp3.MediaType
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import org.jsoup.Jsoup
import java.io.File
import javax.inject.Inject


class BillboardImpl @Inject constructor(private val youtubeMusic: YoutubeAPIImplInterface) :
    BillboardImplInterface {

    override suspend fun topSongs() = flow {
        val cache = responseCache(CacheFiles.topGlobalSongs, MusicDataCache::class.java)
        if (cache != null) {
            if (returnFromCache1Days(cache.cacheTime) && cache.list.isNotEmpty()) {
                cache.list.forEach {

                }
                emit(cache.list)
                return@flow
            }
        }

        val lists = ArrayList<String>()
        val songLists = ArrayList<MusicData>()
        val response = jsoupResponseData(ScrapURL.TOP_SONGS)
        val jsoup = Jsoup.parse(response!!)

        jsoup.select(".o-chart-results-list-row-container").forEachIndexed { i, it ->
            if (i > 30) return@forEachIndexed
            val name = it.selectFirst("#title-of-a-story")?.text()?.trim()
            val artists = it.selectFirst("li.lrv-u-width-100p")?.selectFirst("span")?.text()?.trim()
            lists.add("$name - $artists")
        }

        withContext(Dispatchers.IO) {
            lists.map { name ->
                async {
                    val m = youtubeMusic.musicInfoSearch(name).firstOrNull()
                    if (!songLists.any { it.songId == m?.songId }) m?.let { songLists.add(it) }
                }
            }.map {
                it.await()
            }
        }

        songLists.toTxtCache()?.let { writeToCacheFile(CacheFiles.topGlobalSongs, it) }
        emit(songLists)
    }.flowOn(Dispatchers.IO)
}