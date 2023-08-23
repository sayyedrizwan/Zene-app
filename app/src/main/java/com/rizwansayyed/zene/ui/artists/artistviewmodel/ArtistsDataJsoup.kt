package com.rizwansayyed.zene.ui.artists.artistviewmodel

import android.content.Context
import com.rizwansayyed.zene.ui.artists.model.ArtistsAlbumsData
import com.rizwansayyed.zene.ui.artists.model.ArtistsSongsData
import com.rizwansayyed.zene.utils.Utils.showToast
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.withContext
import org.jsoup.Jsoup
import java.net.URL
import javax.inject.Inject


class ArtistsDataJsoup @Inject constructor(@ApplicationContext private val context: Context) {

    fun artistsBio(url: String) = flow {
        val urlBio = "$url/+wiki"
        val document =
            Jsoup.parse(withContext(Dispatchers.IO) { URL(urlBio).openStream() }, "ISO-8859-1", url)
        val desc = document.selectFirst("div.wiki-content")?.selectFirst("p")?.text()

        if (desc == null) emit("")
        else emit(desc)
    }

    fun artistsImages(url: String) = flow {
        val urlBio = "$url/+images"
        val images = ArrayList<String>(50)
        val document =
            Jsoup.parse(withContext(Dispatchers.IO) { URL(urlBio).openStream() }, "ISO-8859-1", url)
        document.select("li.image-list-item-wrapper").forEach {
            it.selectFirst("img")?.attr("src")?.let { src ->
                if (src.isNotEmpty())
                    images.add(src.replace("avatar170s", "770x0"))
            }
        }

        emit(images)
    }

    fun artistsTopSongs(url: String) = flow {
        val urlBio = "$url/+tracks"
        val list = ArrayList<ArtistsSongsData>(80)
        val document =
            Jsoup.parse(withContext(Dispatchers.IO) { URL(urlBio).openStream() }, "ISO-8859-1", url)
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


    fun artistsTopAlbums(url: String) = flow {
        val urlBio = "$url/+albums"
        val list = ArrayList<ArtistsAlbumsData>(40)
        val document =
            Jsoup.parse(withContext(Dispatchers.IO) { URL(urlBio).openStream() }, "ISO-8859-1", url)

        document.selectFirst("section#artist-albums-section")?.select("li")?.forEach {
            val img =
                it.selectFirst("span.resource-list--release-list-item-image.cover-art")?.selectFirst("img")
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
}