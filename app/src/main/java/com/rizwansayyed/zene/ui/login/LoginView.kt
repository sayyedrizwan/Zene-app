package com.rizwansayyed.zene.ui.login

import android.net.Uri
import android.widget.VideoView
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.viewinterop.AndroidView
import com.rizwansayyed.zene.R
import com.rizwansayyed.zene.db.DataStoreManager.userInfoDB
import kotlinx.coroutines.delay
import kotlin.time.Duration.Companion.seconds


@Composable
fun LoginView(modifier: Modifier = Modifier) {
    val isLogin by userInfoDB.collectAsState(initial = null)

    if (isLogin?.isLoggedIn() == false) LoginViewSpace()
}

@Composable
fun LoginViewSpace() {
    val context = LocalContext.current.applicationContext
    var visibleLoginTopView by remember { mutableStateOf(false) }

    Box(
        Modifier
            .fillMaxSize()
            .background(Color.Black)
    ) {
        AndroidView({ ctx ->
            VideoView(ctx).apply {
                val uriPath = "android.resource://${context.packageName}/${R.raw.bg_login}"
                val uri = Uri.parse(uriPath)
                setVideoURI(uri)
                start()
                setOnPreparedListener { mp ->
                    mp.isLooping = true
                    mp.setVolume(0f, 0f)
                }
            }
        }, Modifier.fillMaxSize())

        AnimatedVisibility(
            visibleLoginTopView, Modifier.fillMaxSize(), fadeIn(tween(1000)), fadeOut(tween(1000))
        ) {
            LoginTopView(
                Modifier
                    .fillMaxSize()
                    .align(Alignment.Center)
            )
        }
    }

    LaunchedEffect(Unit) {
        delay(2.seconds)
        visibleLoginTopView = true
    }
}

@Composable
fun LoginTopView(modifier: Modifier = Modifier) {
    Column(modifier.background(Color.White.copy(0.3f))) {

    }
}