package com.rizwansayyed.zene.data.onlinesongs.jsoupscrap.subtitles

import android.util.Log
import com.rizwansayyed.zene.data.onlinesongs.cache.writeToCacheFile
import com.rizwansayyed.zene.data.onlinesongs.jsoupscrap.jsoupResponseData
import com.rizwansayyed.zene.data.utils.RentAdvisorSubtitles.RENT_ADVISER_BASE_URL
import com.rizwansayyed.zene.data.utils.RentAdvisorSubtitles.searchOnRentAdviser
import com.rizwansayyed.zene.di.ApplicationModule.Companion.context
import com.rizwansayyed.zene.presenter.ui.musicplayer.utils.Utils.areSongNamesEqual
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import org.jsoup.Jsoup

class SubtitlesScrapsImpl {

    suspend fun searchSubtitles(songName: String, artistName: String) = flow {
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
                    (subtitleJsoup?.select("br")?.mapNotNull { it.nextSibling() }?.joinToString("\n")
                        ?.replace("by RentAnAdviser.com", " \uD83C\uDFB6") ?: "")
            emit(text)

            return@flow
        }


        emit("")
    }.flowOn(Dispatchers.IO)

}