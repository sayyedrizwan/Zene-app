package com.rizwansayyed.zene.ui.home.view

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Color
import android.net.Uri
import android.util.Log
import android.webkit.PermissionRequest
import android.webkit.WebChromeClient
import android.webkit.WebResourceRequest
import android.webkit.WebView
import android.webkit.WebViewClient
import com.rizwansayyed.zene.utils.Utils.URLS.USER_AGENT_D
import com.rizwansayyed.zene.utils.Utils.URLS.YOUTUBE_MUSIC
import com.rizwansayyed.zene.utils.Utils.enable
import com.rizwansayyed.zene.utils.Utils.toast
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.cancel
import kotlinx.coroutines.delay
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import okhttp3.OkHttpClient
import okhttp3.Request
import org.jsoup.Jsoup
import java.util.concurrent.TimeUnit
import java.util.regex.Pattern
import kotlin.time.Duration.Companion.seconds


@SuppressLint("ViewConstructor")
class SongDetectWebView(
    context: Context,
    private val notFound: () -> Unit,
    doneLoading: () -> Unit,
    private val name: (String) -> Unit
) : WebView(context) {

    private val baseURL = "https://www.aha-music.com/"
    private var isRedirectNewPage = false

    private val webViewClientObj = object : WebViewClient() {
        override fun shouldOverrideUrlLoading(
            view: WebView?,
            request: WebResourceRequest?
        ): Boolean {
            if (!isRedirectNewPage && request != null) {
                isRedirectNewPage = true
                destroyView()
                readTheSongDetails(request.url)
            }
            return true
        }

        override fun onPageFinished(view: WebView?, url: String?) {
            super.onPageFinished(view, url)
            CoroutineScope(Dispatchers.IO).launch {
                delay(2.seconds)
                doneLoading()

                withContext(Dispatchers.Main) {
                    view?.evaluateJavascript(
                        "document.querySelector('.circleBase.grid.place-items-center').click();",
                        null
                    )
                }
            }
        }
    }

    private val webViewChromeClientObj = object : WebChromeClient() {
        override fun onPermissionRequest(r: PermissionRequest?) {
            r?.grant(r.resources)
        }
    }


    private val isSongButtonAvailableC = """javascript:(function() {
               return document.querySelectorAll('button.detail-btn').length
            })()"""

    private val isSongNotFound = """javascript:(function() {
            const element = document.querySelector('h4[data-v-122e4e3c].text-xl.font-semibold');

            if (element) {
                const style = window.getComputedStyle(element);
                const isVisible = style.display !== 'none' && style.visibility !== 'hidden' && style.opacity !== '0';
        
                const containsText = element.textContent.trim() === "Couldn’t quite catch that.";
                
                return isVisible && containsText;
            }
        
            return false;
            })()"""

    init {
        enable()
        clearCache(true)
        setBackgroundColor(Color.TRANSPARENT)
        webViewClient = webViewClientObj
        webChromeClient = webViewChromeClientObj
        setInitialScale(0)
        loadUrl(baseURL)
    }

    fun checkFunctions() = CoroutineScope(Dispatchers.Main).launch {
        evaluateJavascript(isSongButtonAvailableC) {
            if (it == "1") loadUrl("javascript:document.querySelector('button.detail-btn').click();")
        }

        evaluateJavascript(isSongNotFound) {
            if (it.toBoolean()) notFound()
        }

        if (isActive) cancel()
    }

    fun destroyView() = CoroutineScope(Dispatchers.Main).launch {
        clearCache(true)
        loadUrl("about:blank")
        destroy()
    }

    fun readTheSongDetails(url: Uri) = CoroutineScope(Dispatchers.IO).launch {
        try {
            val document = Jsoup.connect(url.toString()).get()
            val title = document.title()
            name(title.substringBefore("| AHA Music").trim())
        } catch (e: Exception) {
            notFound()
        }
    }
}