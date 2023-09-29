package com.rizwansayyed.zene.data.onlinesongs.youtube.implementation


import android.util.Log
import androidx.compose.runtime.mutableStateListOf
import com.rizwansayyed.zene.data.DataResponse
import com.rizwansayyed.zene.data.db.datastore.DataStorageManager.userIpDetails
import com.rizwansayyed.zene.data.onlinesongs.cache.responseCache
import com.rizwansayyed.zene.data.onlinesongs.cache.returnFromCache1Hour
import com.rizwansayyed.zene.data.onlinesongs.cache.returnFromCache2Days
import com.rizwansayyed.zene.data.onlinesongs.cache.writeToCacheFile
import com.rizwansayyed.zene.data.onlinesongs.jsoupscrap.topartistsplaylists.TopArtistsPlaylistsScrapsInterface
import com.rizwansayyed.zene.data.onlinesongs.youtube.YoutubeAPIService
import com.rizwansayyed.zene.data.onlinesongs.youtube.YoutubeMusicAPIService
import com.rizwansayyed.zene.data.utils.CacheFiles.freshAddedSongs
import com.rizwansayyed.zene.data.utils.CacheFiles.songsForYouCache
import com.rizwansayyed.zene.data.utils.CacheFiles.topArtistsCountry
import com.rizwansayyed.zene.data.utils.YoutubeAPI.ytJsonBody
import com.rizwansayyed.zene.data.utils.YoutubeAPI.ytMusicAlbumSearchJsonBody
import com.rizwansayyed.zene.data.utils.YoutubeAPI.ytMusicBrowseSuggestJsonBody
import com.rizwansayyed.zene.data.utils.YoutubeAPI.ytMusicMainSearchJsonBody
import com.rizwansayyed.zene.data.utils.YoutubeAPI.ytMusicNextJsonBody
import com.rizwansayyed.zene.data.utils.YoutubeAPI.ytMusicSearchAllSongsJsonBody
import com.rizwansayyed.zene.data.utils.YoutubeAPI.ytMusicSearchSuggestionJsonBody
import com.rizwansayyed.zene.data.utils.config.RemoteConfigInterface
import com.rizwansayyed.zene.data.utils.sortNameForSearch
import com.rizwansayyed.zene.domain.IpJsonResponse
import com.rizwansayyed.zene.domain.MusicData
import com.rizwansayyed.zene.domain.MusicDataCache
import com.rizwansayyed.zene.domain.TopSuggestMusicData
import com.rizwansayyed.zene.domain.toTxtCache
import com.rizwansayyed.zene.domain.yt.MusicShelfRendererSongs
import com.rizwansayyed.zene.presenter.util.UiUtils.ContentTypes.THE_ARTISTS
import com.rizwansayyed.zene.utils.Utils.artistsListToString
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.withContext
import javax.inject.Inject

