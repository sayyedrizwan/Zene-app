package com.rizwansayyed.zene.data.onlinesongs.youtube.implementation

import com.rizwansayyed.zene.data.onlinesongs.ip.IpJsonService
import com.rizwansayyed.zene.data.onlinesongs.jsoupscrap.jsoupscrap.JsoupScrapsInterface
import com.rizwansayyed.zene.data.onlinesongs.youtube.YoutubeAPIService
import com.rizwansayyed.zene.data.utils.USER_AGENT
import com.rizwansayyed.zene.data.utils.YoutubeAPI.ytJsonBody
import com.rizwansayyed.zene.data.utils.config.RemoteConfigManager
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import okhttp3.MediaType
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.toRequestBody
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

        val getChannelInfo = jsonScrap.ytMusicChannelJson(channelURL).first()

        emit(getChannelInfo)
    }.flowOn(Dispatchers.IO)
}