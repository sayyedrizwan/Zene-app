package com.rizwansayyed.zene.ui.view

import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.sp
import com.rizwansayyed.zene.ui.theme.AntroVenctraFamily
import com.rizwansayyed.zene.ui.theme.PoppinsFamily

@Composable
fun TextAntroVenctra(
    v: String, center: Boolean = false, color: Color = Color.White, size: Int = 94
) {
    Text(
        v,
        if (center) Modifier.fillMaxWidth() else Modifier,
        color,
        size.sp,
        FontStyle.Normal, FontWeight.Normal, AntroVenctraFamily,
        textAlign = if (center) TextAlign.Center else TextAlign.Start
    )
}

@Composable
fun TextPoppinsSemiBold(
    v: String, center: Boolean = false, color: Color = Color.White, size: Int = 20
) {
    Text(
        v,
        if (center) Modifier.fillMaxWidth() else Modifier,
        color,
        size.sp,
        FontStyle.Normal, FontWeight.SemiBold, PoppinsFamily,
        textAlign = if (center) TextAlign.Center else TextAlign.Start,
        overflow = TextOverflow.Ellipsis
    )
}


@Composable
fun TextPoppins(
    v: String, center: Boolean = false, color: Color = Color.White, size: Int = 20,
    limit: Int? = null, lineHeight : Int = 50,
) {
    Text(
        v,
        if (center) Modifier.fillMaxWidth().animateContentSize() else Modifier.animateContentSize(),
        color,
        size.sp,
        FontStyle.Normal, FontWeight.Normal, PoppinsFamily,
        textAlign = if (center) TextAlign.Center else TextAlign.Start,
        maxLines = limit ?: 200,
        overflow = TextOverflow.Ellipsis,
        lineHeight = lineHeight.sp
    )
}

@Composable
fun TextPoppinsThin(
    v: String, center: Boolean = false, color: Color = Color.White, size: Int = 20,
    limit: Int? = null
) {
    Text(
        v,
        if (center) Modifier.animateContentSize().fillMaxWidth() else Modifier.animateContentSize(),
        color,
        size.sp,
        FontStyle.Normal, FontWeight.Thin, PoppinsFamily,
        textAlign = if (center) TextAlign.Center else TextAlign.Start,
        maxLines = limit ?: 200,
        overflow = TextOverflow.Ellipsis
    )
}

@Composable
fun TextPoppinsLight(
    v: String, center: Boolean = false, color: Color = Color.White, size: Int = 20
) {
    Text(
        v,
        if (center) Modifier.fillMaxWidth() else Modifier,
        color,
        size.sp,
        FontStyle.Normal, FontWeight.ExtraLight, PoppinsFamily,
        textAlign = if (center) TextAlign.Center else TextAlign.Start,
        overflow = TextOverflow.Ellipsis
    )
}