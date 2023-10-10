package com.rizwansayyed.zene.presenter.ui.home

import androidx.compose.foundation.Image
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.rizwansayyed.zene.R
import com.rizwansayyed.zene.presenter.ui.SmallIcons
import com.rizwansayyed.zene.presenter.ui.TextBold
import com.rizwansayyed.zene.presenter.ui.TextBoldBig
import com.rizwansayyed.zene.presenter.ui.TextMedium
import com.rizwansayyed.zene.presenter.ui.TextRegular
import com.rizwansayyed.zene.presenter.ui.TextSemiBold

@Composable
fun HomepageTopView() {
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
                Modifier.size(46.dp)
            )

            Spacer(Modifier.width(9.dp))

            TextBoldBig(stringResource(id = R.string.app_name).trim())
        }

        Row {

            SmallIcons(R.drawable.ic_notification) {

            }

            Spacer(Modifier.width(9.dp))

            SmallIcons(R.drawable.ic_setting) {

            }
        }
    }
}