package com.rizwansayyed.zene.presenter.ui.home.albums

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.rizwansayyed.zene.domain.MusicData
import com.rizwansayyed.zene.presenter.ui.MenuIcon
import com.rizwansayyed.zene.presenter.ui.TextRegular
import com.rizwansayyed.zene.presenter.ui.TextThin

@Composable
fun AlbumsSongsItems(music: MusicData, menu: () -> Unit, play: () -> Unit) {
    Row(
        Modifier
            .padding(horizontal = 9.dp)
            .padding(bottom = 9.dp)
            .fillMaxWidth().clickable {
                play()
            },
        Arrangement.Center, Alignment.CenterVertically
    ) {
        Spacer(Modifier.width(5.dp))

        AsyncImage(music.thumbnail, music.name, Modifier.size(85.dp))

        Spacer(Modifier.width(7.dp))

        TextThin(music.name ?: "", Modifier.weight(1f), singleLine = true)

        Spacer(Modifier.width(5.dp))

        MenuIcon {
            menu()
        }

        Spacer(Modifier.width(3.dp))
    }
}