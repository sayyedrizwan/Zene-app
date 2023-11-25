package com.rizwansayyed.zene.presenter.ui.musicplayer.view.playerwebview.helper

import android.annotation.SuppressLint
import android.content.Context
import android.view.View
import android.view.ViewGroup
import android.webkit.WebChromeClient
import android.webkit.WebSettings
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.annotation.VisibleForTesting
import com.rizwansayyed.zene.R
import com.rizwansayyed.zene.presenter.theme.MainColor
import java.io.BufferedReader
import java.io.InputStream
import java.io.InputStreamReader

@SuppressLint("SetJavaScriptEnabled")
class PlayerWebView(context: Context, videoId: String) : WebView(context) {

    private val layoutParamSize =
        LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT)

    private val clientUserAgent =
        "Mozilla/5.0 (iPhone; CPU iPhone OS 17_0 like Mac OS X) AppleWebKit/605.1.15 (KHTML, like Gecko) Mobile/17E148"

    private val originURL = "https://www.youtube.com"

    init {
        layoutParams = layoutParamSize
        webViewClient = WebViewClient()
        webChromeClient = WebChromeClient()
        isFocusable = true
        isFocusableInTouchMode = true
        setInitialScale(1)
        setBackgroundColor(MainColor.value.toInt())

        settings.apply {
            settings.cacheMode = WebSettings.LOAD_NO_CACHE
            settings.domStorageEnabled = true
            settings.databaseEnabled = true
            scrollBarStyle = View.SCROLLBARS_INSIDE_OVERLAY
            settings.javaScriptCanOpenWindowsAutomatically = false
            settings.setSupportMultipleWindows(true)
            settings.userAgentString = clientUserAgent
            settings.builtInZoomControls = false
            settings.javaScriptEnabled = true
            settings.allowFileAccess = true
            settings.databaseEnabled = true
            settings.domStorageEnabled = true
            settings.setGeolocationEnabled(false)
        }
        loadUrl()
    }

    fun loadUrl() {
        val player = readHTMLFromUTF8File(resources.openRawResource(R.raw.youtube_iframe_player))
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