package com.rizwansayyed.zene.data.onlinesongs.jsoupscrap.bing

import android.util.Log
import com.rizwansayyed.zene.data.db.artistspin.PinnedArtistsEntity
import com.rizwansayyed.zene.data.onlinesongs.cache.writeToCacheFile
import com.rizwansayyed.zene.data.onlinesongs.jsoupscrap.jsoupResponseData
import com.rizwansayyed.zene.data.onlinesongs.lastfm.implementation.LastFMImplInterface
import com.rizwansayyed.zene.data.utils.BingURL.BING_SEARCH
import com.rizwansayyed.zene.data.utils.BingURL.bingAccountSearch
import com.rizwansayyed.zene.data.utils.BingURL.bingOfficialAccountSearch
import com.rizwansayyed.zene.di.ApplicationModule.Companion.context
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import org.jsoup.Jsoup
import java.io.File
import javax.inject.Inject

class BingScrapsImpl @Inject constructor() : BingScrapsInterface {

    override suspend fun bingOfficialVideo(a: String) = flow {
        val response =
            jsoupResponseData("${BING_SEARCH}${a.replace("+", "").trim()}+official+video+youtube")
        val jsoup = Jsoup.parse(response!!)

        var id = ""

        jsoup.select("#mc_vtvc_video_4").forEach {
            val title =
                it.selectFirst(".mc_vtvc_title.b_promtxt")?.attr("title")?.trim()?.lowercase() ?: ""

            if (!id.contains("youtube.com/watch") && id.isEmpty() && title.lowercase()
                    .contains("official") && title.lowercase()
                    .contains("video") && !title.lowercase().lowercase().contains("ft.")
            ) id = it.selectFirst(".mc_vtvc_con_rc")?.attr("ourl") ?: ""
        }

        emit(id.substringAfter("?v="))
    }.flowOn(Dispatchers.IO)


    override suspend fun bingOfficialAccounts(info: PinnedArtistsEntity) = flow {
        val response = jsoupResponseData(bingOfficialAccountSearch(info.name))
        val jsoup = Jsoup.parse(response!!)

        jsoup.selectFirst("div.l_ecrd_webicons")?.select("a")?.forEach {
            val url = it.attr("href")

            if (url.contains("instagram.com/"))
                info.instagramUsername = url.substringAfter("instagram.com/").replace("/", "")
            else if (url.contains("twitter.com/"))
                info.xChannel = url.substringAfter("twitter.com/").replace("/", "")
            else if (url.contains("facebook.com/"))
                info.facebookPage = url.substringAfter("facebook.com/").replace("/", "")
            else if (url.contains("tiktok.com/"))
                info.tiktokPage = url.substringAfter("tiktok.com/").replace("/", "")
            else if (url.contains("youtube.com/"))
                info.youtubeChannel =
                    url.substringAfter("youtube.com/").replace("channel", "").replace("/", "")
        }

        if (info.instagramUsername.length <= 3) {
            val iResponse =
                Jsoup.parse(jsoupResponseData(bingAccountSearch("${info.name} instagram"))!!)
                    .selectFirst("li.b_algo")?.selectFirst("a")
                    ?.attr("href")

            if (iResponse?.contains("instagram.com/") == true)
                info.instagramUsername = iResponse.substringAfter("instagram.com/").replace("/", "")
            else
                info.instagramUsername = "none"
        }

        if (info.xChannel.length <= 3) {
            val iResponse =
                Jsoup.parse(jsoupResponseData(bingAccountSearch("${info.name} twitter"))!!)
                    .selectFirst("li.b_algo")?.selectFirst("a")
                    ?.attr("href")

            if (iResponse?.contains("twitter.com/") == true)
                info.xChannel = iResponse.substringAfter("twitter.com/").replace("/", "")
            else
                info.xChannel = "none"
        }

        if (info.facebookPage.length <= 3) {
            val iResponse =
                Jsoup.parse(jsoupResponseData(bingAccountSearch("${info.name} facebook page"))!!)
                    .selectFirst("li.b_algo")?.selectFirst("a")
                    ?.attr("href")


            if (iResponse?.contains("facebook.com/") == true)
                info.facebookPage = iResponse.substringAfter("facebook.com/").replace("/", "")
            else
                info.facebookPage = "none"
        }

        if (info.tiktokPage.length <= 3) {
            val iResponse =
                Jsoup.parse(jsoupResponseData(bingAccountSearch("${info.name} tiktok"))!!)
                    .selectFirst("li.b_algo")?.selectFirst("a")
                    ?.attr("href")

            if (iResponse?.contains("tiktok.com/") == true)
                info.tiktokPage = iResponse.substringAfter("tiktok.com/").replace("/", "")
            else
                info.tiktokPage = "none"
        }

        if (info.youtubeChannel.length <= 3) {
            val iResponse =
                Jsoup.parse(jsoupResponseData(bingAccountSearch("${info.name} youtube"))!!)
                    .selectFirst("li.b_algo")?.selectFirst("a")
                    ?.attr("href")

            if (iResponse?.contains("youtube.com/") == true)
                info.youtubeChannel =
                    iResponse.substringAfter("youtube.com/").replace("channel", "").replace("/", "")
            else
                info.youtubeChannel = "none"
        }
        emit(info)
    }.flowOn(Dispatchers.IO)

}