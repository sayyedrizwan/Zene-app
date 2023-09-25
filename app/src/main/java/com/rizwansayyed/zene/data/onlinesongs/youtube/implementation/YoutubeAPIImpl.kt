package com.rizwansayyed.zene.data.onlinesongs.youtube.implementation


import androidx.compose.runtime.mutableStateListOf
import com.rizwansayyed.zene.data.onlinesongs.cache.responseCache
import com.rizwansayyed.zene.data.onlinesongs.cache.returnFromCache2Days
import com.rizwansayyed.zene.data.onlinesongs.cache.writeToCacheFile
import com.rizwansayyed.zene.data.onlinesongs.ip.IpJsonService
import com.rizwansayyed.zene.data.onlinesongs.jsoupscrap.jsoupscrap.JsoupScrapsInterface
import com.rizwansayyed.zene.data.onlinesongs.youtube.YoutubeAPIService
import com.rizwansayyed.zene.data.onlinesongs.youtube.YoutubeMusicAPIService
import com.rizwansayyed.zene.data.utils.CacheFiles
import com.rizwansayyed.zene.data.utils.CacheFiles.freshAddedSongs
import com.rizwansayyed.zene.data.utils.YoutubeAPI.ytJsonBody
import com.rizwansayyed.zene.data.utils.YoutubeAPI.ytMusicJsonBody
import com.rizwansayyed.zene.data.utils.config.RemoteConfigManager
import com.rizwansayyed.zene.data.utils.sortNameForSearch
import com.rizwansayyed.zene.domain.IpJsonResponse
import com.rizwansayyed.zene.domain.MusicData
import com.rizwansayyed.zene.domain.MusicDataCache
import com.rizwansayyed.zene.domain.spotify.SpotifyTracksCacheResponse
import com.rizwansayyed.zene.domain.spotify.toTxtCache
import com.rizwansayyed.zene.domain.toTxtCache
import com.rizwansayyed.zene.domain.yt.YoutubeMusicMainSearchResponse
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject


class YoutubeAPIImpl @Inject constructor(
    private val youtubeAPI: YoutubeAPIService,
    private val youtubeMusicAPI: YoutubeMusicAPIService,
    private val ipJson: IpJsonService,
    private val remoteConfig: RemoteConfigManager,
    private val jsonScrap: JsoupScrapsInterface
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

        val ip = ipJson.ip()
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
        nameList.forEach {
            var sortName = sortNameForSearch(it)
            if (sortName.trim().isEmpty()) sortName = it

            musicInfoSearch(sortName, ip, key?.music ?: "")?.let { m -> music.add(m) }
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

    private suspend fun musicInfoSearch(n: String, ip: IpJsonResponse, key: String): MusicData? {
        if (n.trim().isEmpty()) return null

        var m: MusicData? = null

        try {
            val searchResponse =
                youtubeMusicAPI.youtubeSearchResponse(ytMusicJsonBody(ip, n), key)

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
}