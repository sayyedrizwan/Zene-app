package com.rizwansayyed.zene.data.onlinesongs.youtube.implementation


import android.util.Log
import com.rizwansayyed.zene.data.db.datastore.DataStorageManager.userIpDetails
import com.rizwansayyed.zene.data.onlinesongs.cache.responseCache
import com.rizwansayyed.zene.data.onlinesongs.cache.returnFromCache1Days
import com.rizwansayyed.zene.data.onlinesongs.cache.returnFromCache2Days
import com.rizwansayyed.zene.data.onlinesongs.cache.writeToCacheFile
import com.rizwansayyed.zene.data.onlinesongs.jsoupscrap.youtubescrap.YoutubeScrapInterface
import com.rizwansayyed.zene.data.onlinesongs.youtube.YoutubeAPIService
import com.rizwansayyed.zene.data.onlinesongs.youtube.YoutubeMusicAPIService
import com.rizwansayyed.zene.data.utils.CacheFiles.albumsForYouCache
import com.rizwansayyed.zene.data.utils.CacheFiles.artistsFanWithSongsCache
import com.rizwansayyed.zene.data.utils.CacheFiles.freshAddedSongs
import com.rizwansayyed.zene.data.utils.CacheFiles.songsForYouCache
import com.rizwansayyed.zene.data.utils.CacheFiles.suggestionYouMayLikeCache
import com.rizwansayyed.zene.data.utils.CacheFiles.topArtistsCountry
import com.rizwansayyed.zene.data.utils.YoutubeAPI.ytArtistsPlaylistJsonBody
import com.rizwansayyed.zene.data.utils.YoutubeAPI.ytLatestMusicSearch
import com.rizwansayyed.zene.data.utils.YoutubeAPI.ytMerchandiseInfoJsonBody
import com.rizwansayyed.zene.data.utils.YoutubeAPI.ytMusicAlbumsDetailsJsonBody
import com.rizwansayyed.zene.data.utils.YoutubeAPI.ytMusicArtistsAlbumsJsonBody
import com.rizwansayyed.zene.data.utils.YoutubeAPI.ytMusicArtistsSearchJsonBody
import com.rizwansayyed.zene.data.utils.YoutubeAPI.ytMusicBrowseSuggestJsonBody
import com.rizwansayyed.zene.data.utils.YoutubeAPI.ytMusicMainSearchJsonBody
import com.rizwansayyed.zene.data.utils.YoutubeAPI.ytMusicMusicDetails
import com.rizwansayyed.zene.data.utils.YoutubeAPI.ytMusicNewReleaseJsonBody
import com.rizwansayyed.zene.data.utils.YoutubeAPI.ytMusicNextJsonBody
import com.rizwansayyed.zene.data.utils.YoutubeAPI.ytMusicSearchAllSongsJsonBody
import com.rizwansayyed.zene.data.utils.YoutubeAPI.ytMusicSearchSuggestionJsonBody
import com.rizwansayyed.zene.data.utils.YoutubeAPI.ytMusicUpNextDetails
import com.rizwansayyed.zene.data.utils.YoutubeAPI.ytMusicUpNextJsonBody
import com.rizwansayyed.zene.data.utils.config.RemoteConfigInterface
import com.rizwansayyed.zene.domain.ArtistsFanData
import com.rizwansayyed.zene.domain.ArtistsFanDataCache
import com.rizwansayyed.zene.domain.IpJsonResponse
import com.rizwansayyed.zene.domain.MusicData
import com.rizwansayyed.zene.domain.MusicDataCache
import com.rizwansayyed.zene.domain.MusicType
import com.rizwansayyed.zene.domain.PlaylistItemsData
import com.rizwansayyed.zene.domain.SearchData
import com.rizwansayyed.zene.domain.SongsSuggestionsData
import com.rizwansayyed.zene.domain.TopSuggestMusicData
import com.rizwansayyed.zene.domain.toTxtCache
import com.rizwansayyed.zene.domain.yt.MerchandiseItems
import com.rizwansayyed.zene.domain.yt.MusicShelfRendererSongs
import com.rizwansayyed.zene.domain.yt.YoutubeLatestYearResponse
import com.rizwansayyed.zene.domain.yt.YoutubeMusicAllSongsResponse
import com.rizwansayyed.zene.domain.yt.YoutubePlaylistItemsResponse
import com.rizwansayyed.zene.presenter.util.UiUtils.toCapitalFirst
import com.rizwansayyed.zene.utils.Utils.artistsListToString
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.withContext
import java.io.File
import javax.inject.Inject

