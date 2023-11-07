package com.rizwansayyed.zene.data.onlinesongs.jsoupscrap.bing


import com.rizwansayyed.zene.data.onlinesongs.jsoupscrap.jsoupResponseData
import com.rizwansayyed.zene.data.utils.ScrapURL.BING_SEARCH
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import org.jsoup.Jsoup
import javax.inject.Inject

class BingScrapsImpl @Inject constructor() : BingScrapsInterface {

    override suspend fun bingOfficialVideo(a: String) = flow {
        val response =
            jsoupResponseData("${BING_SEARCH}${a.replace("+", "").trim()}+official+video+youtube")
        val jsoup = Jsoup.parse(response!!)

        var id = ""

        jsoup.select("#mc_vtvc_video_4").forEach {
            if (!id.contains("youtube.com/watch"))
                id = it.selectFirst(".mc_vtvc_con_rc")?.attr("ourl") ?: ""
        }

        emit(id.substringAfter("?v="))
    }.flowOn(Dispatchers.IO)

}