package com.rizwansayyed.zene.presenter.ui

import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.rizwansayyed.zene.presenter.theme.MainColor
import com.rizwansayyed.zene.presenter.theme.antroFamily
import com.rizwansayyed.zene.presenter.theme.urbanistFamily


@Composable
fun LoadingStateBar() {
    Row(
        Modifier
            .fillMaxWidth()
            .padding(10.dp), Arrangement.Center, Alignment.CenterVertically
    ) {
        CircularProgressIndicator(
            modifier = Modifier.width(44.dp),
            color = MainColor,
            trackColor = MaterialTheme.colorScheme.secondary,
        )
    }
}


@Composable
fun TextThin(
    v: String,
    modifier: Modifier = Modifier,
    doCenter: Boolean = false,
    color: Color = Color.White,
    singleLine: Boolean = false,
    size: Int = 16
) {
    Text(
        v,
        modifier = modifier,
        color = color,
        fontFamily = urbanistFamily,
        fontWeight = FontWeight.Thin,
        maxLines = if (singleLine) 1 else 10,
        fontSize = size.scaledSp(),
        textAlign = if (doCenter) TextAlign.Center else null
    )
}


@Composable
fun TextSemiBold(
    v: String,
    modifier: Modifier = Modifier,
    doCenter: Boolean = false,
    color: Color = Color.White,
    singleLine: Boolean = false,
    size: Int = 16,
) {
    Text(
        v,
        modifier = modifier,
        color = color,
        fontFamily = urbanistFamily,
        fontWeight = FontWeight.SemiBold,
        maxLines = if (singleLine) 1 else 10,
        fontSize = size.scaledSp(),
        textAlign = if (doCenter) TextAlign.Center else null
    )
}

@Composable
fun TextBold(
    v: String,
    modifier: Modifier = Modifier,
    doCenter: Boolean = false,
    color: Color = Color.White,
    singleLine: Boolean = false,
    size: Int = 16,
) {
    Text(
        v,
        modifier = modifier,
        color = color,
        fontFamily = urbanistFamily,
        fontWeight = FontWeight.Bold,
        maxLines = if (singleLine) 1 else 10,
        fontSize = size.scaledSp(),
        textAlign = if (doCenter) TextAlign.Center else null
    )
}


@Composable
fun TextAntroSemiBold(
    v: String,
    modifier: Modifier = Modifier,
    doCenter: Boolean = false,
    color: Color = Color.White,
    singleLine: Boolean = false,
    size: Int = 16,
) {
    Text(
        v,
        modifier = modifier,
        color = color,
        fontFamily = antroFamily,
        fontWeight = FontWeight.Normal,
        maxLines = if (singleLine) 1 else 10,
        fontSize = size.scaledSp(),
        textAlign = if (doCenter) TextAlign.Center else null
    )
}


@Composable
fun TextLight(
    v: String,
    modifier: Modifier = Modifier,
    doCenter: Boolean = false,
    color: Color = Color.White,
    singleLine: Boolean = false,
    size: Int = 16,
) {
    Text(
        v,
        modifier = modifier,
        color = color,
        fontFamily = urbanistFamily,
        fontWeight = FontWeight.Light,
        maxLines = if (singleLine) 1 else 10,
        textAlign = if (doCenter) TextAlign.Center else null,
        fontSize = size.scaledSp(),
    )
}


@Composable
fun TextMedium(
    v: String,
    modifier: Modifier = Modifier,
    doCenter: Boolean = false,
    color: Color = Color.White,
    singleLine: Boolean = false,
    size: Int = 16
) {
    Text(
        v,
        modifier = modifier,
        color = color,
        fontFamily = urbanistFamily,
        fontWeight = FontWeight.Medium,
        maxLines = if (singleLine) 1 else 10,
        textAlign = if (doCenter) TextAlign.Center else null,
        fontSize = size.scaledSp(),
    )
}


@Composable
fun TextRegular(
    v: String,
    modifier: Modifier = Modifier,
    color: Color = Color.White,
    doCenter: Boolean = false,
    singleLine: Boolean = false,
    size: Int = 16
) {
    Text(
        v,
        modifier = modifier,
        color = color,
        fontFamily = urbanistFamily,
        fontWeight = FontWeight.Normal,
        maxLines = if (singleLine) 1 else 10,
        textAlign = if (doCenter) TextAlign.Center else null,
        fontSize = size.scaledSp()
    )
}


@Composable
fun TextBoldBig(
    v: String,
    modifier: Modifier = Modifier,
    doCenter: Boolean = false,
    color: Color = Color.White,
    singleLine: Boolean = false,
    size: Int = 16
) {
    Text(
        v,
        modifier = modifier,
        color = color,
        fontFamily = urbanistFamily,
        fontWeight = FontWeight.Bold,
        maxLines = if (singleLine) 1 else 10,
        fontSize = size.scaledSp(),
        textAlign = if (doCenter) TextAlign.Center else null
    )
}

