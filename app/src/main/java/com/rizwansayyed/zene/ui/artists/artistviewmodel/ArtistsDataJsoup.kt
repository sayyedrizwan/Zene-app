package com.rizwansayyed.zene.ui.artists.artistviewmodel

import android.content.Context
import android.net.Uri
import android.util.Log
import com.rizwansayyed.zene.presenter.model.TopArtistsSongs
import com.rizwansayyed.zene.ui.artists.artistviewmodel.model.ListenersNumberValue
import com.rizwansayyed.zene.ui.artists.artistviewmodel.model.NewsResponse
import com.rizwansayyed.zene.ui.artists.model.ArtistsAlbumsData
import com.rizwansayyed.zene.ui.artists.model.ArtistsSongsData
import com.rizwansayyed.zene.utils.OkhttpCookies
import com.rizwansayyed.zene.utils.Utils
import com.rizwansayyed.zene.utils.Utils.URL.readNewsUrl
import com.rizwansayyed.zene.utils.Utils.URL.searchArtistsURL
import com.rizwansayyed.zene.utils.Utils.URL.searchViaBing
import com.rizwansayyed.zene.utils.Utils.showToast
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.withContext
import okhttp3.OkHttpClient
import okhttp3.Request
import org.jsoup.Jsoup
import java.net.URL
import javax.inject.Inject


class ArtistsDataJsoup @Inject constructor(@ApplicationContext private val context: Context) {

    private val cookies = OkhttpCookies()

    suspend fun searchArtists(name: String) = flow {
        val response = downloadHTMLOkhttp(searchArtistsURL(name))
        val document = Jsoup.parse(response!!)

        val url =
            document.selectFirst("h4.artist-result-heading")?.select("a")?.attr("href")?.trim()

        if (url == null) emit(null)
        else emit("https://www.last.fm$url")
    }

    suspend fun artistsListeners(url: String) = flow {
        val response = downloadHTMLOkhttp(url)
        val document = Jsoup.parse(response!!)

        val play = document.selectFirst("abbr.intabbr.js-abbreviated-counter")?.text()
        val number = document.selectFirst("abbr.intabbr.js-abbreviated-counter")?.attr("title")

        if (play == null) emit(null)
        else emit(ListenersNumberValue(play, number))
    }

    suspend fun artistsSimilar(url: String) = flow {
        val urlArtists = "$url/+similar"
        val lists = ArrayList<TopArtistsSongs>()

        fun addArtist(response: String?) {
            val document = Jsoup.parse(response!!)

            document.selectFirst("ol.similar-artists.similar-artists--with-10")
                ?.select("li")?.forEach {
                    val name = it.selectFirst("h3.similar-artists-item-name")?.text()?.trim()
                    val img =
                        it.selectFirst("span.avatar.similar-artists-item-image")?.selectFirst("img")
                            ?.attr("src")?.trim()

                    if (name != null && img != null)
                        if (!lists.any { l -> l.name == name }) {
                            lists.add(TopArtistsSongs(name, img, null))
                        }
                }
        }

        val response = downloadHTMLOkhttp(urlArtists)
        addArtist(response)

        val responsePage2 = downloadHTMLOkhttp("$urlArtists?page=2")
        addArtist(responsePage2)

        lists.shuffle()
        emit(lists)
    }

    suspend fun artistsBio(url: String) = flow {
        val urlBio = "$url/+wiki"
        val document =
            Jsoup.parse(withContext(Dispatchers.IO) {
                URL(urlBio).openStream()
            }, "ISO-8859-1", urlBio)
        val desc = document.selectFirst("div.wiki-content")?.selectFirst("p")?.text()

        if (desc == null) emit("")
        else emit(desc)
    }

    suspend fun artistsImages(url: String) = flow {
        val urlBio = "$url/+images"
        val images = ArrayList<String>(50)
        val document =
            Jsoup.parse(
                withContext(Dispatchers.IO) { URL(urlBio).openStream() },
                "ISO-8859-1",
                urlBio
            )
        document.select("li.image-list-item-wrapper").forEach {
            it.selectFirst("img")?.attr("src")?.let { src ->
                if (src.isNotEmpty())
                    images.add(src.replace("avatar170s", "770x0"))
            }
        }

        emit(images)
    }

    suspend fun artistsTopSongs(url: String) = flow {
        val urlBio = "$url/+tracks"
        val list = ArrayList<TopArtistsSongs>(80)
        val document =
            Jsoup.parse(
                withContext(Dispatchers.IO) { URL(urlBio).openStream() },
                "ISO-8859-1",
                urlBio
            )
        document.selectFirst("tbody")?.select("tr")?.forEach {
            val img = it.select("td.chartlist-image").select("img").attr("src").trim()
                .replace("64s", "770x0")
            val name = it.select("td.chartlist-name").select("a").text().trim()
            val numbers =
                it.select("td.chartlist-bar").select("span.chartlist-count-bar-value").text().trim()
            if (img.isNotEmpty() && name.isNotEmpty())
                list.add(TopArtistsSongs(name, img, numbers))
        }

        emit(list)
    }


