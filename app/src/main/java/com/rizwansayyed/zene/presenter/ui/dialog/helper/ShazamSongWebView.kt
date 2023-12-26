package com.rizwansayyed.zene.presenter.ui.dialog.helper

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.util.Log
import android.webkit.ConsoleMessage
import android.webkit.CookieManager
import android.webkit.JavascriptInterface
import android.webkit.PermissionRequest
import android.webkit.WebChromeClient
import android.webkit.WebView
import android.webkit.WebViewClient
import com.rizwansayyed.zene.data.utils.USER_AGENT
import com.rizwansayyed.zene.presenter.theme.MainColor
import com.rizwansayyed.zene.presenter.util.UiUtils.toast
import com.rizwansayyed.zene.service.player.utils.Utils
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlin.time.Duration.Companion.seconds

@SuppressLint("ViewConstructor", "SetJavaScriptEnabled")
class ShazamSongWebView(context: Context) : WebView(context) {

    private val shazamBaseUrl = "https://www.shazam.com/home"
    private val shazamEmptyBaseUrl = "https://shazam.com"
    private var buttonClickJob: Job? = null


    private val startListeningScript = """
        const cancelBtnElement = document.querySelector('.main-text.main-text-listening');
        if (cancelBtnElement == null) {
             document.querySelector('.shazam-button').click();
        }
        
        
        const isDone = document.querySelector('.no-match-container.animate-in.hidden')
        if (cancelBtnElement == null) {
            window.WebInterface.notFound();
        }
        
      
        const isCompleted = document.querySelector('.page-hd-v2.preview-avail.variant-b')
        if (isCompleted != null) {
            window.WebInterface.foundSound('name', 'artist');
        }
      """.trimIndent()

    private val chromeClient = object : WebChromeClient() {
        override fun onPermissionRequest(request: PermissionRequest?) {
            request?.grant(request.resources)
        }

        override fun onConsoleMessage(consoleMessage: ConsoleMessage?): Boolean {
            super.onConsoleMessage(consoleMessage)

            Log.d("TAG", "onConsoleMessage: on console 11 ${consoleMessage?.message()} ")
            return true
        }
    }


    init {
        webViewClient = WebViewClient()
        webChromeClient = chromeClient
        isFocusable = true
        isFocusableInTouchMode = true
        setBackgroundColor(MainColor.value.toInt())
        addJavascriptInterface(WebViewInterface(), "WebInterface")

        settings.apply {
            domStorageEnabled = true
            databaseEnabled = true
            javaScriptCanOpenWindowsAutomatically = true
            setSupportMultipleWindows(true)
            userAgentString =
                "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_15_7) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/120.0.0.0 Safari/537.36"
            mediaPlaybackRequiresUserGesture = false
            builtInZoomControls = false
            javaScriptEnabled = true
            allowFileAccess = true
            databaseEnabled = true
            domStorageEnabled = true
        }

        clearCache(true)
        clearHistory()

        val cookieManager = CookieManager.getInstance();
        cookieManager.removeSessionCookies(null)
        cookieManager.removeAllCookies {  }
        cookieManager.setCookie(shazamEmptyBaseUrl, "")

        loadUrl(shazamBaseUrl)

        startListening()
    }

    private fun startListening() {
        buttonClickJob = CoroutineScope(Dispatchers.Main).launch {
            while (true) {
                evaluateJavascript("(function() { $startListeningScript })();", null)
                delay(1.seconds)
            }
        }

    }

    inner class WebViewInterface {
        @JavascriptInterface
        fun notFound() {
            "not found".toast()
        }

        @JavascriptInterface
        fun foundSound(name:String, artist:String) {
            "$name $artist".toast()
        }

    }
}