package com.rizwansayyed.zene.data.onlinesongs.lastfm.implementation

import android.util.Log
import com.rizwansayyed.zene.data.db.datastore.DataStorageManager
import com.rizwansayyed.zene.data.onlinesongs.cache.responseCache
import com.rizwansayyed.zene.data.onlinesongs.cache.returnFromCache2Hours
import com.rizwansayyed.zene.data.onlinesongs.cache.writeToCacheFile
import com.rizwansayyed.zene.data.onlinesongs.jsoupscrap.jsoupResponseData
import com.rizwansayyed.zene.data.onlinesongs.lastfm.LastFMService
import com.rizwansayyed.zene.data.onlinesongs.youtube.implementation.YoutubeAPIImplInterface
import com.rizwansayyed.zene.data.utils.CacheFiles
import com.rizwansayyed.zene.data.utils.CacheFiles.recentMostPlayedSongs
import com.rizwansayyed.zene.data.utils.LastFM.LAST_FM_BASE_URL
import com.rizwansayyed.zene.data.utils.LastFM.artistsEventInfo
import com.rizwansayyed.zene.data.utils.LastFM.artistsWikiInfo
import com.rizwansayyed.zene.data.utils.LastFM.searchLastFMImageURLPath
import com.rizwansayyed.zene.data.utils.YoutubeAPI
import com.rizwansayyed.zene.data.utils.config.RemoteConfigInterface
import com.rizwansayyed.zene.data.utils.moshi
import com.rizwansayyed.zene.di.ApplicationModule.Companion.context
import com.rizwansayyed.zene.domain.ArtistsArtists
import com.rizwansayyed.zene.domain.ArtistsEvents
import com.rizwansayyed.zene.domain.MusicDataWithArtists
import com.rizwansayyed.zene.domain.MusicDataWithArtistsCache
import com.rizwansayyed.zene.domain.MusicType
import com.rizwansayyed.zene.domain.lastfm.ArtistsSearchResponse
import com.rizwansayyed.zene.domain.lastfm.LastFMArtist
import com.rizwansayyed.zene.domain.toCache
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import org.jsoup.Jsoup
import java.io.File
import javax.inject.Inject

class LastFMImpl @Inject constructor(
    private val lastFMS: LastFMService,
    private val youtubeMusic: YoutubeAPIImplInterface,
    private val remoteConfig: RemoteConfigInterface,
) : LastFMImplInterface {

    override suspend fun topRecentPlayingSongs() = flow {
        val cache = responseCache(recentMostPlayedSongs, MusicDataWithArtistsCache::class.java)

        if (cache != null) {
            if (returnFromCache2Hours(cache.cacheTime) && cache.list.isNotEmpty()) {
                emit(cache.list.toMutableList())
                return@flow
            }
        }
        val list = mutableListOf<MusicDataWithArtists>()

        val ip = DataStorageManager.userIpDetails.first()
        val key = remoteConfig.allApiKeys()

        val res = lastFMS.topRecentPlayingSongs()
        res.results?.artist?.forEach { s ->
            val songName = "${s?.tracks?.first()?.name} - ${s?.name}"
            val songs = youtubeMusic.musicInfoSearch(songName, ip, key?.music ?: "")
            songs?.let { it1 ->
                list.add(
                    MusicDataWithArtists(
                        it1.thumbnail ?: "",
                        it1.name ?: "",
                        it1.artists ?: "",
                        s?.listeners ?: "",
                        s?.image?.replace("174s/", "770x0/")?.replace(".png", ".jpg"),
                        s?.name ?: "",
                        it1.pId ?: "",
                        it1.type ?: MusicType.MUSIC
                    )
                )
            }
        }
        list.toCache()?.let { writeToCacheFile(recentMostPlayedSongs, it) }

        emit(list)
    }.flowOn(Dispatchers.IO)


    override suspend fun artistsUsername(name: String) = flow {
        emit(lastFMS.searchArtists(name).results?.artistmatches?.artist?.first())
    }.flowOn(Dispatchers.IO)

    override suspend fun artistsImages(name: LastFMArtist?, limit: Int) = flow {
        val list = mutableListOf<String>()
        var imgId = name?.image?.substringAfterLast("/")?.substringBeforeLast(".")

        if (limit == 1) {
            name?.image?.let { list.add(it) }
            emit(list)
            return@flow
        }

        while (list.size <= limit) {
            val img = lastFMS.artistsImages(searchLastFMImageURLPath(name?.url ?: "", imgId ?: ""))
            img.values.forEach {
                if (!list.any { l -> l == it.src }) it.src?.let { img -> list.add(img) }
                imgId = it.next?.substringAfterLast("/")
            }
        }
        emit(list)
    }.flowOn(Dispatchers.IO)


    override suspend fun artistsDescription(user: LastFMArtist) = flow {
        val response = jsoupResponseData(artistsWikiInfo(user.url ?: ""))
        val jsoup = Jsoup.parse(response!!)

        var artistsInfo = ""

        jsoup.selectFirst(".wiki-content")?.select("p")?.forEachIndexed { index, element ->
            if (index > 2) return@forEachIndexed
            artistsInfo += " "
            artistsInfo += element.text()
        }
        emit(artistsInfo)
    }.flowOn(Dispatchers.IO)


    override suspend fun artistsEvent(user: LastFMArtist) = flow {
        val list = ArrayList<ArtistsEvents>(30)
        val response = jsoupResponseData(artistsEventInfo(user.url ?: ""))
        val jsoup =
            Jsoup.parse(response!!).select("a.events-list-item-event-name.link-block-target")

        jsoup.forEach { j ->
            val eventInfoURL = "$LAST_FM_BASE_URL${j.attr("href").substringAfter("/")}"
            val events = Jsoup.parse(
                jsoupResponseData(eventInfoURL)?.replace("   ", "")?.replace("\n", "")!!
            )

            val name = events.selectFirst("h1.header-title")?.text()
            val time = events.selectFirst("p.qa-event-date")?.selectFirst("strong")?.text()
            val address = events.selectFirst("p.event-detail-address")?.text()
            val bookingLink =
                events.selectFirst("a.event-detail-long-link.external-link")?.attr("href")

            val artist = ArrayList<ArtistsArtists>()

            events.selectFirst("ol.grid-items")
                ?.select("div.grid-items-cover-image.js-link-block.link-block")?.forEach { list ->
                    val artistsImg =
                        list.selectFirst("div.grid-items-cover-image-image")?.select("img")
                            ?.attr("src")
                    val artistsN =
                        list.selectFirst("div.grid-items-cover-image-image")?.select("img")
                            ?.attr("alt")?.replace("Image for", "")?.replace("'", "")
                    artistsN?.let { artist.add(ArtistsArtists(it, artistsImg ?: "")) }
                }

            list.add(ArtistsEvents(name, time, address, bookingLink, artist))
        }
        emit(list)
    }.flowOn(Dispatchers.IO)
}