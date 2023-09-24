package com.rizwansayyed.zene.data.onlinesongs.youtube.implementation

import android.util.Log
import androidx.compose.runtime.mutableStateListOf
import com.rizwansayyed.zene.data.onlinesongs.cache.writeToCacheFile
import com.rizwansayyed.zene.data.onlinesongs.ip.IpJsonService
import com.rizwansayyed.zene.data.onlinesongs.jsoupscrap.jsoupscrap.JsoupScrapsInterface
import com.rizwansayyed.zene.data.onlinesongs.youtube.YoutubeAPIService
import com.rizwansayyed.zene.data.utils.CacheFiles.updateTheDate
import com.rizwansayyed.zene.data.utils.USER_AGENT
import com.rizwansayyed.zene.data.utils.YoutubeAPI.ytJsonBody
import com.rizwansayyed.zene.data.utils.config.RemoteConfigManager
import com.rizwansayyed.zene.data.utils.moshi
import com.rizwansayyed.zene.domain.yt.YoutubeReleaseChannelResponse
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject


class YoutubeAPIImpl @Inject constructor(
    private val youtubeAPI: YoutubeAPIService,
    private val ipJson: IpJsonService,
    private val remoteConfig: RemoteConfigManager,
    private val jsonScrap: JsoupScrapsInterface
) : YoutubeAPIImplInterface {

    override suspend fun newReleaseMusic() = flow {
        val ip = ipJson.ip()
        val key = remoteConfig.ytApiKeys()!!.yt

        var channelURL = ""
        val musicChannelId = youtubeAPI.youtubePageResponse(ytJsonBody(ip), key)

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

        emit(nameList.size.toString())
    }.flowOn(Dispatchers.IO)
}