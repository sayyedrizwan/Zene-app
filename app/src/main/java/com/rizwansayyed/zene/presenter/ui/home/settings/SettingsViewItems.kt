package com.rizwansayyed.zene.presenter.ui.home.settings

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.rizwansayyed.zene.R
import com.rizwansayyed.zene.presenter.theme.BlackColor
import com.rizwansayyed.zene.presenter.ui.SmallIcons
import com.rizwansayyed.zene.presenter.ui.TextBold
import com.rizwansayyed.zene.presenter.ui.TextRegular
import com.rizwansayyed.zene.presenter.ui.TextThin


@Composable
fun SettingsItems(text: Int, showMarker: Boolean, click: () -> Unit) {
    Row(
        Modifier
            .padding(5.dp)
            .fillMaxWidth()
            .clickable {
                click()
            }, Arrangement.Center, Alignment.CenterVertically
    ) {
        TextRegular(
            stringResource(text),
            Modifier
                .padding(vertical = 16.dp, horizontal = 14.dp)
                .weight(1f),
            size = 14
        )
        if (showMarker) SmallIcons(R.drawable.ic_tick)
    }
}

@Composable
fun SettingsItemsCard(text: Int, click: () -> Unit) {
    Row(
        Modifier
            .padding(5.dp)
            .padding(horizontal = 7.dp)
            .fillMaxWidth()
            .clip(RoundedCornerShape(14.dp))
            .background(BlackColor)
            .clickable {
                click()
            }, Arrangement.Center, Alignment.CenterVertically
    ) {
        TextRegular(
            stringResource(text),
            Modifier
                .padding(vertical = 16.dp, horizontal = 14.dp)
                .weight(1f),
            size = 14
        )

        SmallIcons(R.drawable.ic_arrow_right)

        Spacer(Modifier.width(5.dp))
    }


    Spacer(Modifier.height(35.dp))
}


@Composable
fun SettingsItemsCard(text: Int, body: String, click: () -> Unit) {

    Row(
        Modifier
            .padding(5.dp)
            .padding(horizontal = 7.dp)
            .fillMaxWidth()
            .clip(RoundedCornerShape(14.dp))
            .background(BlackColor)
            .clickable {
                click()
            }, Arrangement.Center, Alignment.CenterVertically
    ) {
        TextRegular(
            stringResource(text),
            Modifier
                .padding(vertical = 16.dp, horizontal = 14.dp)
                .weight(1f),
            size = 14
        )

        TextThin(body, Modifier.padding(end = 9.dp), size = 14)
    }


    Spacer(Modifier.height(35.dp))
}


@Composable
fun SettingsLayout(title: Int, content: @Composable ColumnScope.() -> Unit) {
    TextBold(stringResource(title), Modifier.padding(horizontal = 14.dp), size = 19)

    Spacer(Modifier.height(9.dp))

    Column(
        Modifier
            .padding(horizontal = 10.dp)
            .fillMaxWidth()
            .clip(RoundedCornerShape(14.dp))
            .background(BlackColor)
    ) {
        content()
    }


    Spacer(Modifier.height(95.dp))
}