package com.rizwansayyed.zene.presenter.jsoup

import android.content.Context
import android.util.Log
import com.rizwansayyed.zene.presenter.jsoup.model.AppleMusicPlaylistResponse
import com.rizwansayyed.zene.presenter.model.TopArtistsSongs
import com.rizwansayyed.zene.utils.OkhttpCookies
import com.rizwansayyed.zene.utils.Utils.URL.searchSongsApple
import com.rizwansayyed.zene.utils.Utils.URL.searchViaBingHeader
import com.rizwansayyed.zene.utils.Utils.moshi
import com.rizwansayyed.zene.utils.downloadHTMLOkhttp
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.flow
import okhttp3.MediaType
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONObject
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

    suspend fun top5SongsSpotify(country: String) = flow {
        val lists = ArrayList<TopArtistsSongs>(10)

        val response = downloadHTMLOkhttp(searchViaBingHeader(country))
        val document = Jsoup.parse(response!!)

        val url = document.selectFirst("ol#b_results")?.selectFirst("li.b_algo")
            ?.selectFirst("a.tilk")?.attr("href")

        if (url?.contains("spotify.com") == false) {
            emit(lists)
            return@flow
        }

        val responseToken = downloadHTMLOkhttp(url ?: "")

        val jsonToken = Jsoup.parse(responseToken!!).selectFirst("script#session")?.html()
        val accessToken = JSONObject(jsonToken ?: "").getString("accessToken")
        val clientId = JSONObject(jsonToken ?: "").getString("clientId")

        val dataClientToken = JSONObject().apply {
            val clientData = JSONObject().apply {
                put("client_id", clientId)
                put("js_sdk_data", "{}")
            }

            put("client_data", clientData)
        }

        val cookies = OkhttpCookies()

        val client = OkHttpClient()

// Define the URL and request body
        val urlss = "https://clienttoken.spotify.com/v1/clienttoken"
        val requestBodyJson = JSONObject()
        val clientData = JSONObject()
        clientData.put("client_id", "d8a5ed958d274c2e8ee717e6a4b0971d")
        clientData.put("js_sdk_data", JSONObject())
        requestBodyJson.put("client_data", clientData)

// Create a request
        val requestBody = requestBodyJson.toString().toRequestBody("application/json".toMediaType())
        val request = Request.Builder()
            .url(urlss)
            .addHeader("authority", "clienttoken.spotify.com")
            .addHeader("accept", "application/json")
            .addHeader("accept-language", "en-GB,en;q=0.9")
            .addHeader("cache-control", "no-cache")
            .addHeader("content-type", "application/json")
            .post(requestBody)
            .build()

// Execute the request
        val tokenResponse = client.newCall(request).execute()

// Handle the response as needed
        if (tokenResponse.isSuccessful) {
            val responseBody = tokenResponse.body?.string()

            Log.d("TAG", "top5SongsSpotify: get the ${responseBody} ")
            // Process responseBody as JSON or as needed
        } else {

            Log.d("TAG", "top5SongsSpotify: get the error ${tokenResponse.body?.string()}")
            // Handle the error case
        }

// Don't forget to close the response body and the client when done
        tokenResponse.close()



//            if (url?.contains("https://www.instagram.com/") == true && instagram.isEmpty()) {
//                instagram = url
//            }


        emit(lists)

    }

}