    suspend fun artistsTopAlbums(url: String) = flow {
        val urlBio = "$url/+albums"
        val list = ArrayList<ArtistsAlbumsData>(40)
        val document =
            Jsoup.parse(
                withContext(Dispatchers.IO) { URL(urlBio).openStream() },
                "ISO-8859-1",
                urlBio
            )

        document.selectFirst("section#artist-albums-section")?.select("li")?.forEach {
            val img =
                it.selectFirst("span.resource-list--release-list-item-image.cover-art")
                    ?.selectFirst("img")
                    ?.attr("src")?.trim()?.replace("avatar170s", "770x0")
            val name =
                it.select("h3.resource-list--release-list-item-name").select("a").text().trim()
            val numbers =
                it.select("p.resource-list--release-list-item-aux-text.resource-list--release-list-item-listeners")
                    .text().trim()
            val date = it.select("p.resource-list--release-list-item-aux-text").text().trim()
                .substringBefore("·").trim()
            val track = it.select("p.resource-list--release-list-item-aux-text").text().trim()
                .substringAfter("·").trim()

            if (img?.isNotEmpty() == true && name.isNotEmpty())
                list.add(ArtistsAlbumsData(name, img, numbers, track, date))
        }

        emit(list)
    }


    suspend fun songLyrics(q: String) = flow {
        val lyricsSearch =
            "https://www.google.com/search?q=${q.lowercase().replace(" ", "+")}" +
                    "+site:ilyricshub.com+OR+site:lyricsgoal.com+OR+site:lyrics.com+OR+site:azlyrics.com"

        emit(firstUrls(lyricsSearch))
    }


    suspend fun topCountrySongs(country: String) = flow {
        val topSongsSearch = "https://www.google.com/search?q=top+songs+" +
                country.lowercase().replace(" ", "+") +
                "+site:music.apple.com+OR+site:open.spotify.com"

        emit(firstUrls(topSongsSearch))
    }

    suspend fun trendingSongsApple(country: String) = flow {
        val topSongsSearch = "https://www.google.com/search?q=trending+today+songs+in+" +
                "${country.lowercase().replace(" ", "+")}+site:music.apple.com"

        emit(firstUrls(topSongsSearch))
    }


    suspend fun trendingSongsTopKPop() = flow {
        val search = "https://www.google.com/search?q=top+trending+k-pop+site:open.spotify.com"
        emit(firstUrls(search))
    }


    suspend fun trendingSongsTop50KPop() = flow {
        val search = "https://www.google.com/search?q=top+50+popular+k-pop+site:open.spotify.com"
        emit(firstUrls(search))
    }

    suspend fun albumsWithHeaders() = flow {
        val search =
            "https://www.google.com/search?q=YouTube+Music+Channel+to+find+today's+top+talent,+featured+artists,+and+playlists.+site:youtube.com"

        emit(firstUrls(search))
    }


    suspend fun artistData(name: String) = flow {
        val search = "https://www.google.com/search?q=" +
                "${name.lowercase().replace(" ", "+")}+artist+site%3Alast.fm/music&start=10"

        emit(firstUrls(search))
    }


    suspend fun artistsNews(name: String) = flow {
        val lists = ArrayList<NewsResponse>(100)

        val response = downloadHTMLOkhttp(readNewsUrl(name))

        val document = Jsoup.parse(response!!)
        document.select("div.NiLAwe.y6IFtc.R7GTQ.keNKEd.j7vNaf.nID9nc").forEach {
            var img = it.selectFirst("img.tvs3Id.QwxBBf")?.attr("srcset") ?: ""
            val time = it.selectFirst("time.WW6dff.uQIVzc.Sksgp.slhocf")?.text()?.trim() ?: ""
            val title = it.selectFirst("a.DY5T1d.RZIKme")?.text()?.trim() ?: ""
            val pImage =
                it.selectFirst("img.tvs3Id.tvs3Id.lqNvvd.ICvKtf.WfKKme.IGhidc")?.attr("src") ?: ""
            val pName =
                it.selectFirst("div.N0NI1d.AVN2gc.WfKKme")?.select("a.wEwyrc")?.text()?.trim() ?: ""
            val link = it.selectFirst("a.VDXfz")?.attr("href")

            img.split(",").forEach { i ->
                if (i.contains("2x")) {
                    img = i.replace("2x", "").replace("w200-h200-", "-w512-h512-").trim()
                }
            }

            val doc =
                NewsResponse(title, "https://news.google.com/$link", time, img, pImage, pName)
            lists.add(doc)
        }

        emit(lists)
    }

    suspend fun instagramTwitterAccounts(name: String) = flow {
        val response = downloadHTMLOkhttp(searchViaBing(name))
        val document = Jsoup.parse(response!!)

        var instagram = ""
        var twitter = ""

        document.select("ol#b_results").select("li.b_algo").forEach {
            val url = it.selectFirst("a.tilk")?.attr("href")
            if (url?.contains("https://www.instagram.com/") == true && instagram.isEmpty()) {
                instagram = url
            }
            if (url?.contains("https://www.twitter.com/") == true && twitter.isEmpty()) {
                twitter = url
            }
        }
        emit(Pair(instagram, twitter))
    }

    private fun firstUrls(url: String): String? {
        val document = Jsoup.connect(url).timeout(20000)
            .userAgent("Mozilla/5.0 (Macintosh; Intel Mac OS X 10_15_7) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/116.0.0.0 Safari/537.36")
            .get()

        var gUrl = document.select("div.ezO2md").first()?.selectFirst("a")?.attr("href")
        var returnUrl = Uri.parse("https://google.com$gUrl").getQueryParameter("q")
        if (returnUrl?.contains("https://www.") == true) return returnUrl

        gUrl = document.select("a.fuLhoc.ZWRArf").first()?.attr("href")
        returnUrl = Uri.parse("https://google.com$gUrl").getQueryParameter("q")
        return returnUrl
    }

    private fun downloadHTMLOkhttp(url: String): String? {
        val client = OkHttpClient().newBuilder().cookieJar(cookies).build()
        val request = Request.Builder().url(url)
            .addHeader("User-Agent", Utils.USER_AGENT).method("GET", null).build()
        val response = client.newCall(request).execute()

        if (response.isSuccessful) {
            return response.body?.string()
        }
        return null
    }
}