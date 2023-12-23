package com.rizwansayyed.zene.presenter.ui.ringtone.view

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.rizwansayyed.zene.domain.MusicPlayerData
import com.rizwansayyed.zene.presenter.ui.TextSemiBold

@Composable
fun RingtoneEditView(p: MusicPlayerData?) {
    val width = (LocalConfiguration.current.screenWidthDp / 1.2).dp
    Column(Modifier.fillMaxSize()) {
        Column(Modifier.weight(3f), Arrangement.Center, Alignment.CenterHorizontally) {
            Spacer(Modifier.height(50.dp))

            AsyncImage(
                p?.v?.thumbnail, p?.v?.songName,
                Modifier
                    .padding(10.dp)
                    .clip(RoundedCornerShape(12.dp))
                    .size(width)
            )

            Spacer(Modifier.height(10.dp))

            TextSemiBold(p?.v?.songName ?: "", Modifier.fillMaxWidth(), doCenter = true)
        }

        Column(Modifier.weight(2f), Arrangement.Center, Alignment.CenterHorizontally) {
            RingtoneVocalView()
        }
    }
}