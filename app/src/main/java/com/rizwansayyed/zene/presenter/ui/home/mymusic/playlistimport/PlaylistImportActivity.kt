package com.rizwansayyed.zene.presenter.ui.home.mymusic.playlistimport

import android.content.Intent
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.Box
import androidx.compose.runtime.LaunchedEffect
import com.rizwansayyed.zene.presenter.theme.ZeneTheme
import com.rizwansayyed.zene.presenter.ui.home.mymusic.playlistimport.PlaylistImportersType.*
import com.rizwansayyed.zene.presenter.util.UiUtils.transparentStatusAndNavigation
import com.rizwansayyed.zene.viewmodel.HomeNavViewModel
import com.rizwansayyed.zene.viewmodel.PlaylistImportViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class PlaylistImportActivity : ComponentActivity() {

    private val playlistImportViewModel: PlaylistImportViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        transparentStatusAndNavigation()
        super.onCreate(savedInstanceState)

        val type = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            intent.getSerializableExtra(Intent.EXTRA_TEXT, PlaylistImportersType::class.java)
        } else {
            intent.getSerializableExtra(Intent.EXTRA_TEXT) as PlaylistImportersType
        } ?: return

        setContent {
            ZeneTheme {
                Box {



                    LaunchedEffect(Unit) {
                        when (type) {
                            SPOTIFY -> playlistImportViewModel.spotifyPlaylistInfo()
                            YOUTUBE_MUSIC -> {}
                            APPLE_MUSIC -> {}
                        }
                    }
                }
            }
        }
    }
}
