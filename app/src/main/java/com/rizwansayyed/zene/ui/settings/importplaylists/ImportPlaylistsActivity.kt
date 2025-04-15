package com.rizwansayyed.zene.ui.settings.importplaylists

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.fragment.app.FragmentActivity
import com.rizwansayyed.zene.ui.settings.importplaylists.view.ImportMusicInstructions
import com.rizwansayyed.zene.ui.settings.importplaylists.view.ImportSongSyncView
import com.rizwansayyed.zene.ui.theme.ZeneTheme
import dagger.hilt.android.AndroidEntryPoint
import java.io.File

@AndroidEntryPoint
class ImportPlaylistsActivity : FragmentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            ZeneTheme {
                var selectFiled by remember { mutableStateOf<File?>(null) }
                Box(
                    Modifier
                        .fillMaxSize()
                        .background(Color.Black), Alignment.Center
                ) {
                    if (selectFiled == null) ImportMusicInstructions {
                        selectFiled = it
                    }
                    else
                        ImportSongSyncView(selectFiled!!) {
                            selectFiled = null
                        }
                }
            }
        }
    }

}