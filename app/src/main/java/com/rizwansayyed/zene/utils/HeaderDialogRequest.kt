package com.rizwansayyed.zene.utils

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.runBlocking
import org.jsoup.Jsoup

object HeaderDialogRequest {

    private val googleDocsHeaderAlertURL =
        "https://docs.google.com/document/d/1fQdKLy33taRrqcXGOrFCRApuQacLL0wFrHTkQCmRHDg/edit?usp=sharing"

    fun getAlertHeader() = runBlocking(Dispatchers.IO) {
        try {
            val doc = Jsoup.connect(googleDocsHeaderAlertURL).get()
            val ogDescription = doc.select("meta[property=og:description]").first()?.attr("content")
            return@runBlocking ogDescription
        } catch (e: Exception) {
            e.printStackTrace()
            return@runBlocking null
        }
    }
}