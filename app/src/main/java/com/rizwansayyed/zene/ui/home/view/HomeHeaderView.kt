package com.rizwansayyed.zene.ui.home.view

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.rizwansayyed.zene.R
import com.rizwansayyed.zene.ui.theme.GoldColor
import com.rizwansayyed.zene.ui.view.ImageIcon
import com.rizwansayyed.zene.ui.view.JoinPremiumCommunity
import com.rizwansayyed.zene.ui.view.TextAntroVenctra
import com.rizwansayyed.zene.ui.view.TextPoppins
import com.rizwansayyed.zene.utils.NavigationUtils.NAV_MY_MUSIC
import com.rizwansayyed.zene.utils.NavigationUtils.NAV_SEARCH
import com.rizwansayyed.zene.utils.NavigationUtils.NAV_SETTINGS
import com.rizwansayyed.zene.utils.NavigationUtils.sendNavCommand

@Composable
fun HomeHeaderView() {
    Spacer(Modifier.height(70.dp))

    Row(Modifier.padding(horizontal = 7.dp), Arrangement.Center) {
        Column {
            Row {
                Image(painterResource(R.mipmap.logor), "", Modifier.size(65.dp))

                Row(Modifier.padding(top = 7.dp, start = 12.dp)) {
                    TextAntroVenctra(stringResource(R.string.app_name), size = 36)
                }
            }
            JoinPremiumCommunity()
        }
        Spacer(Modifier.weight(1f))

        Row(Modifier.height(65.dp), Arrangement.Center, Alignment.CenterVertically) {
            ImageIcon(R.drawable.ic_search) {
                sendNavCommand(NAV_SEARCH)
            }

            Spacer(Modifier.width(15.dp))

            ImageIcon(R.drawable.ic_music_note_square) {
                sendNavCommand(NAV_MY_MUSIC)
            }

            Spacer(Modifier.width(15.dp))

            ImageIcon(R.drawable.ic_setting) {
                sendNavCommand(NAV_SETTINGS)
            }
        }
    }

    Spacer(Modifier.height(70.dp))
}

