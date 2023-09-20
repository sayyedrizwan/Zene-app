package com.rizwansayyed.zene.presenter.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.rizwansayyed.zene.R
import com.rizwansayyed.zene.presenter.theme.urbanistFamily
import com.rizwansayyed.zene.presenter.util.UiUtils.toast

@Composable
fun TextSemiBold(
    v: String,
    modifier: Modifier = Modifier,
    color: Color = Color.White,
    singleLine: Boolean = false
) {
    Text(
        v,
        modifier = modifier,
        color = color,
        fontFamily = urbanistFamily,
        fontWeight = FontWeight.SemiBold,
        maxLines = if (singleLine) 1 else 10,
        fontSize = 18.scaledSp()
    )
}


@Composable
fun TextSemiBoldBig(
    v: String,
    modifier: Modifier = Modifier,
    color: Color = Color.White,
    singleLine: Boolean = false
) {
    Text(
        v,
        modifier = modifier,
        color = color,
        fontFamily = urbanistFamily,
        fontWeight = FontWeight.SemiBold,
        maxLines = if (singleLine) 1 else 10,
        fontSize = 20.scaledSp()
    )
}


@Composable
fun TextBoldBig(
    v: String,
    modifier: Modifier = Modifier,
    color: Color = Color.White,
    singleLine: Boolean = false
) {
    Text(
        v,
        modifier = modifier,
        color = color,
        fontFamily = urbanistFamily,
        fontWeight = FontWeight.Bold,
        maxLines = if (singleLine) 1 else 10,
        fontSize = 20.scaledSp()
    )
}

@Composable
fun TextBold(
    v: String,
    modifier: Modifier = Modifier,
    color: Color = Color.White,
    singleLine: Boolean = false
) {
    Text(
        v,
        modifier = modifier,
        color = color,
        fontFamily = urbanistFamily,
        fontWeight = FontWeight.Bold,
        maxLines = if (singleLine) 1 else 10,
        fontSize = 18.scaledSp()
    )
}


@Composable
fun TextLight(
    v: String,
    modifier: Modifier = Modifier,
    color: Color = Color.White,
    singleLine: Boolean = false
) {
    Text(
        v,
        modifier = modifier,
        color = color,
        fontFamily = urbanistFamily,
        fontWeight = FontWeight.Light,
        maxLines = if (singleLine) 1 else 10,
    )
}

@Composable
fun TextMedium(
    v: String,
    modifier: Modifier = Modifier,
    color: Color = Color.White,
    singleLine: Boolean = false
) {
    Text(
        v,
        modifier = modifier,
        color = color,
        fontFamily = urbanistFamily,
        fontWeight = FontWeight.Medium,
        maxLines = if (singleLine) 1 else 10,
    )
}

@Composable
fun TextRegular(
    v: String,
    modifier: Modifier = Modifier,
    color: Color = Color.White,
    singleLine: Boolean = false
) {
    Text(
        v,
        modifier = modifier,
        color = color,
        fontFamily = urbanistFamily,
        fontWeight = FontWeight.Normal,
        maxLines = if (singleLine) 1 else 10,
    )
}

@Composable
fun TextThin(
    v: String,
    modifier: Modifier = Modifier,
    color: Color = Color.White,
    singleLine: Boolean = false
) {
    Text(
        v,
        modifier = modifier,
        color = color,
        fontFamily = urbanistFamily,
        fontWeight = FontWeight.Thin,
        maxLines = if (singleLine) 1 else 10,
        fontSize = 15.scaledSp()
    )
}

@Composable
fun Int.scaledSp(): TextUnit {
    val value: Int = this
    return with(LocalDensity.current) {
        val fontScale = this.fontScale
        val textSize = value / fontScale
        textSize.sp
    }
}

@Composable
fun SmallIcons(icon: Int, click: () -> Unit) {
    Image(
        painterResource(icon), "",
        Modifier
            .padding(5.dp)
            .size(23.dp)
            .clickable {
                click()
            },
        colorFilter = ColorFilter.tint(Color.White)
    )
}

@Composable
fun TopInfoWithSeeMore(v: String) {
    Row(
        Modifier
            .padding(horizontal = 4.dp)
            .fillMaxWidth()
    ) {
        TextSemiBold(v)

        Spacer(Modifier.weight(1f))

        TextThin(stringResource(id = R.string.see_all), Modifier.clickable {
            "recentplay see all".toast()
        })
    }

}