package com.rizwansayyed.zene.presenter.util

import android.content.Context
import android.view.View
import android.webkit.WebView
import com.rizwansayyed.zene.R
import java.io.BufferedReader
import java.io.InputStream
import java.io.InputStreamReader

class PlayerMusicWebView(context: Context): WebView(context) {

    private val clientUserAgent =
        "Mozilla/5.0 (iPhone; CPU iPhone OS 17_0 like Mac OS X) AppleWebKit/605.1.15 (KHTML, like Gecko) Mobile/17E148"

    private val originURL = "https://www.youtube.com"

    init {
        settings.apply {
            domStorageEnabled = true
            databaseEnabled = true
            javaScriptCanOpenWindowsAutomatically = true
            setSupportMultipleWindows(true)
            userAgentString = clientUserAgent
            mediaPlaybackRequiresUserGesture = false
            builtInZoomControls = false
            javaScriptEnabled = true
            allowFileAccess = true
            setGeolocationEnabled(false)
            loadUrl()
        }
    }

    override fun onWindowVisibilityChanged(visibility: Int) {
        if (visibility != View.GONE) super.onWindowVisibilityChanged(View.VISIBLE)
    }

    private fun loadUrl() {
        val player =
            readHTMLFromUTF8File(resources.openRawResource(R.raw.youtube_iframe_player_mini))
                .replace("<VideoID>", "aC9HkZW2hZk")

        loadDataWithBaseURL(originURL, player, "text/html", "UTF-8", null)
    }


    private fun readHTMLFromUTF8File(inputStream: InputStream): String {
        inputStream.use {
            try {
                val bufferedReader = BufferedReader(InputStreamReader(inputStream, "utf-8"))
                return bufferedReader.readLines().joinToString("\n")
            } catch (e: Exception) {
                return ""
            }
        }
    }

}