package com.rizwansayyed.zene.ui.player

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.rizwansayyed.zene.ui.theme.MainColor

@Composable
fun MusicPlayerView(close: () -> Unit) {
    LazyColumn(Modifier.fillMaxSize().background(MainColor)) {

    }

    BackHandler {
        close()
    }
}