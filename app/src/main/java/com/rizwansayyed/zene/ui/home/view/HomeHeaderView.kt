package com.rizwansayyed.zene.ui.home.view

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.rizwansayyed.zene.R
import com.rizwansayyed.zene.ui.view.ImageIcon
import com.rizwansayyed.zene.ui.view.TextAntroVenctra

@Composable
fun HomeHeaderView() {
    Spacer(Modifier.height(70.dp))

    Row(Modifier.padding(horizontal = 7.dp), Arrangement.Center) {
        Image(painterResource(R.drawable.ic_zene_logo_round), "", Modifier.size(65.dp))

        Row(Modifier.padding(top = 7.dp, start = 12.dp)) {
            TextAntroVenctra(stringResource(R.string.app_name), size = 36)
        }

        Spacer(Modifier.weight(1f))

        Row(Modifier.height(65.dp), Arrangement.Center, Alignment.CenterVertically) {
            ImageIcon(R.drawable.ic_search) {

            }

            Spacer(Modifier.width(15.dp))

            ImageIcon(R.drawable.ic_music_note_square) {

            }

            Spacer(Modifier.width(15.dp))

            ImageIcon(R.drawable.ic_setting) {

            }
        }
    }

    Spacer(Modifier.height(70.dp))
}

