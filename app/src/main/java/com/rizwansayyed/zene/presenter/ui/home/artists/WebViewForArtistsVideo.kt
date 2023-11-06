package com.rizwansayyed.zene.presenter.ui.home.artists

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Bitmap
import android.view.View
import android.webkit.JavascriptInterface
import android.webkit.WebChromeClient
import android.webkit.WebResourceError
import android.webkit.WebResourceRequest
import android.webkit.WebSettings
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner
import com.rizwansayyed.zene.presenter.ui.home.artists.WebViewForArtistsVideo.RunFunctions.getDownloadLink
import com.rizwansayyed.zene.presenter.ui.home.artists.WebViewForArtistsVideo.RunFunctions.setInput
import com.rizwansayyed.zene.presenter.ui.home.artists.WebViewForArtistsVideo.RunFunctions.startLoading
import com.rizwansayyed.zene.presenter.util.UiUtils.toast
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.cancel
import kotlinx.coroutines.delay
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext
import kotlin.time.Duration.Companion.seconds

@SuppressLint("SetJavaScriptEnabled")
class WebViewForArtistsVideo(context: Context) : WebView(context) {

    companion object {
        const val SAVE_FROM_BASE_URL = "https://en.savefrom.net/"
    }

    var theCaptureVideoUrl = ""

    private val webViewChromeListener = object : WebChromeClient() {
        override fun onProgressChanged(view: WebView?, newProgress: Int) {
            super.onProgressChanged(view, newProgress)
            if (newProgress != 100) return
            view?.evaluateJavascript(setInput, null)
            view?.evaluateJavascript(startLoading, null)


            CoroutineScope(Dispatchers.Main).launch {
                while (theCaptureVideoUrl.isEmpty()) {
                    delay(2.seconds)
                    view?.evaluateJavascript(getDownloadLink, null)
                }
                if (isActive) cancel()
            }
        }
    }

    init {
        webViewClient = WebViewClient()
        webChromeClient = webViewChromeListener
        settings.loadsImagesAutomatically = true
        settings.javaScriptEnabled = true
        settings.allowFileAccess = true
        settings.javaScriptCanOpenWindowsAutomatically = true
        settings.pluginState = WebSettings.PluginState.ON
        settings.mediaPlaybackRequiresUserGesture = false
        settings.domStorageEnabled = true
        settings.cacheMode = WebSettings.LOAD_NO_CACHE
        requestFocus(View.FOCUS_DOWN)
        addJavascriptInterface(this@WebViewForArtistsVideo, "ZeneWebView")

        loadUrl(SAVE_FROM_BASE_URL)
    }

    @JavascriptInterface
    fun vUrl(value: String) {
        theCaptureVideoUrl = value
    }


    object RunFunctions {
        const val setInput =
            "javascript:document.getElementById('sf_url').value = 'https://www.youtube.com/watch?v=5domUjBEsU8'"
        const val startLoading = "javascript:document.getElementById('sf_submit').click()"

        private const val classNameOfDownloadTag =
            "link link-download subname ga_track_events download-icon"

        val getDownloadLink = """
            (function() { 
                let elements = document.querySelectorAll('[class="$classNameOfDownloadTag"]');
                
                if (elements.length > 0) {
                    let url = document.querySelector('a.${
            classNameOfDownloadTag.replace(
                " ",
                "."
            )
        }').getAttribute('href');
                    ZeneWebView.vUrl(url);
                }
            })()
        """
    }


}