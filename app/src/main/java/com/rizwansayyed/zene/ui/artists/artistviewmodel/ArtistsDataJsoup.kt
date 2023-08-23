package com.rizwansayyed.zene.ui.artists.artistviewmodel

import android.content.Context
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
            it.selectFirst("img")?.attr("src")?.let { src -> images.add(src) }
        }

        emit(images)
    }
}