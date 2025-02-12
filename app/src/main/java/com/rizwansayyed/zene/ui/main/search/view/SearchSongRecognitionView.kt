package com.rizwansayyed.zene.ui.main.search.view

import android.annotation.SuppressLint
import android.graphics.Bitmap
import android.util.Log
import android.webkit.ConsoleMessage
import android.webkit.PermissionRequest
import android.webkit.WebChromeClient
import android.webkit.WebSettings
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.viewinterop.AndroidView


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
            } else if (consoleMessage?.message() == "button click error!") {
                recType = SongRecognitionType.ERROR
            } else if (consoleMessage?.message() == "cannot detect song!") {
                recType = SongRecognitionType.NO_SONG
            }
            Log.d("TAG", "onConsoleMessage: data ${consoleMessage?.message()}")
            return true
        }
    }

    val webVClient = object : WebViewClient() {
        override fun onPageStarted(view: WebView?, url: String?, favicon: Bitmap?) {
            super.onPageStarted(view, url, favicon)
            recType = SongRecognitionType.LOADING
            detectWhenNoSongFound(view)
        }

        override fun onPageFinished(view: WebView?, url: String?) {
            super.onPageFinished(view, url)
            clickButtonToStart(view)
            detectWhenIsSongFound(view)
        }
    }

    AndroidView({ ctx ->
        WebView(ctx).apply {
            settings.javaScriptEnabled = true
            settings.javaScriptCanOpenWindowsAutomatically = true
            settings.domStorageEnabled = true
            webViewClient = webVClient

            settings.useWideViewPort = true
            settings.loadWithOverviewMode = true
            settings.mixedContentMode = WebSettings.MIXED_CONTENT_ALWAYS_ALLOW

            settings.setSupportZoom(false)
            settings.cacheMode = WebSettings.LOAD_NO_CACHE
            webChromeClient = webCClient

            settings.userAgentString =
                "Mozilla/5.0 (Linux; Android 12; Pixel 5) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/110.0.0.0 Mobile Safari/537.36"

            loadUrl("https://www.shazam.com/")
        }
    })
}

fun clickButtonToStart(view: WebView?) {
    view?.evaluateJavascript(
        """
                setTimeout(function() {
                    var btn = document.querySelector('div[data-test-id="home_impression_shazamButton"] div[data-test-id="home_userevent_shazamStatus"]');
                    if (btn) {
                        btn.click();
                        console.log("button clicked!");
                    } else {
                        console.log("button click error!");
                    }
                }, 1000);
                """.trimIndent(), null
    )
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