@Composable
fun shimmerBrush(showShimmer: Boolean = true, targetValue: Float = 1000f): Brush {
    return if (showShimmer) {
        val shimmerColors = listOf(
            Color.LightGray.copy(alpha = 0.6f),
            Color.LightGray.copy(alpha = 0.2f),
            Color.LightGray.copy(alpha = 0.6f),
        )

        val transition = rememberInfiniteTransition(label = "")
        val translateAnimation = transition.animateFloat(
            initialValue = 0f,
            targetValue = targetValue,
            animationSpec = infiniteRepeatable(
                animation = tween(1000), repeatMode = RepeatMode.Restart
            ), label = ""
        )
        Brush.linearGradient(
            colors = shimmerColors,
            start = Offset.Zero,
            end = Offset(x = translateAnimation.value, y = translateAnimation.value)
        )
    } else {
        Brush.linearGradient(
            colors = listOf(Color.Transparent, Color.Transparent),
            start = Offset.Zero,
            end = Offset.Zero
        )
    }
}


// ---------------------- old
@Composable
fun TextSemiBoldDualLines(
    v: String,
    modifier: Modifier = Modifier,
    doCenter: Boolean = false,
    color: Color = Color.White
) {
    Text(
        v,
        modifier = modifier,
        color = color,
        fontFamily = urbanistFamily,
        fontWeight = FontWeight.SemiBold,
        maxLines = 2,
        fontSize = 15.scaledSp(),
        textAlign = if (doCenter) TextAlign.Center else null
    )
}


@Composable
fun TextSemiBoldBig(
    v: String,
    modifier: Modifier = Modifier,
    doCenter: Boolean = false,
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
        fontSize = 18.scaledSp(),
        textAlign = if (doCenter) TextAlign.Center else null
    )
}

@Composable
fun TextRegularTwoLine(
    v: String,
    color: Color = Color.White,
    doCenter: Boolean = false
) {
    Text(
        v,
        modifier = Modifier,
        color = color,
        fontFamily = urbanistFamily,
        fontWeight = FontWeight.Normal,
        maxLines = 2,
        textAlign = if (doCenter) TextAlign.Center else null,
        fontSize = 15.scaledSp(),
        lineHeight = 18.sp
    )
}

@Composable
fun TextThinBig(
    v: String,
    modifier: Modifier = Modifier,
    doCenter: Boolean = false,
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
        fontSize = 15.scaledSp(),
        textAlign = if (doCenter) TextAlign.Center else null
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
fun TopInfoWithSeeMore(v: Int, s: Int?, click: () -> Unit) {
    Spacer(Modifier.height(80.dp))

    Row(
        Modifier
            .padding(horizontal = 4.dp)
            .padding(bottom = 11.dp)
            .fillMaxWidth()
    ) {
        TextSemiBoldBig(stringResource(id = v))

        Spacer(Modifier.weight(1f))

        if (s != null) TextThin(stringResource(id = s), Modifier.clickable {
            click()
        })
    }
}

@Composable
fun TopInfoWithSeeMore(v: String, s: Int?, click: () -> Unit) {
    Spacer(Modifier.height(80.dp))

    Row(
        Modifier
            .padding(horizontal = 4.dp)
            .padding(bottom = 11.dp)
            .fillMaxWidth()
    ) {
        TextSemiBoldBig(v)

        Spacer(Modifier.weight(1f))

        if (s != null) TextThin(stringResource(id = s), Modifier.clickable {
            click()
        })
    }
}

@Composable
fun TopInfoWithImage(v: Int, s: Int?, click: () -> Unit) {

    Spacer(Modifier.height(80.dp))

    Row(
        Modifier
            .padding(horizontal = 4.dp)
            .padding(bottom = 11.dp)
            .fillMaxWidth(),
        Arrangement.Center, Alignment.CenterVertically
    ) {
        TextSemiBoldBig(stringResource(id = v))

        Spacer(Modifier.weight(1f))


        if (s != null) Image(
            painterResource(s), "",
            Modifier
                .size(24.dp)
                .clickable {
                    click()
                }, colorFilter = ColorFilter.tint(Color.White)
        )
    }
}


@Composable
fun TopInfoWithImage(v: String, s: Int?, click: () -> Unit) {

    Spacer(Modifier.height(80.dp))

    Row(
        Modifier
            .padding(horizontal = 4.dp)
            .padding(bottom = 11.dp)
            .fillMaxWidth(),
        Arrangement.Center, Alignment.CenterVertically
    ) {
        TextSemiBoldBig(v)

        Spacer(Modifier.weight(1f))


        if (s != null) Image(
            painterResource(s), "",
            Modifier
                .size(24.dp)
                .clickable {
                    click()
                }, colorFilter = ColorFilter.tint(Color.White)
        )
    }
}

@Composable
fun SongsTitleAndArtists(title: String, artists: String, modifier: Modifier, doCenter: Boolean) {
    Column {
        Spacer(Modifier.height(14.dp))
        TextSemiBold(title, modifier, doCenter = doCenter, singleLine = true)
        Spacer(Modifier.height(4.dp))
        TextThin(artists, modifier, doCenter = doCenter, singleLine = true)
        Spacer(Modifier.height(16.dp))
    }
}

@Composable
fun SongsTitleAndArtistsSmall(
    title: String, artists: String, modifier: Modifier, doCenter: Boolean
) {
    Column {
        Spacer(Modifier.height(6.dp))
        TextSemiBold(title, modifier, doCenter = doCenter, singleLine = true)
        TextThin(artists, modifier, doCenter = doCenter, singleLine = true)
        Spacer(Modifier.height(6.dp))
    }
}

@Composable
fun EmptyView() {
    Column {}
}