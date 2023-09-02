package com.rizwansayyed.zene.ui.home.homeui

import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.rizwansayyed.zene.R
import com.rizwansayyed.zene.utils.QuickSandBold

@Composable
fun TopHeaderInfo() {
    Row(
        Modifier
            .fillMaxWidth()
            .padding(vertical = 55.dp, horizontal = 20.dp),
        Arrangement.SpaceBetween,
        Alignment.CenterVertically
    ) {

        Row(verticalAlignment = Alignment.CenterVertically) {
            Image(
                painterResource(R.mipmap.r_logo), "",
                Modifier.size(35.dp)
            )

            Spacer(Modifier.width(6.dp))

            QuickSandBold(
                stringResource(id = R.string.app_name).trim(),
                size = 17
            )
        }

        Image(
            painterResource(R.drawable.ic_search), "",
            Modifier.size(20.dp),
            colorFilter = ColorFilter.tint(Color.White)
        )

    }
}