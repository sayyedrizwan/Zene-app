package com.rizwansayyed.zene.data.onlinesongs.lastfm.implementation

import android.util.Log
import com.rizwansayyed.zene.data.db.datastore.DataStorageManager
import com.rizwansayyed.zene.data.onlinesongs.cache.responseCache
import com.rizwansayyed.zene.data.onlinesongs.cache.returnFromCache2Hours
import com.rizwansayyed.zene.data.onlinesongs.cache.writeToCacheFile
import com.rizwansayyed.zene.data.onlinesongs.jsoupscrap.jsoupResponseData
import com.rizwansayyed.zene.data.onlinesongs.lastfm.LastFMService
import com.rizwansayyed.zene.data.onlinesongs.youtube.implementation.YoutubeAPIImplInterface
import com.rizwansayyed.zene.data.utils.CacheFiles.recentMostPlayedSongs
import com.rizwansayyed.zene.data.utils.LastFM.LAST_FM_BASE_URL
import com.rizwansayyed.zene.data.utils.LastFM.artistsEventInfo
import com.rizwansayyed.zene.data.utils.LastFM.artistsTopSongsInfo
import com.rizwansayyed.zene.data.utils.LastFM.artistsWikiInfo
import com.rizwansayyed.zene.data.utils.LastFM.searchLastFMImageURLPath
import com.rizwansayyed.zene.data.onlinesongs.config.implementation.RemoteConfigInterface
import com.rizwansayyed.zene.domain.ArtistsArtists
import com.rizwansayyed.zene.domain.ArtistsEvents
import com.rizwansayyed.zene.domain.MusicData
import com.rizwansayyed.zene.domain.MusicDataWithArtists
import com.rizwansayyed.zene.domain.MusicDataWithArtistsCache
import com.rizwansayyed.zene.domain.lastfm.LastFMArtist
import com.rizwansayyed.zene.domain.lastfm.toMusicArtists
import com.rizwansayyed.zene.domain.toCache
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.withContext
import org.jsoup.Jsoup
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

        withContext(Dispatchers.IO) {
            lastFMS.topRecentPlayingSongs().results?.artist?.map { s ->
                async {
                    val trackInfo = s.tracks?.firstOrNull() ?: return@async
                    val songName = "${trackInfo.name} - ${trackInfo.artist}"
                    val songs = youtubeMusic.musicInfoSearch(songName, ip, key?.music ?: "")
                    songs?.let { it1 -> list.add(it1.toMusicArtists(s)) }
                }
            }?.map {
                it.await()
            }
        }

        val newList = list.toTypedArray()
            .sortedByDescending { i -> i.listeners?.replace(",", "")?.toIntOrNull() ?: 0 }

        newList.toCache()?.let { writeToCacheFile(recentMostPlayedSongs, it) }

        emit(newList.toMutableList())
    }.flowOn(Dispatchers.IO)


    override suspend fun artistsUsername(name: String) = flow {
        emit(lastFMS.searchArtists(name).results?.artistmatches?.artist?.first())
    }.flowOn(Dispatchers.IO)

    override suspend fun artistsImages(name: LastFMArtist?, limit: Int) = flow {
        val list = mutableListOf<String>()
        var imgId = name?.hdImage()?.substringAfterLast("/")?.substringBeforeLast(".")

        if (limit == 1) {
            name?.hdImage()?.let { list.add(it) }
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
        val list = ArrayList<ArtistsEvents>(15)
        val response = jsoupResponseData(artistsEventInfo(user.url ?: ""))
        val jsoup =
            Jsoup.parse(response!!).select("a.events-list-item-event-name.link-block-target")

        jsoup.forEachIndexed { index, j ->
            if (index > 15) return@forEachIndexed
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

    override suspend fun artistsTopSongs(user: LastFMArtist) = flow {
        val songsLists = ArrayList<MusicData>(11)

        val response = jsoupResponseData(artistsTopSongsInfo(user.url ?: ""))
        val jsoup = Jsoup.parse(response!!)
        val artistsName = jsoup.selectFirst("h1.header-new-title")?.text() ?: user.name ?: ""

        val ip = DataStorageManager.userIpDetails.first()
        val key = remoteConfig.allApiKeys()

        jsoup.select("td.chartlist-name").forEachIndexed { index, element ->
            if (index > 11) return@forEachIndexed
            val songName = "${element.text()} - $artistsName"
            val songs = youtubeMusic.musicInfoSearch(songName, ip, key?.music ?: "")
            songs?.let { songsLists.add(it) }
        }

        emit(songsLists)
    }.flowOn(Dispatchers.IO)
}