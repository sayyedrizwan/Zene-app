package com.rizwansayyed.zene.presenter.ui.musicplayer.view

import android.util.Log
import androidx.annotation.OptIn
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.media3.common.util.UnstableApi
import coil.compose.AsyncImage
import com.rizwansayyed.zene.domain.MusicPlayerData
import com.rizwansayyed.zene.presenter.theme.MainColor
import com.rizwansayyed.zene.service.player.ArtistsThumbnailVideoPlayer

@Composable
fun PlayerBackgroundImage(p: MusicPlayerData?) {
    AsyncImage(
        p?.v?.thumbnail, p?.v?.songName,
        Modifier
            .fillMaxSize(),
        contentScale = ContentScale.Crop
    )

    Spacer(
        Modifier
            .fillMaxSize()
            .background(MainColor.copy(0.6f))
    )
}