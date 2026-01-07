package com.rizwansayyed.zene.ui.view

import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.em
import androidx.compose.ui.unit.sp
import com.rizwansayyed.zene.ui.theme.proximanOverFamily

@Composable
fun TextViewNormal(
    txt: String, size: Int = 17, color: Color = Color.White, center: Boolean = false, line: Int = 20
) {
    Text(
        text = txt,
        if (center) Modifier
            .fillMaxWidth()
            .animateContentSize() else Modifier.animateContentSize(),
        color = color,
        fontSize = size.sp,
        fontWeight = FontWeight.Normal,
        fontFamily = proximanOverFamily,
        textAlign = if (center) TextAlign.Center else null,
        maxLines = line,
        overflow = TextOverflow.Ellipsis
    )
}

@Composable
fun TextViewBoldBig(
    txt: String, size: Int = 17, color: Color = Color.White, center: Boolean = false, line: Int = 20
) {
    Text(
        text = txt,
        modifier = if (center) Modifier
            .fillMaxWidth()
            .animateContentSize() else Modifier.animateContentSize(),
        color = color,
        fontSize = size.sp,
        fontWeight = FontWeight.Bold,
        fontFamily = proximanOverFamily,
        textAlign = if (center) TextAlign.Center else null,
        maxLines = line,
        lineHeight = 43.sp
    )
}

@Composable
fun TextViewBold(
    txt: String, size: Int = 17, color: Color = Color.White, center: Boolean = false, line: Int = 20
) {
    Text(
        text = txt,
        modifier = if (center) Modifier
            .fillMaxWidth()
            .animateContentSize() else Modifier.animateContentSize(),
        color = color,
        fontSize = size.sp,
        fontWeight = FontWeight.Bold,
        fontFamily = proximanOverFamily,
        textAlign = if (center) TextAlign.Center else null,
        maxLines = line,
        overflow = TextOverflow.Ellipsis
    )
}

@Composable
fun TextViewSemiBold(
    txt: String, size: Int = 17, color: Color = Color.White, center: Boolean = false, line: Int = 20
) {
    Text(
        text = txt,
        modifier = if (center) Modifier.fillMaxWidth() else Modifier,
        color = color,
        fontSize = size.sp,
        fontWeight = FontWeight.SemiBold,
        fontFamily = proximanOverFamily,
        textAlign = if (center) TextAlign.Center else null,
        maxLines = line
    )
}

@Composable
fun TextViewLight(
    txt: String, size: Int = 17, color: Color = Color.White, center: Boolean = false, line: Int = 20
) {
    Text(
        text = txt,
        modifier = if (center) Modifier.fillMaxWidth() else Modifier,
        color = color,
        fontSize = size.sp,
        fontWeight = FontWeight.Light,
        fontFamily = proximanOverFamily,
        textAlign = if (center) TextAlign.Center else null,
        maxLines = line,
        overflow = TextOverflow.Ellipsis,
        lineHeight = 1.4.em
    )
}

@Composable
fun TextViewBorder(text: String, color: Color = Color.Black, click: () -> Unit) {
    Row(
        Modifier
            .padding(4.dp)
            .clip(RoundedCornerShape(80))
            .background(color)
            .clickable { click() }
            .padding(horizontal = 15.dp, vertical = 9.dp),
    ) {
        TextViewSemiBold(text, size = 14)
    }
}