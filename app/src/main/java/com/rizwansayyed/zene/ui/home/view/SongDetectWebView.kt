package com.rizwansayyed.zene.ui.home.view

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Color
import android.webkit.PermissionRequest
import android.webkit.WebChromeClient
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.compose.runtime.MutableState
import com.rizwansayyed.zene.utils.Utils
import com.rizwansayyed.zene.utils.Utils.enable
import com.rizwansayyed.zene.utils.Utils.toast
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.cancel
import kotlinx.coroutines.delay
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kotlin.time.Duration.Companion.seconds

@SuppressLint("ViewConstructor")
class SongDetectWebView(
    context: Context,
    private val notFound: () -> Unit,
    doneLoading: () -> Unit,
    private val name: (String) -> Unit
) : WebView(context) {

    private val webViewClientObj = object : WebViewClient() {
        override fun onPageFinished(view: WebView?, url: String?) {
            super.onPageFinished(view, url)
            CoroutineScope(Dispatchers.IO).launch {
                delay(2.seconds)
                doneLoading()

                withContext(Dispatchers.Main) {
                    view?.evaluateJavascript(
                        "document.querySelector('.FloatingShazamButton_buttonContainer__DZGwL').click();",
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

    private val isSongDataAvailable = """javascript:(function() {
              var element = document.getElementsByClassName('TrackPageHeader_data__RxygT TrackPageHeader_grid__EPCjO');
              return element.length;
            })()"""

    private val isSongNotFound = """javascript:(function() {
            var element = document.getElementsByClassName('FloatingShazamButton_calloutContainer__ERGTc');
            return element.length;
            })()"""

    private var getTitle: String = "javascript:(function() { return document.title; })()"

    init {
        enable()
        clearCache(true)
        setBackgroundColor(Color.TRANSPARENT)
        webViewClient = webViewClientObj
        webChromeClient = webViewChromeClientObj

        loadUrl("https://www.shazam.com/")
    }

    fun checkFunctions() = CoroutineScope(Dispatchers.Main).launch {
        evaluateJavascript(isSongDataAvailable) {
            if (it.toInt() > 0) evaluateJavascript(getTitle) {
                val nameTitle = it.substringBefore(": Song Lyrics")
                name(nameTitle)
            }
        }

        evaluateJavascript(isSongNotFound) {
            if (it.toInt() > 0) notFound()
        }

        if (isActive) cancel()
    }

    fun destroyView() {
        clearCache(true)
        loadUrl("about:blank")
        destroy()
    }

    fun setDesktopMode(webView: WebView, enabled: Boolean) {
        var newUserAgent: String? = webView.settings.userAgentString
        if (enabled) {
            try {
                val ua: String = webView.settings.userAgentString
                val androidOSString: String = webView.settings.userAgentString.substring(
                    ua.indexOf("("),
                    ua.indexOf(")") + 1
                )
                newUserAgent =
                    webView.settings.userAgentString.replace(androidOSString, "(X11; Linux x86_64)")
            } catch (e: Exception) {
                e.printStackTrace()
            }
        } else {
            newUserAgent = null
        }
        webView.settings.apply {
            userAgentString = newUserAgent
            useWideViewPort = enabled
            loadWithOverviewMode = enabled
        }
    }
}