package com.rizwansayyed.zene.ui.artists.artistviewmodel

import android.content.Context
import android.net.Uri
import com.rizwansayyed.zene.ui.artists.model.ArtistsAlbumsData
import com.rizwansayyed.zene.ui.artists.model.ArtistsSongsData
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.withContext
import org.jsoup.Jsoup
import java.net.URL
import javax.inject.Inject


class ArtistsDataJsoup @Inject constructor(@ApplicationContext private val context: Context) {

    suspend fun artistsListeners(url: String) = flow {
        val document =
            Jsoup.parse(withContext(Dispatchers.IO) { URL(url).openStream() }, "ISO-8859-1", url)
        val play = document.selectFirst("abbr.intabbr.js-abbreviated-counter")?.text()

        if (play == null) emit("")
        else emit(play)
    }

    suspend fun artistsBio(url: String) = flow {
        val urlBio = "$url/+wiki"
        val document =
            Jsoup.parse(
                withContext(Dispatchers.IO) { URL(urlBio).openStream() },
                "ISO-8859-1",
                urlBio
            )
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
        val list = ArrayList<ArtistsSongsData>(80)
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
                list.add(ArtistsSongsData(name, img, numbers))
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


    suspend fun similarArtists(name: String) = flow {
        val search = "https://www.google.com/search?q=" +
                "${name.lowercase().replace(" ", "+")}+similar+artists+site%3Alast.fm"

        emit(firstUrls(search))
    }


    suspend fun artistData(name: String) = flow {
        val search = "https://www.google.com/search?q=" +
                "${name.lowercase().replace(" ", "+")}+artist+site%3Alast.fm/music"

        emit(firstUrls(search))
    }

    suspend fun instagramAccounts(name: String) = flow {
        val search =
            "https://www.google.com/search?q=${name.lowercase().replace(" ", "+")}" +
                    "+official+account+on+instagram+site%3Ainstagram.com"

        emit(firstUrls(search))
    }

    suspend fun twitterAccounts(name: String) = flow {
        val search =
            "https://www.google.com/search?q=${name.lowercase().replace(" ", "+")}" +
                    "+official+account+on+twitter+site%3Atwitter.com"

        emit(firstUrls(search))
    }


    private fun firstUrls(url: String): String? {
        val document = Jsoup.connect(url).timeout(20000)
            .userAgent("Mozilla/5.0 (Windows; U; WindowsNT 5.1; en-US; rv1.8.1.6) Gecko/20070725 Firefox/2.0.0.6")
            .referrer("http://www.google.com").get()

        val gUrl = document.select("div.ezO2md").first()?.selectFirst("a")?.attr("href")

        return Uri.parse("https://google.com$gUrl").getQueryParameter("q")
    }

}