class YoutubeAPIImpl @Inject constructor(
    private val youtubeMusicAPI: YoutubeMusicAPIService,
    private val youtubeAPI: YoutubeAPIService,
    private val remoteConfig: RemoteConfigInterface,
    private val ytJsonScrap: YoutubeScrapInterface
) : YoutubeAPIImplInterface {
    override suspend fun newReleaseMusic() = flow {
        val cache = responseCache(freshAddedSongs, MusicDataCache::class.java)
        if (cache != null) {
            if (returnFromCache1Days(cache.cacheTime) && cache.list.isNotEmpty()) {
                emit(cache.list)
                return@flow
            } else
                emit(cache.list)
        }

        val ip = userIpDetails.first()
        val key = remoteConfig.allApiKeys()

        val channels =
            youtubeMusicAPI.youtubeReleaseResponse(ytMusicNewReleaseJsonBody(ip), key?.music ?: "")

        val pList = mutableListOf<String>()
        channels.contents?.singleColumnBrowseResultsRenderer?.tabs?.first()?.tabRenderer?.content?.sectionListRenderer?.contents?.forEach { content ->
            content?.gridRenderer?.items?.forEach { items ->
                items?.musicTwoRowItemRenderer?.thumbnailOverlay?.musicItemThumbnailOverlayRenderer?.content?.musicPlayButtonRenderer?.playNavigationEndpoint?.watchPlaylistEndpoint?.playlistId?.let { pId ->
                    pList.add(pId)
                }
            }
        }
        val music = mutableListOf<MusicData>()

        pList.forEach {
            ytJsonScrap.ytReleaseItems(it).first().forEach { name ->
                val response = musicInfoSearch(name, ip, key?.music ?: "")
                response?.let { m -> music.add(m) }
            }
        }

        music.toTxtCache()?.let { writeToCacheFile(freshAddedSongs, it) }

        emit(music)
    }.flowOn(Dispatchers.IO)

    override suspend fun musicInfoSearch(q: String, ip: IpJsonResponse?, key: String): MusicData? {
        return withContext(Dispatchers.IO) {
            val n = q.trim().lowercase().replace("teaser", "")

            if (n.isEmpty()) return@withContext null

            var m: MusicData? = null

            try {
                if (n.isEmpty()) return@withContext null
                val searchResponse =
                    youtubeMusicAPI.youtubeSearchResponse(ytMusicMainSearchJsonBody(ip, n), key)

                searchResponse.contents?.tabbedSearchResultsRenderer?.tabs?.first()?.tabRenderer?.content?.sectionListRenderer?.contents?.forEach { s ->
                    if (s?.musicShelfRenderer?.title?.runs?.first()?.text?.lowercase() == "songs") {
                        val thumbnail =
                            s.musicShelfRenderer.contents?.first()?.musicResponsiveListItemRenderer?.thumbnail
                                ?.musicThumbnailRenderer?.thumbnail?.thumbnailURL()

                        val name =
                            s.musicShelfRenderer.contents?.first()?.musicResponsiveListItemRenderer?.names()?.first

                        val songsId =
                            s.musicShelfRenderer.contents?.first()?.musicResponsiveListItemRenderer?.names()?.second

                        val artists = s.musicShelfRenderer.getArtists()

                        m = MusicData(thumbnail ?: "", name, artists, songsId, MusicType.MUSIC)
                    }
                }
            } catch (e: Exception) {
                m = null
            }

            m
        }
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
        val key = remoteConfig.allApiKeys()?.music ?: ""

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
                youtubeMusicAPI.youtubeSearchResponse(ytMusicArtistsSearchJsonBody(ip, n), key)

            searchResponse.contents?.tabbedSearchResultsRenderer?.tabs?.first()?.tabRenderer?.content?.sectionListRenderer?.contents?.forEach { s ->
                if (s?.musicShelfRenderer?.title?.runs?.first()?.text?.lowercase() == "artists") {
                    val thumbnail =
                        s.musicShelfRenderer.contents?.first()?.musicResponsiveListItemRenderer?.thumbnail
                            ?.musicThumbnailRenderer?.thumbnail?.thumbnailURL()

                    val a = s.musicShelfRenderer.getArtistsNoCheck()
                    val id = s.musicShelfRenderer.getArtistsId()

                    a?.let {
                        if (!artistsLists.any { a ->
                                a.artists?.trim()?.lowercase() == it.lowercase().trim()
                            }) artistsLists.add(
                            MusicData(thumbnail ?: "", it, it, id, MusicType.ARTISTS)
                        )
                    }
                }
            }
        }
        artistsLists.distinct()
        artistsLists.toTxtCache()?.let { writeToCacheFile(topArtistsCountry, it) }

        emit(artistsLists.toList())
    }.flowOn(Dispatchers.IO)


    override suspend fun searchArtistsInfo(s: String) = flow {
        val ip = userIpDetails.first()
        val key = remoteConfig.allApiKeys()?.music ?: ""

        val artistsLists = mutableListOf<MusicData>()

        val searchResponse =
            youtubeMusicAPI.youtubeSearchResponse(ytMusicArtistsSearchJsonBody(ip, s), key)

        searchResponse.contents?.tabbedSearchResultsRenderer?.tabs?.first()?.tabRenderer?.content?.sectionListRenderer?.contents?.forEach { s ->
            if (s?.musicShelfRenderer?.title?.runs?.first()?.text?.lowercase() == "artists") {
                s.musicShelfRenderer.contents?.forEach { content ->
                    val thumbnail =
                        content?.musicResponsiveListItemRenderer?.thumbnail
                            ?.musicThumbnailRenderer?.thumbnail?.thumbnailURL()
                    val id = s.musicShelfRenderer.getArtistsId()

                    content?.musicResponsiveListItemRenderer?.flexColumns?.forEach { flex ->
                        if (flex?.musicResponsiveListItemFlexColumnRenderer?.displayPriority?.trim() == "MUSIC_RESPONSIVE_LIST_ITEM_COLUMN_DISPLAY_PRIORITY_HIGH")

                            flex.musicResponsiveListItemFlexColumnRenderer.text?.runs?.forEachIndexed { index, content ->
                                if (index == 0) content?.text?.let {
                                    if (it.lowercase() == "artist") return@let

                                    if (!artistsLists.any { a ->
                                            a.artists?.trim()?.lowercase() == it.lowercase().trim()
                                        }) artistsLists.add(
                                        MusicData(
                                            thumbnail ?: "", it, it, id, MusicType.ARTISTS
                                        )
                                    )
                                }
                            }
                    }
                }
            }
        }

        artistsLists.distinct()
        emit(artistsLists.toList())
    }.flowOn(Dispatchers.IO)


    override suspend fun topFourSongsSuggestionOnHistory(pIds: List<String>) = flow {
        val cache = responseCache(songsForYouCache, TopSuggestMusicData::class.java)
        if (cache != null) if (cache.pList == pIds) {
            emit(cache.list)
            return@flow
        }

        val list = mutableListOf<MusicData>()

        val ip = userIpDetails.first()
        val key = remoteConfig.allApiKeys()?.music ?: ""

        pIds.forEach { id ->
            if (id.trim().isEmpty()) return@forEach

            val r =
                youtubeMusicAPI.youtubeNextSearchResponse(ytMusicNextJsonBody(ip, id), key)
            val browserId = r.browseID() ?: return@forEach

            val songsList = youtubeMusicAPI
                .youtubeBrowseResponse(ytMusicBrowseSuggestJsonBody(ip, browserId), key)

            songsList.contents?.sectionListRenderer?.contents?.forEach { c ->
                if (c?.musicCarouselShelfRenderer?.header?.musicCarouselShelfBasicHeaderRenderer?.accessibilityData?.accessibilityData?.label?.lowercase() == "you might also like") {
                    c.musicCarouselShelfRenderer.contents?.forEachIndexed { index, content ->
                        if (index > 4) return@forEachIndexed

                        val thumbnail =
                            content?.musicResponsiveListItemRenderer?.thumbnail?.musicThumbnailRenderer?.thumbnail?.thumbnailURL()
                        val musicData = MusicData(thumbnail, "", "", null, MusicType.MUSIC)
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
    }.flowOn(Dispatchers.IO)


    override suspend fun searchSuggestions(s: String) = flow {
        val list = mutableListOf<String>()

        if (s.trim().isEmpty()) {
            emit(list)
            return@flow
        }
        val ip = userIpDetails.first()
        val key = remoteConfig.allApiKeys()?.music ?: ""

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
    }.flowOn(Dispatchers.IO)

    override suspend fun allSongsSearch(q: String) = flow {
        val list = mutableListOf<MusicData>()

        val ip = userIpDetails.first()
        val key = remoteConfig.allApiKeys()?.music ?: ""

        var token = ""
        var clickParams = ""

        fun addItems(shelf: MusicShelfRendererSongs.Content?) {
            val thumbnail = shelf?.musicResponsiveListItemRenderer?.thumbnail
                ?.musicThumbnailRenderer?.thumbnail?.thumbnailURL()

            val name = shelf?.musicResponsiveListItemRenderer?.names()?.first
            val songId = shelf?.musicResponsiveListItemRenderer?.names()?.second
            val artists = shelf?.getArtists()

            val m = MusicData(
                thumbnail ?: "", name, artists, songId, MusicType.MUSIC
            )
            list.add(m)
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
//            val res = youtubeMusicAPI.youtubeMoreSearchAllSongsResponse(
//                ytMusicNextSearchJsonBody(ip), token, token, clickParams, key
//            )
//
//            res.contents?.tabbedSearchResultsRenderer?.tabs?.first()?.tabRenderer?.content?.sectionListRenderer?.contents?.forEach { c ->
//                c?.musicShelfRenderer?.contents?.forEach { shelf ->
//                    addItems(shelf)
//                }
//            }
        }

        emit(list)
    }.flowOn(Dispatchers.IO)


    override suspend fun albumsSearch(id: String) = flow {
        val list = mutableListOf<MusicData>()

        val ip = userIpDetails.first()
        val key = remoteConfig.allApiKeys()?.music ?: ""

        val r = youtubeMusicAPI.youtubeBrowsePlaylist(ytMusicAlbumsDetailsJsonBody(ip, id), key)

        val albumName = r.header?.musicDetailHeaderRenderer?.title?.runs?.first()?.text

        r.contents?.singleColumnBrowseResultsRenderer?.tabs?.first()?.tabRenderer?.content?.sectionListRenderer?.contents?.first()?.musicShelfRenderer?.contents?.forEach {
            val sName =
                it?.musicResponsiveListItemRenderer?.flexColumns?.first()?.musicResponsiveListItemFlexColumnRenderer?.text?.runs?.first()?.text

            val pId =
                it?.musicResponsiveListItemRenderer?.overlay?.musicItemThumbnailOverlayRenderer?.content?.musicPlayButtonRenderer?.playNavigationEndpoint?.watchEndpoint?.videoId

            list.add(MusicData("", sName, albumName, pId, MusicType.MUSIC))
        }


        var artist: String? = ""
        r.header?.musicDetailHeaderRenderer?.subtitle?.runs?.forEach {
            if (it?.navigationEndpoint?.browseEndpoint?.browseEndpointContextSupportedConfigs?.browseEndpointContextMusicConfig?.pageType == "MUSIC_PAGE_TYPE_ARTIST")
                artist = it.text
        }

        val thumbnail =
            r.header?.musicDetailHeaderRenderer?.thumbnail?.croppedSquareThumbnailRenderer?.thumbnail?.thumbnailURL()
        val desc = r.header?.musicDetailHeaderRenderer?.description?.runs?.first()?.text
        var timeLength = ""
        r.header?.musicDetailHeaderRenderer?.secondSubtitle?.runs?.forEach {
            if (it?.text?.contains("minutes") == true)
                timeLength = it.text
        }
        emit(PlaylistItemsData(thumbnail, albumName, artist, timeLength, desc, list))
    }.flowOn(Dispatchers.IO)

    override suspend fun youtubeVideoThisYearSearch(q: String) = flow {
        val list = mutableListOf<MusicData>()

        val ip = userIpDetails.first()
        val key = remoteConfig.allApiKeys()?.yt ?: ""

        val r = youtubeAPI.youtubeSearchResponse(ytLatestMusicSearch(ip, q, true), key)

        r.contents?.twoColumnSearchResultsRenderer?.primaryContents?.sectionListRenderer?.contents?.first()?.itemSectionRenderer?.contents?.forEach { c ->
            val vId = c?.videoRenderer?.videoId
            val thumbnail = c?.videoRenderer?.thumbnailURL()
            val name = c?.videoRenderer?.title?.runs?.first()?.text

            val m = MusicData(
                thumbnail ?: "", name, "", vId, MusicType.VIDEO
            )
            list.add(m)
        }

        emit(list)
    }.flowOn(Dispatchers.IO)

    override suspend fun youtubeVideoSearch(q: String) = flow {
        val list = mutableListOf<MusicData>()

        val ip = userIpDetails.first()
        val key = remoteConfig.allApiKeys()?.yt ?: ""

        val r = youtubeAPI.youtubeSearchResponse(ytLatestMusicSearch(ip, q, false), key)

        r.contents?.twoColumnSearchResultsRenderer?.primaryContents?.sectionListRenderer?.contents?.first()?.itemSectionRenderer?.contents?.forEach { c ->
            val vId = c?.videoRenderer?.videoId
            val thumbnail = c?.videoRenderer?.thumbnailURL()
            val name = c?.videoRenderer?.title?.runs?.first()?.text

            val m = MusicData(
                thumbnail ?: "", name, "", vId, MusicType.VIDEO
            )
            list.add(m)
        }

        emit(list)
    }.flowOn(Dispatchers.IO)


    override suspend fun youtubeShortsThisYearSearch(q: String) = flow {
        val list = mutableListOf<MusicData>()

        val ip = userIpDetails.first()
        val key = remoteConfig.allApiKeys()?.yt ?: ""

        val r = youtubeAPI
            .youtubeSearchResponse(ytLatestMusicSearch(ip, "${q.lowercase()} shorts", false), key)

        r.contents?.twoColumnSearchResultsRenderer?.primaryContents?.sectionListRenderer?.contents?.first()?.itemSectionRenderer?.contents?.forEach { c ->
            if (c?.reelShelfRenderer?.title?.simpleText?.lowercase() != "shorts") return@forEach

            c.reelShelfRenderer.items?.forEach { shorts ->
                val vId = shorts?.reelItemRenderer?.videoId
                val thumbnail = shorts?.reelItemRenderer?.thumbnailURL()
                val name = shorts?.reelItemRenderer?.headline?.simpleText
                val viewsInArtists = shorts?.reelItemRenderer?.viewCountText?.simpleText

                val m = MusicData(
                    thumbnail ?: "", name, viewsInArtists, vId, MusicType.VIDEO
                )
                list.add(m)
            }
        }

        emit(list)
    }.flowOn(Dispatchers.IO)


    override suspend fun searchArtistsPlaylistsForRadio(q: String) = flow {
        val list = mutableListOf<MusicData>()

        val ip = userIpDetails.first()
        val key = remoteConfig.allApiKeys()?.yt ?: ""

        val r = youtubeAPI.youtubeSearchResponse(ytArtistsPlaylistJsonBody(ip, q), key)

        r.contents?.twoColumnSearchResultsRenderer?.primaryContents?.sectionListRenderer?.contents?.first()?.itemSectionRenderer?.contents?.forEach { c ->
            val vId = c?.videoRenderer?.videoId ?: return@forEach

            val thumbnail = c.videoRenderer.thumbnailURL()

            val m = MusicData(
                thumbnail ?: "", "${q.toCapitalFirst()} Radio",
                q.lowercase(), vId, MusicType.MUSIC
            )
            list.add(m)
        }
        list.shuffle()

        emit(list.random())
    }.flowOn(Dispatchers.IO)

    override suspend fun songFromArtistsTopFive(artists: List<String>) = flow {
        val cache = responseCache(songsForYouCache, TopSuggestMusicData::class.java)
        if (cache != null) if (cache.list.isNotEmpty()) emit(cache.list)

        val list = mutableListOf<MusicData>()

        artists.forEach {
            try {
                val l = allSongsSearch(it).first()
                list.addAll(l)
            } catch (e: Exception) {
                e.message
            }
        }
        if (list.isNotEmpty()) {
            val tempList = list.shuffled()
            TopSuggestMusicData(System.currentTimeMillis(), artists, tempList)
                .toTxtCache()?.let { writeToCacheFile(songsForYouCache, it) }
        }
        emit(list.shuffled().distinct())
    }.flowOn(Dispatchers.IO)


    override suspend fun artistsAlbumsTopFive(names: List<String>) = flow {
        val cache = responseCache(albumsForYouCache, TopSuggestMusicData::class.java)
        if (cache != null) if (cache.pList == names || names.isEmpty()) {
            emit(cache.list)
            return@flow
        }

        val ip = userIpDetails.first()
        val key = remoteConfig.allApiKeys()?.music ?: ""

        val list = mutableListOf<MusicData>()

        names.forEach {
            if (it.trim().isEmpty()) return@forEach

            val r = youtubeMusicAPI
                .youtubeSearchAllSongsResponse(ytMusicArtistsAlbumsJsonBody(ip, it), key)

            r.contents?.tabbedSearchResultsRenderer?.tabs?.forEach { tabs ->
                tabs?.tabRenderer?.content?.sectionListRenderer?.contents?.forEach { c ->
                    c?.musicShelfRenderer?.contents?.forEachIndexed { index, content ->
                        if (index > 2) return@forEachIndexed
                        val thumbnail = content?.musicResponsiveListItemRenderer?.thumbnail
                            ?.musicThumbnailRenderer?.thumbnail?.thumbnailURL()

                        val name = content?.theName()
                        val artists = content?.getArtists()

                        var isAlbums = false

                        content?.musicResponsiveListItemRenderer?.flexColumns?.forEach { flex ->
                            flex?.musicResponsiveListItemFlexColumnRenderer?.text?.runs?.forEach { r ->
                                if (r?.text?.lowercase() == "album") isAlbums = true
                            }
                        }
                        val pId =
                            content?.musicResponsiveListItemRenderer?.navigationEndpoint?.browseEndpoint?.browseId

                        if (isAlbums) name?.let {
                            if (name.trim().isNotEmpty())
                                list.add(MusicData(thumbnail, name, artists, pId, MusicType.ALBUMS))
                        }
                    }

                }
            }

        }

        list.shuffle()
        list.shuffle()
        list.shuffle()

        TopSuggestMusicData(System.currentTimeMillis(), names, list)
            .toTxtCache()?.let { writeToCacheFile(albumsForYouCache, it) }

        emit(list)
    }.flowOn(Dispatchers.IO)


    override suspend fun songsSuggestionsForUsers(sId: List<String>) = flow {
        val cache = responseCache(suggestionYouMayLikeCache, SongsSuggestionsData::class.java)
        if (cache != null) if (cache.cacheSId == sId) {
            emit(cache)
            return@flow
        }

        val ip = userIpDetails.first()
        val key = remoteConfig.allApiKeys()?.music ?: ""

        val upNextList = mutableListOf<MusicData>()
        val relatedList = mutableListOf<MusicData>()
        val artist = mutableListOf<MusicData>()

        sId.forEach { id ->
            if (id.trim().isEmpty()) return@forEach

            var upNextID = ""
            val upNext =
                youtubeMusicAPI.youtubeNextSearchResponse(ytMusicUpNextJsonBody(ip, id), key)

            upNext.contents?.singleColumnMusicWatchNextResultsRenderer?.tabbedRenderer?.watchNextTabbedResultsRenderer?.tabs?.forEach { tab ->
                if (tab?.tabRenderer?.endpoint?.browseEndpoint?.browseEndpointContextSupportedConfigs?.browseEndpointContextMusicConfig?.pageType == "MUSIC_PAGE_TYPE_TRACK_RELATED")
                    upNextID = tab.tabRenderer.endpoint.browseEndpoint.browseId ?: ""
            }

            upNext.contents?.singleColumnMusicWatchNextResultsRenderer?.tabbedRenderer?.watchNextTabbedResultsRenderer?.tabs?.forEach { tab ->
                tab?.tabRenderer?.content?.musicQueueRenderer?.content?.playlistPanelRenderer?.contents?.forEach { content ->
                    val name = content?.playlistPanelVideoRenderer?.title?.runs?.first()?.text
                    val artists = mutableListOf<String>()

                    content?.playlistPanelVideoRenderer?.longBylineText?.runs?.forEach { a ->
                        if (a?.navigationEndpoint?.browseEndpoint?.browseEndpointContextSupportedConfigs?.browseEndpointContextMusicConfig?.pageType == "MUSIC_PAGE_TYPE_ARTIST")
                            if (!artists.any {
                                    it.trim().lowercase() == a.text?.trim()?.lowercase()
                                })
                                a.text?.let { artists.add(it) }
                    }

                    val thumbnail = content?.playlistPanelVideoRenderer?.thumbnailURL()
                    val pID = content?.playlistPanelVideoRenderer?.videoId ?: ""

                    val music = MusicData(
                        thumbnail, name, artistsListToString(artists), pID, MusicType.MUSIC
                    )
                    if (!upNextList.any { it.pId == pID } && !relatedList.any { it.pId == pID })
                        upNextList.add(music)
                }
            }

            val related = youtubeMusicAPI
                .youtubeBrowseResponse(ytMusicBrowseSuggestJsonBody(ip, upNextID), key)

            related.contents?.sectionListRenderer?.contents?.forEach { c ->
                if (c?.musicCarouselShelfRenderer?.header?.musicCarouselShelfBasicHeaderRenderer?.title?.runs?.first()?.text?.lowercase() == "you might also like") {
                    var name: String? = ""
                    val artists = mutableListOf<String>()

                    c.musicCarouselShelfRenderer.contents?.forEach { content ->
                        content?.musicResponsiveListItemRenderer?.flexColumns?.forEach { item ->
                            item?.musicResponsiveListItemFlexColumnRenderer?.text?.runs?.forEach { n ->
                                if (n?.navigationEndpoint?.watchEndpoint?.watchEndpointMusicSupportedConfigs?.watchEndpointMusicConfig?.musicVideoType == "MUSIC_VIDEO_TYPE_ATV")
                                    name = n.text
                            }

                            item?.musicResponsiveListItemFlexColumnRenderer?.text?.runs?.forEach { n ->
                                if (n?.navigationEndpoint?.browseEndpoint?.browseEndpointContextSupportedConfigs?.browseEndpointContextMusicConfig?.pageType == "MUSIC_PAGE_TYPE_ARTIST")
                                    if (!artists.any {
                                            it.trim().lowercase() == n.text?.trim()?.lowercase()
                                        })
                                        n.text?.let { artists.add(it) }
                            }

                            val thumbnail =
                                content.musicResponsiveListItemRenderer.thumbnail?.musicThumbnailRenderer?.thumbnail?.thumbnailURL()
                            val pID =
                                content.musicResponsiveListItemRenderer.playlistItemData?.videoId
                                    ?: ""

                            name?.let { n ->
                                val music = MusicData(
                                    thumbnail, n, artistsListToString(artists), pID, MusicType.MUSIC
                                )
                                if (!upNextList.any { it.pId == pID } && !relatedList.any { it.pId == pID })
                                    relatedList.add(music)
                            }
                        }
                    }
                } else if (c?.musicCarouselShelfRenderer?.header?.musicCarouselShelfBasicHeaderRenderer?.title?.runs?.first()?.text?.lowercase() == "similar artists") {
                    c.musicCarouselShelfRenderer.contents?.forEach { content ->
                        val thumbnail =
                            content?.musicTwoRowItemRenderer?.thumbnailRenderer?.musicThumbnailRenderer?.thumbnailURL()
                        var name: String? = ""
                        var ids: String? = ""
                        content?.musicTwoRowItemRenderer?.title?.runs?.forEach { a ->
                            if (a?.navigationEndpoint?.browseEndpoint?.browseEndpointContextSupportedConfigs?.browseEndpointContextMusicConfig?.pageType == "MUSIC_PAGE_TYPE_ARTIST") {
                                name = a.text
                                ids = a.navigationEndpoint.browseEndpoint.browseId
                            }
                        }

                        val music = MusicData(
                            thumbnail, name, name, ids, MusicType.ARTISTS
                        )
                        if (!artist.any { it.name?.lowercase() == name?.lowercase() })
                            artist.add(music)
                    }
                }
            }
        }

        (1..4).forEach { _ ->
            artist.shuffle()
        }

        val d = SongsSuggestionsData(sId, upNextList, relatedList, artist)
        d.toTxtCache()?.let { writeToCacheFile(suggestionYouMayLikeCache, it) }

        emit(d)
    }.flowOn(Dispatchers.IO)


    override suspend fun artistsFansItemSearch(artists: List<String>) = flow {
        val cache = responseCache(artistsFanWithSongsCache, ArtistsFanDataCache::class.java)
        if (cache != null) if (cache.aList == artists) {
            emit(cache.list)
            return@flow
        }

        val list = mutableListOf<ArtistsFanData>()

        val ip = userIpDetails.first()
        val key = remoteConfig.allApiKeys()?.music ?: ""

        artists.forEach { a ->
            if (a.trim().isEmpty()) return@forEach

            try {
                val searchResponse =
                    youtubeMusicAPI.youtubeSearchResponse(ytMusicArtistsSearchJsonBody(ip, a), key)

                var artist: Pair<String?, String?> = Pair("", "")

                searchResponse.contents?.tabbedSearchResultsRenderer?.tabs?.first()?.tabRenderer?.content?.sectionListRenderer?.contents?.forEachIndexed { index, s ->
                    if (index != 0) return@forEachIndexed

                    if (s?.musicShelfRenderer?.title?.runs?.first()?.text?.lowercase() == "artists") {
                        val thumbnail =
                            s.musicShelfRenderer.contents?.first()?.musicResponsiveListItemRenderer?.thumbnail
                                ?.musicThumbnailRenderer?.thumbnail?.thumbnailURL()

                        val name = s.musicShelfRenderer.getArtistsNoCheck()
                        name?.let {
                            artist = Pair(name, thumbnail)
                        }
                    }
                }

                artist.first ?: return@forEach

                val songs = mutableListOf<MusicData>()

                val r = youtubeMusicAPI.youtubeSearchAllSongsResponse(
                    ytMusicSearchAllSongsJsonBody(ip, artist.first!!), key
                )

                r.contents?.tabbedSearchResultsRenderer?.tabs?.first()?.tabRenderer?.content?.sectionListRenderer?.contents?.forEach { c ->
                    c?.musicShelfRenderer?.contents?.forEach { shelf ->
                        val thumbnail = shelf?.musicResponsiveListItemRenderer?.thumbnail
                            ?.musicThumbnailRenderer?.thumbnail?.thumbnailURL()
                        val name = shelf?.musicResponsiveListItemRenderer?.names()
                        val artistsN = shelf?.getArtists()

                        val m = MusicData(
                            thumbnail ?: "", name?.first, artistsN, name?.second, MusicType.MUSIC
                        )
                        songs.add(m)
                    }
                }

                if (!list.any { it.artistsName.lowercase() == artist.first!!.lowercase() })
                    list.add(ArtistsFanData(artist.first!!, artist.second ?: "", songs))
            } catch (e: Exception) {
                e.message
            }
        }

        ArtistsFanDataCache(artists, list).toTxtCache()
            ?.let { writeToCacheFile(artistsFanWithSongsCache, it) }

        emit(list)
    }.flowOn(Dispatchers.IO)


    override suspend fun searchTextsSuggestions(q: String) = flow {
        val search = mutableListOf<MusicData>()

        val ip = userIpDetails.first()
        val key = remoteConfig.allApiKeys()?.music ?: ""

        val r = youtubeMusicAPI
            .youtubeSearchTextSuggestionResponse(ytMusicSearchSuggestionJsonBody(ip, q), key)

        r.contents?.forEach { content ->
            content?.searchSuggestionsSectionRenderer?.contents?.forEach { item ->
                if (item?.searchSuggestionRenderer?.icon?.iconType?.lowercase() == "search") {
                    val name =
                        item.searchSuggestionRenderer.navigationEndpoint?.searchEndpoint?.query
                            ?: ""
                    val m = MusicData("", name, name, "", MusicType.TEXT)
                    search.add(m)
                }
            }
        }

        emit(search)
    }.flowOn(Dispatchers.IO)


    override suspend fun songDetail(songId: String) = flow {
        val ip = userIpDetails.first()
        val key = remoteConfig.allApiKeys()?.music ?: ""

        val r = youtubeMusicAPI.songDetail(ytMusicMusicDetails(ip, songId), key)

        val vId = r.videoDetails?.videoId
        val name = r.videoDetails?.title
        val artists = r.videoDetails?.author
        val thumbnail = r.videoDetails?.thumbnailURL()

        emit(MusicData(thumbnail, name, artists, vId, MusicType.MUSIC))
    }.flowOn(Dispatchers.IO)


    override suspend fun artistsShopMerchandise(vId: String) = flow {
        val items = mutableListOf<MerchandiseItems>()
        val ip = userIpDetails.first()
        val key = remoteConfig.allApiKeys()?.yt ?: ""

        val r = youtubeAPI
            .youtubeArtistsMerchandiseResponse(ytMerchandiseInfoJsonBody(ip, vId), key)

        r.contents?.twoColumnWatchNextResults?.results?.results?.contents?.forEach {
            if (it?.merchandiseShelfRenderer?.title?.lowercase()?.contains("shop") == true &&
                it.merchandiseShelfRenderer.title.lowercase().contains("store")
            ) it.merchandiseShelfRenderer.items?.forEach { m ->
                var url = ""
                m?.merchandiseItemRenderer?.buttonCommand?.commandExecutorCommand?.commands?.forEach {
                    if (it?.commandMetadata?.webCommandMetadata?.url != null && url.isEmpty())
                        url = it.commandMetadata.webCommandMetadata.url
                }
                val i = MerchandiseItems(
                    m?.merchandiseItemRenderer?.description,
                    m?.merchandiseItemRenderer?.title,
                    m?.merchandiseItemRenderer?.thumbnail?.thumbnails?.maxBy { t ->
                        t?.height ?: 0
                    }?.url,
                    m?.merchandiseItemRenderer?.price,
                    url,
                )
                items.add(i)
            }
        }

        emit(items)
    }

    override suspend fun searchData(q: String) = flow {
        val albums = mutableListOf<MusicData>()
        val artists = mutableListOf<MusicData>()

        val ip = userIpDetails.first()
        val key = remoteConfig.allApiKeys()?.music ?: ""

        val albumResponse = youtubeMusicAPI
            .youtubeSearchAllSongsResponse(ytMusicArtistsAlbumsJsonBody(ip, q), key)

        albumResponse.contents?.tabbedSearchResultsRenderer?.tabs?.forEach { tabs ->
            tabs?.tabRenderer?.content?.sectionListRenderer?.contents?.forEach { c ->
                c?.musicShelfRenderer?.contents?.forEach { content ->
                    val thumbnail = content?.musicResponsiveListItemRenderer?.thumbnail
                        ?.musicThumbnailRenderer?.thumbnail?.thumbnailURL()
                    val name = content?.theName()

                    var isAlbums = false

                    content?.musicResponsiveListItemRenderer?.flexColumns?.forEach { flex ->
                        flex?.musicResponsiveListItemFlexColumnRenderer?.text?.runs?.forEach { r ->
                            if (r?.text?.lowercase() == "album") isAlbums = true
                        }
                    }

                    val pId =
                        content?.musicResponsiveListItemRenderer?.navigationEndpoint?.browseEndpoint?.browseId

                    if (isAlbums) name?.let {
                        if (name.trim().isNotEmpty())
                            albums.add(MusicData(thumbnail, name, q, pId, MusicType.ALBUMS))
                    }
                }
            }
        }

        val songs = allSongsSearch(q).first()

        val artistsResponse =
            youtubeMusicAPI.youtubeSearchResponse(ytMusicArtistsSearchJsonBody(ip, q), key)

        artistsResponse.contents?.tabbedSearchResultsRenderer?.tabs?.first()?.tabRenderer?.content?.sectionListRenderer?.contents?.forEach { s ->
            if (s?.musicShelfRenderer?.title?.runs?.first()?.text?.lowercase() == "artists") {
                s.musicShelfRenderer.contents?.forEach { c ->
                    val thumbnail = c?.musicResponsiveListItemRenderer?.thumbnail
                        ?.musicThumbnailRenderer?.thumbnail?.thumbnailURL()

                    val name =
                        c?.musicResponsiveListItemRenderer?.flexColumns?.first()?.musicResponsiveListItemFlexColumnRenderer?.text?.runs?.first()?.text

                    val id =
                        c?.musicResponsiveListItemRenderer?.navigationEndpoint?.browseEndpoint?.browseId

                    val music = MusicData(thumbnail, name, name, id, MusicType.ARTISTS)

                    name?.let {
                        artists.add(music)
                    }
                }
            }
        }

        emit(SearchData(songs, albums, artists))
    }.flowOn(Dispatchers.IO)
}
