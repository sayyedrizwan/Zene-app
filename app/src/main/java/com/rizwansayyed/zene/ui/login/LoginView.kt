package com.rizwansayyed.zene.ui.login

import android.net.Uri
import android.util.Log
import android.widget.VideoView
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.viewinterop.AndroidView
import androidx.lifecycle.Lifecycle
import com.rizwansayyed.zene.R
import com.rizwansayyed.zene.db.DataStoreManager.userInfoDB
import com.rizwansayyed.zene.ui.theme.MainColor
import com.rizwansayyed.zene.ui.view.TextAntroVenctra
import com.rizwansayyed.zene.utils.Utils.toast
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlin.time.Duration.Companion.seconds


@Composable
fun LoginView() {
    val isLogin by userInfoDB.collectAsState(initial = null)

    if (isLogin?.isLoggedIn() == false) LoginViewSpace()
}

@Composable
fun LoginViewSpace() {
    val context = LocalContext.current.applicationContext
    val lifecycleOwner = LocalLifecycleOwner.current
    val lifecycleState by lifecycleOwner.lifecycle.currentStateFlow.collectAsState()

    var videoView by remember { mutableStateOf<VideoView?>(null) }
    var visibleLoginTopView by remember { mutableStateOf(false) }

    Box(
        Modifier
            .fillMaxSize()
            .background(Color.Black)
    ) {
        AndroidView({ ctx ->
            VideoView(ctx).apply {
                videoView = this
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
            LoginZeneLogo(
                Modifier
                    .fillMaxSize()
                    .align(Alignment.Center))
        }
    }

    LaunchedEffect(lifecycleState) {
        if (lifecycleState == Lifecycle.State.RESUMED) videoView?.start()
        delay(2.seconds)
        visibleLoginTopView = true
    }
}

@Composable
fun LoginZeneLogo(modifier: Modifier = Modifier) {
    var visibleLoginButton by remember { mutableStateOf(false) }

    var nameText by remember { mutableStateOf("") }
    val name = stringResource(R.string.app_name)

    Column(
        modifier.background(MainColor.copy(0.7f)), Arrangement.Center, Alignment.CenterHorizontally
    ) {
        TextAntroVenctra(nameText)

        AnimatedVisibility(
            visibleLoginButton, Modifier, fadeIn(tween(1000)), fadeOut(tween(1000))
        ) {
            LoginButtonView()
        }
    }

    LaunchedEffect(Unit) {
        while (nameText != name) {
            delay(400)
            nameText += name.split("").filter { it.isNotEmpty() }[nameText.length]
        }

        visibleLoginButton = true
    }
}


@Composable
fun LoginButtonView(modifier: Modifier = Modifier) {
    TextAntroVenctra(stringResource(R.string.a_music_app))
}