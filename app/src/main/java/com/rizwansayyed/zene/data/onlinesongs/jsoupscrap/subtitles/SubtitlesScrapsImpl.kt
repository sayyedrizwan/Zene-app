package com.rizwansayyed.zene.data.onlinesongs.jsoupscrap.subtitles

import android.util.Log
import com.rizwansayyed.zene.data.onlinesongs.cache.writeToCacheFile
import com.rizwansayyed.zene.data.onlinesongs.jsoupscrap.jsoupResponseData
import com.rizwansayyed.zene.data.utils.GeniusURL.GENIUS_BASE_URL
import com.rizwansayyed.zene.data.utils.GeniusURL.geniusMusicSearch
import com.rizwansayyed.zene.data.utils.RentAdvisorSubtitles.RENT_ADVISER_BASE_URL
import com.rizwansayyed.zene.data.utils.RentAdvisorSubtitles.searchOnRentAdviser
import com.rizwansayyed.zene.data.utils.moshi
import com.rizwansayyed.zene.di.ApplicationModule.Companion.context
import com.rizwansayyed.zene.domain.subtitles.GeniusLyricsWithInfo
import com.rizwansayyed.zene.domain.subtitles.GeniusSearchResponse
import com.rizwansayyed.zene.presenter.ui.musicplayer.utils.Utils.areSongNamesEqual
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import org.jsoup.Jsoup
import javax.inject.Inject

class SubtitlesScrapsImpl @Inject constructor() : SubtitlesScrapsImplInterface {

    override suspend fun searchSubtitles(songName: String, artistName: String) = flow {
        val searchName = "${songName.substringBefore("-").substringBefore("(")} - " +
                artistName.substringBefore(",").substringBefore("&")
        val response = jsoupResponseData(searchOnRentAdviser(searchName))
        val jsoup = Jsoup.parse(response!!)

        val link = jsoup.selectFirst("div#tablecontainer")?.selectFirst("a")
        val subtitleLink = "$RENT_ADVISER_BASE_URL${link?.attr("href")}"

        if (areSongNamesEqual("$artistName - $songName", link?.text() ?: "")) {
            val subtitleResponse = jsoupResponseData(subtitleLink)
            val subtitleJsoup = Jsoup.parse(subtitleResponse!!)
                .selectFirst("span#ctl00_ContentPlaceHolder1_lbllyrics_simple")
            val text = "[00:00.00]  \uD83C\uDFB6" +
                    (subtitleJsoup?.select("br")?.mapNotNull { it.nextSibling() }
                        ?.joinToString("\n")
                        ?.replace("by RentAnAdviser.com", " \uD83C\uDFB6") ?: "")
            emit(GeniusLyricsWithInfo(text, "", true))

            return@flow
        }

        val lyrics = searchSubtitlesOnGenius(songName, artistName).first()

        emit(lyrics)
    }.flowOn(Dispatchers.IO)


    override suspend fun searchSubtitlesOnGenius(songName: String, artistName: String) = flow {
        val searchName = "${artistName.substringBefore(",").substringBefore("&")} - " +
                songName.substringBefore("-").substringBefore("(")
        val response = jsoupResponseData(geniusMusicSearch(searchName))

        val searchData = moshi.adapter(GeniusSearchResponse::class.java).fromJson(response!!)
        var link = ""
        searchData?.response?.sections?.forEach { s ->
            if (s?.type == "top_hit") s.hits?.forEach { h ->
                if (areSongNamesEqual(
                        "$artistName - $songName", h?.result?.full_title ?: ""
                    ) && link.isEmpty()
                ) link = h?.result?.path ?: ""
            }
        }

        val lyricsResponse = jsoupResponseData("$GENIUS_BASE_URL$link")
        val lyricsJsoup = Jsoup.parse(lyricsResponse!!)

        var songInfo = ""

        lyricsJsoup.selectFirst("div.SongDescription__Content-sc-615rvk-2.kRzyD")
            ?.select("p")?.forEach {
                songInfo += it.text()
                songInfo += "\n \n"
            }

        val lyrics: String =
            lyricsJsoup.selectFirst("div.Lyrics__Container-sc-1ynbvzw-1.kUgSbL")?.text() ?: ""
        emit(GeniusLyricsWithInfo(lyrics, songInfo, false))
    }.flowOn(Dispatchers.IO)
}