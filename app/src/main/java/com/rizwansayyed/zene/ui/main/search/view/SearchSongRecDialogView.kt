package com.rizwansayyed.zene.ui.main.search.view

import android.os.SystemClock
import android.view.MotionEvent
import android.webkit.WebView
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.rizwansayyed.zene.R
import com.rizwansayyed.zene.ui.view.ButtonWithBorder
import com.rizwansayyed.zene.ui.view.TextViewNormal
import com.rizwansayyed.zene.utils.URLSUtils
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.cancel
import kotlinx.coroutines.delay
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import kotlin.time.Duration.Companion.seconds

@Composable
fun SearchSongListeningTextView(
    modifier: Modifier, recType: URLSUtils.SongRecognitionType, close: () -> Unit
) {
    when (recType) {
        URLSUtils.SongRecognitionType.NONE -> {}
        URLSUtils.SongRecognitionType.LOADING -> Column(
            modifier
                .padding(bottom = 30.dp)
                .fillMaxWidth(),
            Arrangement.Center,
            Alignment.CenterHorizontally
        ) {
            TextViewNormal(stringResource(R.string.loading_), 17, center = true)
        }

        URLSUtils.SongRecognitionType.LISTENING -> Column(
            modifier
                .padding(bottom = 30.dp)
                .fillMaxWidth(),
            Arrangement.Center,
            Alignment.CenterHorizontally
        ) {
            TextViewNormal(stringResource(R.string.listening_), 17, center = true)
        }

        URLSUtils.SongRecognitionType.ERROR -> Column(
            modifier
                .padding(bottom = 30.dp)
                .fillMaxWidth(),
            Arrangement.Center,
            Alignment.CenterHorizontally
        ) {
            TextViewNormal(
                stringResource(R.string.error_recognizing_song), 17, center = true
            )
            Spacer(Modifier.height(20.dp))
            ButtonWithBorder(R.string.close) { close() }
        }

        URLSUtils.SongRecognitionType.NO_SONG -> Column(
            modifier
                .padding(bottom = 30.dp)
                .fillMaxWidth(),
            Arrangement.Center,
            Alignment.CenterHorizontally
        ) {
            TextViewNormal(
                stringResource(R.string.no_song_recognized), 17, center = true
            )
            Spacer(Modifier.height(20.dp))
            ButtonWithBorder(R.string.close) { close() }
        }
    }
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
            console.log("button clicked!");
            return rect.left + ',' + rect.top;
        }
        console.log("button click error!");
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

fun removeYoutubeTopView(view: WebView?, done: () -> Unit) {
    val js = """
         javascript:(function() { 
            var iframe = document.getElementById('player');
            if (iframe) {
                var innerDoc = iframe.contentDocument || iframe.contentWindow.document;
                var elements = innerDoc.querySelectorAll('.ytp-chrome-top, .ytp-show-cards-title, .ytp-pause-overlay, .ytp-watermark');
                elements.forEach(el => el.remove());
            }
         })();
    """.trimIndent()

    view?.evaluateJavascript(js) {
        CoroutineScope(Dispatchers.IO).launch {
            delay(700)
            done()
            if (isActive) cancel()
        }
    }

    val jsCards = """
        (function() {
            var iframe = document.getElementById('player');
            if (iframe) {
                var innerDoc = iframe.contentDocument || iframe.contentWindow.document;
                if (innerDoc) {
                    setTimeout(function() {
                        var ceElements = innerDoc.querySelectorAll('[class*="ytp-ce-element"]');
                        ceElements.forEach(el => el.remove());
                    }, 1000);
                }
            }
        })();
    """
    view?.evaluateJavascript(jsCards, null)
}