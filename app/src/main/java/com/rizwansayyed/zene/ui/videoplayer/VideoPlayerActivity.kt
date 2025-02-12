package com.rizwansayyed.zene.ui.videoplayer

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.rizwansayyed.zene.ui.theme.ZeneTheme
import com.rizwansayyed.zene.utils.MainUtils.toast
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class VideoPlayerActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            val videoID = intent.getStringExtra(Intent.ACTION_VIEW)
            videoID?.toast()
            ZeneTheme {

            }
        }
    }
}