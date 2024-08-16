package com.rizwansayyed.zene.ui.lockscreen


import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import com.rizwansayyed.zene.data.db.DataStoreManager.musicPlayerDB
import com.rizwansayyed.zene.ui.player.MusicPlayerView
import com.rizwansayyed.zene.viewmodel.MusicPlayerViewModel
import dagger.hilt.android.AndroidEntryPoint
import com.rizwansayyed.zene.utils.FirebaseLogEvents
import com.rizwansayyed.zene.utils.FirebaseLogEvents.logEvents

@AndroidEntryPoint
class MusicPlayerActivity : ComponentActivity() {

    private val musicPlayerViewModel: MusicPlayerViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            val playerInfo by musicPlayerDB.collectAsState(initial = null)

            MusicPlayerView(playerInfo, musicPlayerViewModel, true) {
                finishAffinity()
            }

            LaunchedEffect(Unit) {
                logEvents(FirebaseLogEvents.FirebaseEvents.SONG_PLAYING_VIEW_ON_LOCK_SCREEN)
            }
        }
    }

}
