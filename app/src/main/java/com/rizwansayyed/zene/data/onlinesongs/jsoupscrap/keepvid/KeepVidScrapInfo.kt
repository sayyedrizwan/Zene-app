package com.rizwansayyed.zene.data.onlinesongs.jsoupscrap.keepvid

import android.util.Log
import com.rizwansayyed.zene.data.onlinesongs.jsoupscrap.jsoupResponseData
import com.rizwansayyed.zene.data.utils.ScrapURL
import com.rizwansayyed.zene.data.utils.SongDownloader.keepVidButtonBaseURL
import com.rizwansayyed.zene.data.utils.moshi
import com.rizwansayyed.zene.domain.download.KeepVideoTsId
import org.jsoup.Jsoup
import javax.inject.Inject

class KeepVidScrapInfo @Inject constructor() {

    suspend fun getTsId(sId: String): KeepVideoTsId? {
        val response = jsoupResponseData(keepVidButtonBaseURL(sId))
        val jsoup = Jsoup.parse(response!!)

        var responseJson = ""

        jsoup.select("script").forEach {
            if (it.html().contains("ajax"))
                responseJson = it.html().substringAfter("data: { ").substringBefore("},").trim()
        }

        return moshi.adapter(KeepVideoTsId::class.java)
            .fromJson("{${responseJson.replace("'", "\"")}}")
    }
}