package com.rizwansayyed.zene.data.onlinesongs.jsoupscrap.songkick

import android.util.Log
import com.rizwansayyed.zene.data.onlinesongs.jsoupscrap.jsoupResponseData
import com.rizwansayyed.zene.data.utils.ScrapURL.songKickArtistsCalendar
import com.rizwansayyed.zene.data.utils.ScrapURL.songKickArtistsCalendarInfo
import com.rizwansayyed.zene.data.utils.ScrapURL.songKickArtistsSearch
import com.rizwansayyed.zene.domain.ArtistsEvents
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import org.jsoup.Jsoup
import javax.inject.Inject

class SongKickScrapsImpl @Inject constructor() : SongKickScrapsImplInterface {

    override suspend fun artistsEvents(a: String) = flow {
        val list = ArrayList<ArtistsEvents>(100)

        val response = jsoupResponseData(songKickArtistsSearch(a))
        val src = Jsoup.parse(response!!).selectFirst("li.artist")?.selectFirst("a")?.attr("href")

        val responseCalendar = jsoupResponseData(songKickArtistsCalendar(src ?: ""))
        val calendars = Jsoup.parse(responseCalendar!!).select("li.event-listing")

        calendars.forEach { c ->
            val path = c.selectFirst("a")?.attr("href") ?: ""
            val cRes = Jsoup.parse(jsoupResponseData(songKickArtistsCalendarInfo(path))!!)

            Log.d("TAG", "artistsEvents: the srcs ${cRes.selectFirst("div.additional-details-container")?.text()}")
        }

        emit("")
    }.flowOn(Dispatchers.IO)

}