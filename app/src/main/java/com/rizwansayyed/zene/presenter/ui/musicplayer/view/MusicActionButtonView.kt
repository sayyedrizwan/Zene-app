package com.rizwansayyed.zene.presenter.ui.musicplayer.view

import android.graphics.drawable.BitmapDrawable
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
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
        MusicActionButton(R.drawable.ic_flim_video, R.string.switch_to_video) {}
        MusicActionButton(R.drawable.ic_closed_caption, R.string.switch_to_lyrics_video) {}
        MusicActionButton(R.drawable.ic_repeat, R.string.enable_loop) {}
        MusicActionButton(R.drawable.ic_autoplay, R.string.enable_autoplay) {}
        MusicActionButton(R.drawable.ic_share, R.string.share) {}
        MusicActionButton(R.drawable.ic_playlist, R.string.add_to_playlist) {}
        MusicActionButton(R.drawable.ic_download, R.string.offline_download) {}

    }

    // autoplay
    // loop
    // add to playlists
    // share
    // download
    // switch to video
    // switch to lyrics video
}

@Composable
fun MusicActionButton(drawable: Int, txt: Int, click: () -> Unit) {
    Row(
        Modifier
            .padding(6.dp)
            .border(1.dp, Color.Gray, RoundedCornerShape(12.dp))
            .clickable {
                click()
            }
            .padding(horizontal = 6.dp, vertical = 3.dp),
        Arrangement.Center, Alignment.CenterVertically
    ) {

        SmallIcons(icon = drawable, size = 17)
        TextRegular(v = stringResource(txt))

        Spacer(Modifier.width(6.dp))
    }
}