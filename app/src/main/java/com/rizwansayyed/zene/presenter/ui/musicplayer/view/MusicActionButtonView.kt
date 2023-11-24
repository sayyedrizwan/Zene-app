package com.rizwansayyed.zene.presenter.ui.musicplayer.view

import android.graphics.drawable.BitmapDrawable
import androidx.compose.foundation.border
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.rizwansayyed.zene.R
import com.rizwansayyed.zene.presenter.ui.SmallIcons
import com.rizwansayyed.zene.presenter.ui.TextRegular
import com.rizwansayyed.zene.presenter.ui.TextThin

@Composable
fun MusicActionButtons() {

    Row(
        Modifier
            .fillMaxWidth()
            .horizontalScroll(rememberScrollState()), Arrangement.Center, Alignment.CenterVertically
    ) {
        repeat(5) {
            MusicActionButton(R.drawable.ic_play, "autoplay") {}
        }
    }

    // autoplay
    // loop
    // add to playlists
    // share
    // add to playlist
    //download
    // switch to video
    // switch to lyrics video
}

@Composable
fun MusicActionButton(drawable: Int, txt: String, click: () -> Unit) {
    Row(
        Modifier
            .padding(6.dp)
            .border(1.dp, Color.Gray, RoundedCornerShape(12.dp))
            .padding(horizontal = 6.dp, vertical = 3.dp),
        Arrangement.Center, Alignment.CenterVertically
    ) {

        SmallIcons(icon = drawable, size = 17)
        TextRegular(v = txt)

        Spacer(Modifier.width(6.dp))
    }
}