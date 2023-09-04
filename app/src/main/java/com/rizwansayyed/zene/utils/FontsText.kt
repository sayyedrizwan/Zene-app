package com.rizwansayyed.zene.utils

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.sp
import com.rizwansayyed.zene.R

val quicksandFamily = FontFamily(
    Font(R.font.quicksand_light, FontWeight.Light),
    Font(R.font.quicksand_regular, FontWeight.Normal),
    Font(R.font.quicksand_medium, FontWeight.Medium),
    Font(R.font.quicksand_semi_bold, FontWeight.SemiBold),
    Font(R.font.quicksand_bold, FontWeight.Bold)
)

val Int.nonScaledSp
    @Composable
    get() = (this / LocalDensity.current.fontScale).sp

@Composable
fun QuickSandBold(
    v: String,
    modifier: Modifier = Modifier,
    color: Color = Color.White,
    size: Int = 20,
    maxLine: Int = Int.MAX_VALUE,
    align: TextAlign = TextAlign.Center
) {
    Text(
        text = v,
        fontFamily = quicksandFamily,
        fontWeight = FontWeight.Bold,
        color = color,
        fontSize = size.nonScaledSp,
        modifier = modifier,
        textAlign = align,
        maxLines = maxLine
    )
}

@Composable
fun QuickSandRegular(
    v: String,
    modifier: Modifier = Modifier,
    color: Color = Color.White,
    size: Int = 20,
    align: TextAlign = TextAlign.Center
) {
    Text(
        text = v,
        fontFamily = quicksandFamily,
        fontWeight = FontWeight.Normal,
        color = color,
        fontSize = size.nonScaledSp,
        modifier = modifier,
        textAlign = align
    )
}

@Composable
fun QuickSandRegular(
    v: String,
    modifier: Modifier = Modifier,
    color: Color = Color.White,
    size: Int = 20,
    maxLine: Int = Int.MAX_VALUE,
    align: TextAlign = TextAlign.Center
) {
    Text(
        text = v,
        fontFamily = quicksandFamily,
        fontWeight = FontWeight.Normal,
        color = color,
        fontSize = size.nonScaledSp,
        modifier = modifier,
        textAlign = align,
        maxLines = maxLine
    )
}


@Composable
fun QuickSandSemiBold(
    v: String,
    modifier: Modifier = Modifier,
    color: Color = Color.White,
    size: Int = 20,
    maxLine: Int = Int.MAX_VALUE,
    align: TextAlign? = null
) {
    Text(
        text = v,
        fontFamily = quicksandFamily,
        fontWeight = FontWeight.SemiBold,
        color = color,
        fontSize = size.nonScaledSp,
        modifier = modifier,
        maxLines = maxLine,
        textAlign = align
    )
}

@Composable
fun QuickSandLight(
    v: String,
    modifier: Modifier = Modifier,
    color: Color = Color.White,
    size: Int = 20,
    maxLine: Int = Int.MAX_VALUE,
    align: TextAlign = TextAlign.Center
) {
    Text(
        text = v,
        fontFamily = quicksandFamily,
        fontWeight = FontWeight.Light,
        color = color,
        fontSize = size.nonScaledSp,
        modifier = modifier,
        textAlign = align,
        maxLines = maxLine
    )
}

@Composable
fun QuickSandLight(
    v: String,
    modifier: Modifier = Modifier,
    color: Color = Color.White,
    size: Int = 20
) {
    Text(
        text = v,
        fontFamily = quicksandFamily,
        fontWeight = FontWeight.Light,
        color = color,
        fontSize = size.nonScaledSp,
        modifier = modifier,
        textAlign = TextAlign.Center
    )
}

@Composable
fun QuickSandLightUnderline(
    v: String,
    modifier: Modifier = Modifier,
    color: Color = Color.White,
    size: Int = 20
) {
    Text(
        text = v,
        fontFamily = quicksandFamily,
        fontWeight = FontWeight.Light,
        color = color,
        fontSize = size.nonScaledSp,
        modifier = modifier,
        textAlign = TextAlign.Center,
        style = TextStyle(textDecoration = TextDecoration.Underline)
    )
}