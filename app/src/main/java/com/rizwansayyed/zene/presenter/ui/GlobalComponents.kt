package com.rizwansayyed.zene.presenter.ui

import android.content.Intent
import androidx.activity.compose.ManagedActivityResultLauncher
import androidx.activity.result.ActivityResult
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.rizwansayyed.zene.R
import com.rizwansayyed.zene.presenter.theme.BlackColor
import com.rizwansayyed.zene.presenter.theme.MainColor
import com.rizwansayyed.zene.presenter.theme.antroFamily
import com.rizwansayyed.zene.presenter.theme.urbanistFamily
import com.rizwansayyed.zene.presenter.ui.home.views.startSpeech


@Composable
fun Dp.dpToPx() = with(LocalDensity.current) { this@dpToPx.toPx() }

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
fun LoadingCircle(s: Int) {
    val strokeWidth = 5.dp

    CircularProgressIndicator(
        modifier = Modifier
            .size(s.dp)
            .drawBehind {
                drawCircle(
                    Color.White,
                    radius = size.width / 2 - strokeWidth.toPx() / 2,
                    style = Stroke(strokeWidth.toPx())
                )
            },
        color = Color.LightGray,
        strokeWidth = strokeWidth
    )
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
        maxLines = if (singleLine) 1 else Int.MAX_VALUE,
        fontSize = size.scaledSp(),
        textAlign = if (doCenter) TextAlign.Center else null,
        overflow = TextOverflow.Ellipsis
    )
}


@Composable
fun TextThinArtistsDesc(
    v: String, showFull: Boolean = false
) {
    Text(
        v,
        modifier = Modifier
            .padding(horizontal = 10.dp)
            .offset(y = (-15).dp)
            .animateContentSize(spring(Spring.DampingRatioLowBouncy, Spring.StiffnessLow)),
        color = Color.White,
        fontFamily = urbanistFamily,
        fontWeight = FontWeight.Thin,
        maxLines = if (showFull) Int.MAX_VALUE else 5,
        fontSize = 14.scaledSp(),
        textAlign = TextAlign.Start,
        overflow = TextOverflow.Ellipsis
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
        maxLines = if (singleLine) 1 else Int.MAX_VALUE,
        fontSize = size.scaledSp(),
        textAlign = if (doCenter) TextAlign.Center else null,
        overflow = TextOverflow.Ellipsis,
        style = TextStyle(lineHeight = 29.sp)
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
        maxLines = if (singleLine) 1 else Int.MAX_VALUE,
        fontSize = size.scaledSp(),
        textAlign = if (doCenter) TextAlign.Center else null,
        overflow = TextOverflow.Ellipsis
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
        maxLines = if (singleLine) 1 else Int.MAX_VALUE,
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
        maxLines = if (singleLine) 1 else Int.MAX_VALUE,
        textAlign = if (doCenter) TextAlign.Center else null,
        fontSize = size.scaledSp(),
        overflow = TextOverflow.Ellipsis
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
        maxLines = if (singleLine) 1 else Int.MAX_VALUE,
        textAlign = if (doCenter) TextAlign.Center else null,
        fontSize = size.scaledSp(),
        overflow = TextOverflow.Ellipsis
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
        maxLines = if (singleLine) 1 else Int.MAX_VALUE,
        textAlign = if (doCenter) TextAlign.Center else null,
        fontSize = size.scaledSp(),
        overflow = TextOverflow.Ellipsis
    )
}


@Composable
fun TextRegularNews(v: String) {
    Text(
        v,
        modifier = Modifier.fillMaxWidth(),
        color = Color.White,
        fontFamily = urbanistFamily,
        fontWeight = FontWeight.Normal,
        maxLines = 2,
        textAlign = TextAlign.Start,
        fontSize = 14.scaledSp(),
        overflow = TextOverflow.Ellipsis
    )
}


@Composable
fun shimmerBrush(): Brush {
    val shimmerColors = listOf(
        Color.LightGray.copy(alpha = 0.6f),
        Color.LightGray.copy(alpha = 0.2f),
        Color.LightGray.copy(alpha = 0.6f),
    )
    val transition = rememberInfiniteTransition(label = "")
    val translateAnim by transition.animateFloat(
        initialValue = 0f,
        targetValue = 2300f,
        animationSpec = infiniteRepeatable(
            tween(durationMillis = 1200, easing = FastOutSlowInEasing),
            RepeatMode.Restart
        ), label = ""
    )

    return Brush.linearGradient(
        colors = shimmerColors,
        start = Offset(10f, 10f),
        end = Offset(translateAnim, translateAnim)
    )
}

@Composable
fun MenuIcon(modifier: Modifier = Modifier, click: () -> Unit) {
    Image(
        painterResource(R.drawable.ic_menu), "",
        modifier
            .size(25.dp)
            .clickable {
                click()
            },
        colorFilter = ColorFilter.tint(Color.White)
    )
}

@Composable
fun SearchEditTextView(
    p: String,
    text: String,
    listener: ManagedActivityResultLauncher<Intent, ActivityResult>?,
    onChange: (String) -> Unit,
    onDone: () -> Unit
) {
    OutlinedTextField(
        value = text,
        onValueChange = { onChange(it) },
        modifier = Modifier
            .fillMaxWidth()
            .background(BlackColor, CircleShape),
        keyboardOptions = KeyboardOptions(
            autoCorrect = false,
            keyboardType = KeyboardType.Text,
            imeAction = ImeAction.Search
        ),
        shape = CircleShape,
        placeholder = {
            TextThin(p, color = Color.LightGray)
        },
        maxLines = 1,
        singleLine = true,
        colors = OutlinedTextFieldDefaults.colors(
            focusedBorderColor = MainColor,
            unfocusedBorderColor = BlackColor
        ),
        keyboardActions = KeyboardActions(onSearch = {
            onDone()
        }),
        textStyle = TextStyle(
            fontSize = 17.sp,
            fontFamily = urbanistFamily,
            fontWeight = FontWeight.SemiBold,
        ),
        trailingIcon = {
            if (listener != null)
                SmallIcons(R.drawable.ic_mic) {
                    listener.launch(startSpeech())
                }
        }
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
fun SmallIcons(icon: Int, size: Int = 23, p: Int = 5, click: () -> Unit) {
    Image(
        painterResource(icon), "",
        Modifier
            .padding(p.dp)
            .size(size.dp)
            .clickable {
                click()
            },
        colorFilter = ColorFilter.tint(Color.White)
    )
}

@Composable
fun SmallIcons(icon: Int, size: Int = 23, p: Int = 5) {
    Image(
        painterResource(icon), "",
        Modifier
            .padding(p.dp)
            .size(size.dp),
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
        TextSemiBold(stringResource(id = v), size = 18)

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
        TextSemiBold(v, size = 18)

        Spacer(Modifier.weight(1f))

        if (s != null) TextThin(stringResource(id = s), Modifier.clickable {
            click()
        })
    }
}
