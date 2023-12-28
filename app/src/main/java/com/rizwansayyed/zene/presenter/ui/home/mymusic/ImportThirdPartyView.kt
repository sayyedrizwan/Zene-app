package com.rizwansayyed.zene.presenter.ui.home.mymusic

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.rizwansayyed.zene.R
import com.rizwansayyed.zene.presenter.theme.MainColor
import com.rizwansayyed.zene.presenter.ui.SmallIcons
import com.rizwansayyed.zene.presenter.ui.TextSemiBold

@Composable
fun ImportPlaylistSpotify() {
    ImportPlaylistButton(R.drawable.ic_spotify, R.string.import_playlist_from_spotify) {

    }
}


@Composable
fun ImportPlaylistYoutubeMusic() {
    ImportPlaylistButton(R.drawable.ic_youtube_music, R.string.import_playlist_from_youtube_music) {

    }
}


@Composable
fun ImportPlaylistButton(icon: Int, txt: Int, click: () -> Unit) {
    Column(
        Modifier
            .padding(8.dp)
            .fillMaxWidth()
            .clickable { click() }
            .clip(RoundedCornerShape(12.dp))
            .background(MainColor), Arrangement.Center, Alignment.CenterHorizontally
    ) {
        Spacer(Modifier.height(30.dp))

        Image(
            painterResource(icon), "",
            Modifier.size(45.dp)
        )

        Spacer(Modifier.height(20.dp))

        TextSemiBold(v = stringResource(txt), Modifier.fillMaxWidth(), true)

        Spacer(Modifier.height(10.dp))

        Box(Modifier.rotate(180f)) {
            SmallIcons(icon = R.drawable.ic_arrow_up_right, 20, 9)
        }
        Spacer(Modifier.height(30.dp))
    }
}

