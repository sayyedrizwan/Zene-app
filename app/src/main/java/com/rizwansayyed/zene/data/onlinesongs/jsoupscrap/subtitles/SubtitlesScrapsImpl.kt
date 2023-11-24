package com.rizwansayyed.zene.data.onlinesongs.jsoupscrap.subtitles

import com.rizwansayyed.zene.data.onlinesongs.jsoupscrap.jsoupResponseData
import com.rizwansayyed.zene.data.utils.GeniusURL.GENIUS_BASE_URL
import com.rizwansayyed.zene.data.utils.GeniusURL.geniusMusicSearch
import com.rizwansayyed.zene.data.utils.RentAdvisorSubtitles.RENT_ADVISER_BASE_URL
import com.rizwansayyed.zene.data.utils.RentAdvisorSubtitles.searchOnRentAdviser
import com.rizwansayyed.zene.data.utils.moshi
import com.rizwansayyed.zene.domain.MusicPlayerList
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

    override suspend fun searchSubtitles(data: MusicPlayerList) = flow {
        val searchName = "${data.songName?.substringBefore("-")?.substringBefore("(")} - " +
                data.artists?.substringBefore(",")?.substringBefore("&")
        val response = jsoupResponseData(searchOnRentAdviser(searchName))
        val jsoup = Jsoup.parse(response!!)

        val link = jsoup.selectFirst("div#tablecontainer")?.selectFirst("a")
        val subtitleLink = "$RENT_ADVISER_BASE_URL${link?.attr("href")}"

        if (areSongNamesEqual("${data.artists} - ${data.songName}", link?.text() ?: "")) {
            val subtitleResponse = jsoupResponseData(subtitleLink)
            val subtitleJsoup = Jsoup.parse(subtitleResponse!!)
                .selectFirst("span#ctl00_ContentPlaceHolder1_lbllyrics_simple")
            val text = "[00:00.00]  \uD83C\uDFB6" +
                    (subtitleJsoup?.select("br")?.mapNotNull { it.nextSibling() }
                        ?.joinToString("\n")
                        ?.replace("by RentAnAdviser.com", " \uD83C\uDFB6") ?: "")
            emit(GeniusLyricsWithInfo(data.songID ?: "", text, "", true))

            return@flow
        }

        val lyrics = searchSubtitlesOnGenius(data).first()

        emit(lyrics)
    }.flowOn(Dispatchers.IO)


    override suspend fun searchSubtitlesOnGenius(data: MusicPlayerList) = flow {
        val searchName = "${data.artists?.substringBefore(",")?.substringBefore("&")} - " +
                data.songName?.substringBefore("-")?.substringBefore("(")
        val response = jsoupResponseData(geniusMusicSearch(searchName))

        val searchData = moshi.adapter(GeniusSearchResponse::class.java).fromJson(response!!)
        var link = ""
        searchData?.response?.sections?.forEach { s ->
            if (s?.type == "top_hit") s.hits?.forEach { h ->
                if (areSongNamesEqual(
                        "${data.artists} - ${data.songName}", h?.result?.full_title ?: ""
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

        var lyrics = ""
            lyricsJsoup.select("div.Lyrics__Container-sc-1ynbvzw-1.kUgSbL").forEach {
                lyrics += it.text()
            }
        emit(GeniusLyricsWithInfo(data.songID ?: "", lyrics, songInfo, false))
    }.flowOn(Dispatchers.IO)
}