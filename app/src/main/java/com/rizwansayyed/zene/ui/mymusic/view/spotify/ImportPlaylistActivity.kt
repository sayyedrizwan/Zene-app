package com.rizwansayyed.zene.ui.mymusic.view.spotify

import android.annotation.SuppressLint
import android.content.Intent
import android.content.pm.ActivityInfo
import android.net.Uri
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.rizwansayyed.zene.R
import com.rizwansayyed.zene.ui.theme.DarkCharcoal
import com.rizwansayyed.zene.ui.view.HorizontalCardItems
import com.rizwansayyed.zene.ui.view.LoadingCardView
import com.rizwansayyed.zene.ui.view.TextPoppins
import com.rizwansayyed.zene.ui.view.TextPoppinsSemiBold
import com.rizwansayyed.zene.utils.ShowAdsOnAppOpen
import com.rizwansayyed.zene.viewmodel.ZeneViewModel
import dagger.hilt.android.AndroidEntryPoint

interface PlaylistRun {
    fun spotify(token: String, path: String?)
}

@AndroidEntryPoint
class ImportPlaylistActivity : ComponentActivity(), PlaylistRun {

    companion object {
        lateinit var playlistRun: PlaylistRun
    }

    private val viewModel: ZeneViewModel by viewModels()

    @SuppressLint("SourceLockedOrientationActivity")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        playlistRun = this

        checkAndRunWeb(intent)
        setContent {
            requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT

            LazyColumn(
                Modifier
                    .fillMaxSize()
                    .background(DarkCharcoal)
            ) {
                if (viewModel.isSpotifyPlaylistsLoading) item {
                    Row(
                        Modifier
                            .fillMaxWidth()
                            .padding(top = 50.dp),
                        Arrangement.Center,
                        Alignment.Bottom
                    ) {
                        LoadingCardView()
                    }
                }

                if (!viewModel.isSpotifyPlaylistsLoading && viewModel.spotifyPlaylists.isEmpty())
                    item {
                        Spacer(Modifier.height(190.dp))
                        TextPoppins(
                            stringResource(R.string.no_playlists_found_on_spotify), true, size = 17
                        )
                        Spacer(Modifier.height(30.dp))
                    }

                if (viewModel.spotifyPlaylists.isNotEmpty()) {
                    item {
                        Spacer(Modifier.height(90.dp))

                        TextPoppinsSemiBold(stringResource(R.string.playlists), size = 15)
                    }
                    items(viewModel.spotifyPlaylists) {
                        HorizontalCardItems(it)
                    }
                }
            }
        }
    }

    override fun onStart() {
        super.onStart()
        ShowAdsOnAppOpen(this).interstitialAds()
    }

    override fun onNewIntent(intent: Intent) {
        super.onNewIntent(intent)
        checkAndRunWeb(intent)
    }

    private fun checkAndRunWeb(intent: Intent) {
        val appLinkData: Uri? = intent.data
        if (appLinkData == null) {
            finish()
            return
        }
        val accessToken = extractAccessToken(appLinkData.toString())
        if (accessToken != null) {
            viewModel.spotifyPlaylists(accessToken, null)
        }
    }

    private fun extractAccessToken(callbackUrl: String): String? {
        val uri = Uri.parse(callbackUrl)
        val fragment = uri.fragment ?: return null
        val params = fragment.split("&").associate {
            val (key, value) = it.split("=")
            key to value
        }
        return params["access_token"]
    }

    override fun spotify(token: String, path: String?) {
        viewModel.spotifyPlaylists(token, path)
    }
}
