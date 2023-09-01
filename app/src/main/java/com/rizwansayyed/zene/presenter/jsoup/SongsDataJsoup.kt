package com.rizwansayyed.zene.presenter.jsoup

import android.content.Context
import android.util.Log
import com.rizwansayyed.zene.presenter.jsoup.model.AppleMusicPlaylistResponse
import com.rizwansayyed.zene.presenter.model.TopArtistsSongs
import com.rizwansayyed.zene.utils.Utils.URL.searchSongsApple
import com.rizwansayyed.zene.utils.Utils.moshi
import com.rizwansayyed.zene.utils.Utils.showToast
import com.rizwansayyed.zene.utils.downloadHTMLOkhttp
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.flow
import org.json.JSONArray
import org.jsoup.Jsoup
import javax.inject.Inject

class SongsDataJsoup @Inject constructor(@ApplicationContext private val context: Context) {

    suspend fun appleMusicCountryTrends(country: String) = flow {
        val lists = ArrayList<TopArtistsSongs>(150)
        val response = downloadHTMLOkhttp(searchSongsApple(country))
        val document = Jsoup.parse(response!!)

        val playlistId = document.selectFirst("div.top-search-lockup__action.svelte-ldozvk")
            ?.selectFirst("a")?.attr("href")
        if (playlistId == null) {
            emit(lists)
            return@flow
        }

        val responseSongs = downloadHTMLOkhttp(playlistId.trim())
        val documentSongs = Jsoup.parse(responseSongs!!)

        val data = documentSongs.selectFirst("script#serialized-server-data")
        val json =
            moshi.adapter(Array<AppleMusicPlaylistResponse>::class.java).fromJson(data?.html()!!)

        json?.get(0)?.data?.sections?.forEach { track ->
            if (track?.itemKind == "trackLockup")
                track.items?.forEach { item ->
                    val name = item?.title?.trim() ?: ""
                    val artist = item?.artistName?.trim()
                    val img = item?.artwork?.dictionary?.url?.trim()
                        ?.replace("{w}x{h}bb.{f}", "592x592bb-60.jpg")

                    if (name.isNotEmpty()) {
                        lists.add(TopArtistsSongs(name, img, artist))
                    }
                }
        }
        lists.shuffle()

        emit(lists)
    }


    suspend fun appleMusicKoreanSongs() = flow {
        val lists = ArrayList<TopArtistsSongs>(150)
        val response = downloadHTMLOkhttp(searchSongsApple("korean songs"))
        val document = Jsoup.parse(response!!)

        val playlistId = document.selectFirst("div.top-search-lockup__action.svelte-ldozvk")
            ?.selectFirst("a")?.attr("href")
        if (playlistId == null) {
            emit(lists)
            return@flow
        }

        val responseSongs = downloadHTMLOkhttp(playlistId.trim())
        val documentSongs = Jsoup.parse(responseSongs!!)

        val data = documentSongs.selectFirst("script#serialized-server-data")
        val json =
            moshi.adapter(Array<AppleMusicPlaylistResponse>::class.java).fromJson(data?.html()!!)

        json?.get(0)?.data?.sections?.forEach { track ->
            if (track?.itemKind == "trackLockup")
                track.items?.forEach { item ->
                    val name = item?.title?.trim() ?: ""
                    val artist = item?.artistName?.trim()
                    val img = item?.artwork?.dictionary?.url?.trim()
                        ?.replace("{w}x{h}bb.{f}", "592x592bb-60.jpg")

                    if (name.isNotEmpty()) {
                        Log.d("", "appleMusicKoreanSongs: ${item}")
                        lists.add(TopArtistsSongs(name, img, artist))
                    }
                }
        }
        lists.shuffle()

        emit(lists)
    }

}