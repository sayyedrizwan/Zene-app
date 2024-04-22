package com.rizwansayyed.zene.presenter.ui.home.mymusic

import android.content.Intent
import android.view.LayoutInflater
import android.webkit.WebView
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.rizwansayyed.zene.R
import com.rizwansayyed.zene.di.ApplicationModule.Companion.context
import com.rizwansayyed.zene.presenter.theme.MainColor
import com.rizwansayyed.zene.presenter.ui.SmallIcons
import com.rizwansayyed.zene.presenter.ui.TextSemiBold
import com.rizwansayyed.zene.presenter.ui.home.mymusic.helper.MusicWebsitesLoginWebView
import com.rizwansayyed.zene.presenter.ui.home.mymusic.playlistimport.PlaylistImportActivity
import com.rizwansayyed.zene.presenter.ui.home.mymusic.playlistimport.PlaylistImportersType
import com.rizwansayyed.zene.presenter.util.UiUtils.toast
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.cancel
import kotlinx.coroutines.delay
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import kotlin.time.Duration.Companion.seconds

fun startPlaylistImportActivity(type: PlaylistImportersType) {
    Intent(context, PlaylistImportActivity::class.java).apply {
        flags = Intent.FLAG_ACTIVITY_NEW_TASK
        putExtra(Intent.EXTRA_TEXT, type)
        context.startActivity(this)
    }
}

@Composable
fun ImportPlaylistSpotify(open: () -> Unit) {
    ImportPlaylistButton(R.drawable.ic_spotify, R.string.import_playlist_from_spotify, open)
}

@Composable
fun SpotifyLoginDialog(close: () -> Unit) {
    val height = LocalConfiguration.current.screenHeightDp / 1.2
    val login = stringResource(id = R.string.login_to_your_spotify_account)

    Dialog(close, DialogProperties(usePlatformDefaultWidth = false)) {
        Card(
            Modifier
                .padding(5.dp)
                .fillMaxWidth()
                .height(height.dp), RoundedCornerShape(16.dp), CardDefaults.cardColors(MainColor)
        ) {
            AndroidView(factory = { ctx ->
                val view = LayoutInflater.from(ctx).inflate(R.layout.web_view_ui, null, false)
                val webView: WebView = view.findViewById(R.id.web_view)
                MusicWebsitesLoginWebView {
                    CoroutineScope(Dispatchers.IO).launch {
                        delay(1.seconds)
                        startPlaylistImportActivity(PlaylistImportersType.SPOTIFY)
                        if (isActive) cancel()
                    }
                    close()
                }.init(webView).spotify()
                view
            }, Modifier.fillMaxSize())

        }
    }

    LaunchedEffect(Unit) {
        login.toast()
    }
}


@Composable
fun ImportPlaylistYoutubeMusic(open: () -> Unit) {
    ImportPlaylistButton(
        R.drawable.ic_youtube_music, R.string.import_playlist_from_youtube_music, open
    )
}


@Composable
fun YoutubeMusicLoginDialog(close: () -> Unit) {
    val height = LocalConfiguration.current.screenHeightDp / 1.2
    val login = stringResource(id = R.string.login_to_your_yt_music_account)

    Dialog(close, DialogProperties(usePlatformDefaultWidth = false)) {
        Card(
            Modifier
                .padding(5.dp)
                .fillMaxWidth()
                .height(height.dp), RoundedCornerShape(16.dp), CardDefaults.cardColors(MainColor)
        ) {
            AndroidView(factory = { ctx ->
                val view = LayoutInflater.from(ctx).inflate(R.layout.web_view_ui, null, false)
                val webView: WebView = view.findViewById(R.id.web_view)
                MusicWebsitesLoginWebView {
                    CoroutineScope(Dispatchers.IO).launch {
                        delay(1.seconds)
                        startPlaylistImportActivity(PlaylistImportersType.YOUTUBE_MUSIC)
                        if (isActive) cancel()
                    }
                    close()
                }.init(webView).ytMusic()
                view
            }, Modifier.fillMaxSize())

        }
    }

    LaunchedEffect(Unit) {
        login.toast()
    }
}


@Composable
fun ImportPlaylistAppleMusic(open: () -> Unit) {
    ImportPlaylistButton(
        R.drawable.ic_apple_music, R.string.import_playlist_from_apple_music, open
    )
}


@Composable
fun AppleMusicLoginDialog(close: () -> Unit) {
    val height = LocalConfiguration.current.screenHeightDp / 1.2
    val login = stringResource(id = R.string.login_to_your_apple_music_account)

    Dialog(close, DialogProperties(usePlatformDefaultWidth = false)) {
        Card(
            Modifier
                .padding(5.dp)
                .fillMaxWidth()
                .height(height.dp), RoundedCornerShape(16.dp), CardDefaults.cardColors(MainColor)
        ) {
            AndroidView(factory = { ctx ->
                val view = LayoutInflater.from(ctx).inflate(R.layout.web_view_ui, null, false)
                val webView: WebView = view.findViewById(R.id.web_view)
                MusicWebsitesLoginWebView {
                    CoroutineScope(Dispatchers.IO).launch {
                        delay(1.seconds)
                        startPlaylistImportActivity(PlaylistImportersType.APPLE_MUSIC)
                        if (isActive) cancel()
                    }
                    close()
                }.init(webView).appleMusicMusic()
                view
            }, Modifier.fillMaxSize())

        }
    }

    LaunchedEffect(Unit) {
        login.toast()
    }
}


@Composable
fun ImportPlaylistButton(icon: Int, txt: Int, click: () -> Unit) {
    Column(
        Modifier
            .padding(8.dp)
            .fillMaxWidth()
            .clickable { click() }
            .clip(RoundedCornerShape(12.dp))
            .background(MainColor), Arrangement.Center, Alignment.CenterHorizontally
    ) {
        Spacer(Modifier.height(30.dp))

        Image(
            painterResource(icon), "",
            Modifier.size(45.dp)
        )

        Spacer(Modifier.height(20.dp))

        TextSemiBold(v = stringResource(txt), Modifier.fillMaxWidth(), true)

        Spacer(Modifier.height(10.dp))

        Box(Modifier.rotate(180f)) {
            SmallIcons(icon = R.drawable.ic_arrow_up_right, 20, 9)
        }
        Spacer(Modifier.height(30.dp))
    }
}