class YoutubeAPIImpl @Inject constructor(
    private val youtubeAPI: YoutubeAPIService,
    private val youtubeMusicAPI: YoutubeMusicAPIService,
    private val remoteConfig: RemoteConfigInterface,
    private val jsonScrap: TopArtistsPlaylistsScrapsInterface
) : YoutubeAPIImplInterface {

    override suspend fun newReleaseMusic() = flow {
        val cache = responseCache(freshAddedSongs, MusicDataCache::class.java)
        if (cache != null) {
            if (returnFromCache2Days(cache.cacheTime) && cache.list.isNotEmpty()) {
                emit(cache.list)
                return@flow
            } else
                emit(cache.list)
        }

        val ip = userIpDetails.first()
        val key = remoteConfig.ytApiKeys()

        var channelURL = ""
        val musicChannelId = youtubeAPI.youtubePageResponse(ytJsonBody(ip), key?.yt ?: "")

        musicChannelId.items?.forEach { mainItem ->
            mainItem?.guideSectionRenderer?.items?.forEach { item ->
                if (item?.guideEntryRenderer?.icon?.iconType?.lowercase()?.trim() == "music" &&
                    item.guideEntryRenderer.formattedTitle?.simpleText?.lowercase()
                        ?.trim() == "music" &&
                    item.guideEntryRenderer.accessibility?.accessibilityData?.label?.lowercase()
                        ?.trim() == "music"
                ) channelURL =
                    "https://www.youtube.com/${item.guideEntryRenderer.navigationEndpoint?.commandMetadata?.webCommandMetadata?.url}"
            }
        }
        if (channelURL.isEmpty()) return@flow

        val getReleasePlaylist = jsonScrap.ytChannelJson(channelURL).first()

        var pId = ""
        getReleasePlaylist?.contents?.twoColumnBrowseResultsRenderer?.tabs?.forEach { tab ->
            tab?.tabRenderer?.content?.sectionListRenderer?.contents?.forEach { info ->
                info?.itemSectionRenderer?.contents?.forEach { c ->
                    c?.shelfRenderer?.content?.horizontalListRenderer?.items?.forEach { items ->
                        if (items?.compactStationRenderer?.title?.simpleText?.lowercase() == "released" && pId.isEmpty()) {
                            pId = items.compactStationRenderer.navigationEndpoint?.watchEndpoint
                                ?.playlistId ?: ""
                        }
                    }
                }
            }
        }

        if (pId.isEmpty()) return@flow
        val nameList = mutableStateListOf<String>()

        val songsPlaylist =
            jsonScrap.ytPlaylistItems("https://www.youtube.com/playlist?list=$pId").first()
        songsPlaylist?.contents?.twoColumnBrowseResultsRenderer?.tabs?.forEach { tab ->
            tab?.tabRenderer?.content?.sectionListRenderer?.contents?.forEach { content ->
                content?.itemSectionRenderer?.contents?.forEach { item ->
                    item?.playlistVideoListRenderer?.contents?.forEach { c ->
                        c?.playlistVideoRenderer?.title?.runs?.forEachIndexed { index, txt ->
                            if (index == 0) txt?.text?.let { nameList.add(it) }
                        }
                    }
                }
            }
        }


        val music = mutableListOf<MusicData>()

        suspend fun synList(it: String) {
            var sortName = sortNameForSearch(it)
            if (sortName.trim().isEmpty()) sortName = it

            musicInfoSearch(sortName, ip, key?.music ?: "")?.let { m ->
                if (!music.contains(m)) music.add(m)
            }
        }

        withContext(Dispatchers.IO) {
            val first = async {
                nameList.forEachIndexed { index, s ->
                    if (index <= nameList.size / 2) synList(s)
                }
            }

            val second = async {
                nameList.forEachIndexed { index, s ->
                    if (index >= nameList.size / 2) synList(s)
                }
            }
            first.await()
            second.await()
        }

        val firstHalfSize = music.size / 2
        val topFirst = ArrayList(music.subList(0, firstHalfSize))
        val topSecond = ArrayList(music.subList(firstHalfSize, music.size))
        topFirst.shuffle()
        topFirst.shuffle()
        topSecond.shuffle()

        music.clear()
        music.addAll(topFirst)
        music.addAll(topSecond)
        music.toTxtCache()?.let { writeToCacheFile(freshAddedSongs, it) }
        emit(music)
    }.flowOn(Dispatchers.IO)

    override suspend fun musicInfoSearch(n: String, ip: IpJsonResponse?, key: String): MusicData? {
        if (n.trim().isEmpty()) return null

        var m: MusicData? = null

        try {
            if (n.trim().isEmpty()) return null
            val searchResponse =
                youtubeMusicAPI.youtubeSearchResponse(ytMusicMainSearchJsonBody(ip, n), key)

            searchResponse.contents?.tabbedSearchResultsRenderer?.tabs?.first()?.tabRenderer?.content?.sectionListRenderer?.contents?.forEach { s ->
                if (s?.musicShelfRenderer?.title?.runs?.first()?.text?.lowercase() == "songs") {
                    val thumbnail =
                        s.musicShelfRenderer.contents?.first()?.musicResponsiveListItemRenderer?.thumbnail
                            ?.musicThumbnailRenderer?.thumbnail?.thumbnailURL()

                    val name =
                        s.musicShelfRenderer.contents?.first()?.musicResponsiveListItemRenderer?.names()

                    val artists = s.musicShelfRenderer.getArtists()

                    m = MusicData(thumbnail ?: "", name?.first, artists, name?.second)
                }
            }
        } catch (e: Exception) {
            m = null
        }

        return m
    }

    override suspend fun artistsInfo(artists: List<MusicData>) = flow {
        val cache = responseCache(topArtistsCountry, MusicDataCache::class.java)
        if (cache != null) {
            if (returnFromCache2Days(cache.cacheTime) && cache.list.isNotEmpty()) {
                emit(cache.list)
                return@flow
            }
        }

        val ip = userIpDetails.first()
        val key = remoteConfig.ytApiKeys()?.music ?: ""

        val lists = mutableListOf<String>()
        val artistsLists = mutableListOf<MusicData>()

        artists.forEach { a ->
            for (artist in a.artists?.split(",", "&")!!) {
                if (!lists.contains(artist.lowercase().trim())) lists.add(artist.lowercase().trim())
            }
        }

        if (lists.size > 54) {
            val subList = ArrayList(lists.subList(0, 44))
            lists.clear()
            lists.addAll(subList)
        }

        lists.forEach { n ->
            if (n.trim().isEmpty()) return@forEach
            val searchResponse =
                youtubeMusicAPI.youtubeSearchResponse(ytMusicAlbumSearchJsonBody(ip, n), key)

            searchResponse.contents?.tabbedSearchResultsRenderer?.tabs?.first()?.tabRenderer?.content?.sectionListRenderer?.contents?.forEach { s ->
                if (s?.musicShelfRenderer?.title?.runs?.first()?.text?.lowercase() == "artists") {
                    val thumbnail =
                        s.musicShelfRenderer.contents?.first()?.musicResponsiveListItemRenderer?.thumbnail
                            ?.musicThumbnailRenderer?.thumbnail?.thumbnailURL()

                    val a = s.musicShelfRenderer.getArtistsNoCheck()
                    a.let {
                        if (!artistsLists.any { a ->
                                a.artists?.trim()?.lowercase() == it?.lowercase()?.trim()
                            }) artistsLists.add(MusicData(thumbnail ?: "", it, it, THE_ARTISTS))
                    }
                }
            }
        }
        artistsLists.distinct()
        artistsLists.toTxtCache()?.let { writeToCacheFile(topArtistsCountry, it) }

        emit(artistsLists.toList())
    }.flowOn(Dispatchers.IO)

    suspend fun topTwoSongsSuggestionOnHistory(pIds: List<String>) = flow {
        val cache = responseCache(songsForYouCache, TopSuggestMusicData::class.java)
        if (cache != null) if (cache.pList == pIds) {
            emit(cache.list)
            return@flow
        }

        val list = mutableListOf<MusicData>()

        val ip = userIpDetails.first()
        val key = remoteConfig.ytApiKeys()?.music ?: ""

        pIds.forEach { id ->
            val r =
                youtubeMusicAPI.youtubeNextSearchResponse(ytMusicNextJsonBody(ip, id), key)
            val browserId = r.browseID() ?: return@forEach

            val songsList = youtubeMusicAPI
                .youtubeBrowseResponse(ytMusicBrowseSuggestJsonBody(ip, browserId), key)

            songsList.contents?.sectionListRenderer?.contents?.forEach { c ->
                if (c?.musicCarouselShelfRenderer?.header?.musicCarouselShelfBasicHeaderRenderer?.accessibilityData?.accessibilityData?.label?.lowercase() == "you might also like") {
                    c.musicCarouselShelfRenderer.contents?.forEachIndexed { index, content ->
                        if (index > 2) return@forEachIndexed

                        val thumbnail =
                            content?.musicResponsiveListItemRenderer?.thumbnail?.musicThumbnailRenderer?.thumbnail?.thumbnailURL()
                        val musicData = MusicData(thumbnail, "", "", null)
                        val artists = mutableListOf<String>()

                        content?.musicResponsiveListItemRenderer?.flexColumns?.forEach { n ->
                            n?.musicResponsiveListItemFlexColumnRenderer?.text?.runs?.forEach { na ->
                                if (na?.navigationEndpoint?.watchEndpoint?.watchEndpointMusicSupportedConfigs?.watchEndpointMusicConfig?.musicVideoType == "MUSIC_VIDEO_TYPE_ATV") {
                                    musicData.name = na.text
                                    na.navigationEndpoint.watchEndpoint.videoId.also {
                                        musicData.pId = it
                                    }
                                }

                                if (na?.navigationEndpoint?.browseEndpoint?.browseEndpointContextSupportedConfigs?.browseEndpointContextMusicConfig?.pageType == "MUSIC_PAGE_TYPE_ARTIST") {
                                    na.text?.let { artists.add(it) }
                                }
                            }
                        }

                        musicData.artists = artistsListToString(artists)

                        if (musicData.pId != null)
                            if (!list.any { it.pId == musicData.pId }) list.add(musicData)
                    }
                }
            }
        }

        list.shuffle()

        TopSuggestMusicData(System.currentTimeMillis(), pIds, list).toTxtCache()
            ?.let { writeToCacheFile(songsForYouCache, it) }

        emit(list)
    }


    override suspend fun searchSuggestions(s: String) = flow {
        val list = mutableListOf<String>()
        val ip = userIpDetails.first()
        val key = remoteConfig.ytApiKeys()?.music ?: ""

        val r =
            youtubeMusicAPI.youtubeSearchSuggestion(ytMusicSearchSuggestionJsonBody(ip, s), key)

        r.contents?.forEach { c ->
            c?.searchSuggestionsSectionRenderer?.contents?.forEach { s ->
                if (s?.searchSuggestionRenderer?.icon?.iconType == "SEARCH") {
                    s.searchSuggestionRenderer.navigationEndpoint?.searchEndpoint?.query?.let {
                        list.add(it)
                    }
                }
            }
        }

        emit(list)
    }

    override suspend fun allSongsSearch(q: String) = flow {
        val list = mutableListOf<MusicData>()

        val ip = userIpDetails.first()
        val key = remoteConfig.ytApiKeys()?.music ?: ""

        var token = ""
        var clickParams = ""

        fun addItems(shelf: MusicShelfRendererSongs.Content?) {
            val thumbnail = shelf?.musicResponsiveListItemRenderer?.thumbnail
                ?.musicThumbnailRenderer?.thumbnail?.thumbnailURL()
            val name = shelf?.musicResponsiveListItemRenderer?.names()
            val artists = shelf?.getArtists()

            list.add(MusicData(thumbnail ?: "", name?.first, artists, name?.second))
        }

        val r =
            youtubeMusicAPI.youtubeSearchAllSongsResponse(ytMusicSearchAllSongsJsonBody(ip, q), key)

        r.contents?.tabbedSearchResultsRenderer?.tabs?.first()?.tabRenderer?.content?.sectionListRenderer?.contents?.forEach { c ->
            c?.musicShelfRenderer?.contents?.forEach { shelf ->
                addItems(shelf)
            }

            c?.musicShelfRenderer?.continuations?.forEach { i ->
                token = i?.nextContinuationData?.continuation ?: ""
                clickParams = i?.nextContinuationData?.clickTrackingParams ?: ""
            }
        }

        if (token.isNotEmpty() && clickParams.isNotEmpty()) {
            val res =
                youtubeMusicAPI.youtubeMoreSearchAllSongsResponse(
                    ytMusicSearchAllSongsJsonBody(ip, q), token, token, clickParams, key
                )

            res.contents?.tabbedSearchResultsRenderer?.tabs?.first()?.tabRenderer?.content?.sectionListRenderer?.contents?.forEach { c ->
                c?.musicShelfRenderer?.contents?.forEach { shelf ->
                    addItems(shelf)
                }
            }
        }

        emit(list)
    }

    override suspend fun songFromArtistsTopFive(artists: List<String>) = flow {
        val cache = responseCache(songsForYouCache, TopSuggestMusicData::class.java)
        if (cache != null) if (cache.list.isNotEmpty()) emit(cache.list)

        val list = mutableListOf<MusicData>()

        artists.forEach {
            try {
                val l = allSongsSearch(it).first()
                if (l.size > 5)
                    l.addAll(list.subList(0, 5))
                else
                    l.addAll(list)
            } catch (e: Exception) {
                e.message
            }
        }
        if (list.isNotEmpty()) {
            val tempList = list.shuffled()
            TopSuggestMusicData(System.currentTimeMillis(), artists, tempList)
                .toTxtCache()?.let { writeToCacheFile(songsForYouCache, it) }

            emit(list)
        }

    }

}