package com.rizwansayyed.zene.ui.settings.importplaylists

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.fragment.app.FragmentActivity
import com.rizwansayyed.zene.ui.settings.importplaylists.view.ImportMusicInstructions
import com.rizwansayyed.zene.ui.settings.importplaylists.view.ImportPlaylistDialogView
import com.rizwansayyed.zene.ui.settings.importplaylists.view.ImportSongSyncView
import com.rizwansayyed.zene.ui.theme.ZeneTheme
import com.rizwansayyed.zene.viewmodel.ImportPlaylistViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ImportPlaylistsActivity : FragmentActivity() {

    private val viewModel: ImportPlaylistViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            ZeneTheme {
                Box(
                    Modifier
                        .fillMaxSize()
                        .background(Color.Black), Alignment.Center
                ) {
                    if (viewModel.selectedFile == null) ImportMusicInstructions(viewModel)
                    else ImportSongSyncView(viewModel)
                }


                if (viewModel.selectDialogTitle != null) ImportPlaylistDialogView(viewModel)
            }
        }
    }

}