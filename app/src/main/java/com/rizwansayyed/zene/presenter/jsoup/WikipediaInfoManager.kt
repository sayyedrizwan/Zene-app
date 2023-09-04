package com.rizwansayyed.zene.presenter.jsoup

import com.rizwansayyed.zene.utils.Utils
import com.rizwansayyed.zene.utils.Utils.showToast
import com.rizwansayyed.zene.utils.downloadHTMLOkhttp
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.jsoup.Jsoup

class WikipediaInfoManager(
    private val name: String,
    private val artists: String,
    private val data: (data: String, url: String) -> Unit
) {

    private var wikiPath = ""

    private fun searchWikipedia(): String {
        return "https://en.wikipedia.org/w/index.php?profile=advanced&search=" +
                "${name.replace(" ", "-")}+-+${artists.replace(" ", "-")}&ns0=1"
    }

    private fun infoWikipedia(): String {
        return "https://en.wikipedia.org/$wikiPath"
    }

    init {
        searchApi()
    }

    private fun searchApi() = CoroutineScope(Dispatchers.IO).launch {
        try {
            val response = downloadHTMLOkhttp(searchWikipedia())
            val document = Jsoup.parse(response!!)

            val firstPath = document.selectFirst("div.mw-search-result-heading")?.selectFirst("a")
            wikiPath = firstPath?.attr("href") ?: ""

            wikiInfo()
        } catch (e: Exception) {
            data("", "")
        }
    }

    private fun wikiInfo() = CoroutineScope(Dispatchers.IO).launch {
        if (wikiPath.isEmpty()) {
            data("", "")
            return@launch
        }

        try {

            val response = downloadHTMLOkhttp(infoWikipedia())
            val document = Jsoup.parse(response!!)

            var stringBuilder = ""

            document.selectFirst("div.mw-parser-output")?.select("p")?.forEach { element ->
                stringBuilder += "${element.html()}\n\n"
            }

            for (i in 0 until 280) {
                stringBuilder = stringBuilder.replace("[$i]", "")
            }
            data(stringBuilder, infoWikipedia())
        } catch (e: Exception) {
            data("", "")
        }

    }
}