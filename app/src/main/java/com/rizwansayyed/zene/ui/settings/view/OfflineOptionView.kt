package com.rizwansayyed.zene.ui.settings.view

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.rizwansayyed.zene.R
import com.rizwansayyed.zene.ui.theme.BlackLight
import com.rizwansayyed.zene.utils.QuickSandBold
import com.rizwansayyed.zene.utils.QuickSandRegular
import com.rizwansayyed.zene.utils.QuickSandSemiBold

@Composable
fun OfflineOptionSettings() {
    QuickSandSemiBold(
        stringResource(id = R.string.local_songs),
        size = 16,
        modifier = Modifier.padding(top = 35.dp, start = 15.dp)
    )

    Column(
        Modifier
            .padding(vertical = 15.dp, horizontal = 9.dp)
            .clip(RoundedCornerShape(14.dp))
            .background(BlackLight)
            .padding(5.dp)
    ) {
        ViewLocalSongs(stringResource(id = R.string.show_local_song_when_offline), false)

        ViewLocalSongs(stringResource(id = R.string.hide_local_songs), true)

        ViewLocalSongs(stringResource(id = R.string.show_local_songs), false)
    }
}

@Composable
fun ViewLocalSongs(txt: String, doTick: Boolean) {
    Row(
        Modifier
            .fillMaxSize()
            .clickable {

            }, Arrangement.Start, Alignment.CenterVertically
    ) {
        QuickSandRegular(
            txt,
            size = 14,
            modifier = Modifier
                .padding(15.dp)
                .weight(1f),
            align = TextAlign.Start
        )

        if (doTick) Image(
            painterResource(id = R.drawable.ic_tick),
            "",
            Modifier.size(30.dp),
            colorFilter = ColorFilter.tint(Color.White)
        )
    }
}