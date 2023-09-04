package com.rizwansayyed.zene.presenter.jsoup

import com.rizwansayyed.zene.utils.Utils
import com.rizwansayyed.zene.utils.Utils.showToast
import com.rizwansayyed.zene.utils.downloadHTMLOkhttp
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.jsoup.Jsoup

class WikipediaInfoManager(
    private val name: String, private val artists: String, private val data: (String) -> Unit
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
        val response = downloadHTMLOkhttp(searchWikipedia())
        val document = Jsoup.parse(response!!)

        val firstPath = document.selectFirst("div.mw-search-result-heading")?.selectFirst("a")
        if (firstPath?.attr("title")?.lowercase() == name.lowercase()) {
            wikiPath = firstPath.attr("href") ?: ""
            wikiPath.showToast()
        }

        wikiInfo()
    }

    private fun wikiInfo() = CoroutineScope(Dispatchers.IO).launch {
        if (wikiPath.isEmpty()) {
            return@launch
        }

        val response = downloadHTMLOkhttp(infoWikipedia())
        val document = Jsoup.parse(response!!)

        val stringBuilder = StringBuilder()

        document.selectFirst("div.mw-parser-output")?.select("p")?.forEachIndexed { index, element ->
            if (index < 6) {
                stringBuilder.append(element.html())
                stringBuilder.append("\n\n")
            }
        }

        data(stringBuilder.toString())

    }
}