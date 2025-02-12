package com.rizwansayyed.zene.ui.main.search.view

import android.annotation.SuppressLint
import android.graphics.Bitmap
import android.os.SystemClock
import android.util.Log
import android.view.MotionEvent
import android.webkit.ConsoleMessage
import android.webkit.CookieManager
import android.webkit.PermissionRequest
import android.webkit.WebChromeClient
import android.webkit.WebResourceRequest
import android.webkit.WebSettings
import android.webkit.WebStorage
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.viewinterop.AndroidView
import com.rizwansayyed.zene.utils.MainUtils.toast
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlin.time.Duration.Companion.seconds


enum class SongRecognitionType {
    NONE, LOADING, LISTENING, ERROR, NO_SONG
}


@SuppressLint("SetJavaScriptEnabled")
@Composable
fun SearchSongRecognition() {
    var recType by remember { mutableStateOf(SongRecognitionType.NONE) }

    val webCClient = object : WebChromeClient() {
        override fun onPermissionRequest(request: PermissionRequest?) {
            request?.resources?.forEach {
                if (it == PermissionRequest.RESOURCE_AUDIO_CAPTURE) request.grant(request.resources)
            }
        }

        override fun onConsoleMessage(consoleMessage: ConsoleMessage?): Boolean {
            if (consoleMessage?.message() == "button clicked!") {
                recType = SongRecognitionType.LISTENING
                "listening".toast()
            } else if (consoleMessage?.message() == "button click error!") {
                recType = SongRecognitionType.ERROR
                "error".toast()
            } else if (consoleMessage?.message() == "cannot detect song!") {
                "no song".toast()
                recType = SongRecognitionType.NO_SONG
            }
            Log.d("TAG", "onConsoleMessage: data ${consoleMessage?.message()}")
            return true
        }
    }

    val webVClient = object : WebViewClient() {
        override fun shouldOverrideUrlLoading(
            view: WebView?, request: WebResourceRequest?
        ): Boolean = false

        override fun onPageStarted(view: WebView?, url: String?, favicon: Bitmap?) {
            super.onPageStarted(view, url, favicon)
            recType = SongRecognitionType.LOADING
            detectWhenNoSongFound(view)
        }

        override fun onPageFinished(view: WebView?, url: String?) {
            super.onPageFinished(view, url)
            CoroutineScope(Dispatchers.IO).launch {
                delay(2.seconds)
                clickButtonToStart(view)
            }
            detectWhenIsSongFound(view)
            CoroutineScope(Dispatchers.Main).launch {
                while (true) {
                    delay(2.seconds)
                    Log.d("TAG", "onPageFinished: the trackers infos ${view?.url}== ${view?.title}")
                }
            }
        }
    }

    AndroidView({ ctx ->
        WebView(ctx).apply {
            settings.javaScriptEnabled = true
            settings.javaScriptCanOpenWindowsAutomatically = true
            settings.domStorageEnabled = true
            webViewClient = webVClient
            setInitialScale(1)
            settings.useWideViewPort = true
            settings.loadWithOverviewMode = true
            settings.mixedContentMode = WebSettings.MIXED_CONTENT_ALWAYS_ALLOW

            settings.setSupportZoom(false)
            settings.cacheMode = WebSettings.LOAD_NO_CACHE
            webChromeClient = webCClient

            settings.userAgentString =
                "Mozilla/5.0 (Linux; Android 10; Mobile) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/117.0.0.0 Mobile Safari/537.36"

            settings.javaScriptEnabled = true
            settings.mediaPlaybackRequiresUserGesture = false
            settings.useWideViewPort = true
            settings.loadWithOverviewMode = true

            settings.domStorageEnabled = true
            settings.allowContentAccess = true

            settings.useWideViewPort = true
            settings.loadWithOverviewMode = true

            clearWebViewData(this)
            loadUrl("https://www.shazam.com/")
        }
    })
}

fun clearWebViewData(webView: WebView) {
    val cookieManager = CookieManager.getInstance()
    cookieManager.removeAllCookies(null)
    cookieManager.flush()
    WebStorage.getInstance().deleteAllData()
    webView.clearCache(true)
    webView.clearFormData()
    webView.clearHistory()
}

fun simulateTapOnWebView(webView: WebView, x: Float, y: Float) {
    webView.post {
        val now = SystemClock.uptimeMillis()

        val downEvent = MotionEvent.obtain(
            now, now, MotionEvent.ACTION_DOWN, x, y, 0
        )
        val upEvent = MotionEvent.obtain(
            now, now + 100, MotionEvent.ACTION_UP, x, y, 0
        )

        webView.dispatchTouchEvent(downEvent)
        webView.dispatchTouchEvent(upEvent)

        downEvent.recycle()
        upEvent.recycle()
    }
}

fun clickButtonToStart(view: WebView?) = CoroutineScope(Dispatchers.Main).launch {
    view?.evaluateJavascript(
        """
    (function() {
        let btn = document.querySelector('[data-test-id="home_userevent_shazamStatus"]');
        if (btn) {
            let rect = btn.getBoundingClientRect();
            return rect.left + ',' + rect.top;
        }
        return null;
    })();
""".trimIndent()
    ) { result ->
        result?.let {
            val parts = it.replace("\"", "").split(",")
            if (parts.size == 2) {
                val rawX = parts[0].toFloat()
                val rawY = parts[1].toFloat()

                val scale = view.scale
                val x = rawX * scale
                val y = rawY * scale

                simulateTapOnWebView(view, x, y)
            }
        }
    }
}

fun detectWhenNoSongFound(view: WebView?) {
    view?.evaluateJavascript(
        """
    (function() {
        let observer = new MutationObserver((mutations) => {
            mutations.forEach((mutation) => {
                if (mutation.addedNodes) {
                    mutation.addedNodes.forEach((node) => {
                        if (node.nodeType === 1) {
                            let text = node.innerText || node.textContent;
                            if (text.includes("Couldn't quite catch that")) {
                                console.log("cannot detect song!");
                            }
                        }
                    });
                }
            });
        });

        let targetNode = document.body;
        let config = { childList: true, subtree: true };
        observer.observe(targetNode, config);
    })();
    """.trimIndent(), null
    )
}

fun detectWhenIsSongFound(view: WebView?) {
    view?.evaluateJavascript(
        """
    (function() {
        let observer = new MutationObserver((mutations) => {
            mutations.forEach((mutation) => {
                if (mutation.addedNodes) {
                    mutation.addedNodes.forEach((node) => {
                        if (node.nodeType === 1) {
                            let text = node.innerText || node.textContent;
                            if (text.includes("Couldn't quite catch that")) {
                                console.log("cannot detect song!");
                            }
                        }
                    });
                }
            });
        });

        let targetNode = document.body;
        let config = { childList: true, subtree: true };
        observer.observe(targetNode, config);
    })();
    """.trimIndent(), null
    )
}
