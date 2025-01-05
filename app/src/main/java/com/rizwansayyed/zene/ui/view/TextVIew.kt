package com.rizwansayyed.zene.ui.view

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.sp
import com.rizwansayyed.zene.ui.theme.proximanOverFamily

@Composable
fun TextViewNormal(
    txt: String, size: Int = 17, color: Color = Color.White, center: Boolean = false, line: Int = 20
) {
    Text(
        txt,
        if (center) Modifier.fillMaxWidth() else Modifier,
        color, size.sp, null, FontWeight.Normal, proximanOverFamily,
        textAlign = if (center) TextAlign.Center else null, maxLines = line,
        overflow = TextOverflow.Ellipsis
    )
}

@Composable
fun TextViewBold(
    txt: String, size: Int = 17, color: Color = Color.White, center: Boolean = false
) {
    Text(
        txt,
        if (center) Modifier.fillMaxWidth() else Modifier,
        color, size.sp, null, FontWeight.Bold, proximanOverFamily,
        textAlign = if (center) TextAlign.Center else null
    )
}

@Composable
fun TextViewSemiBold(
    txt: String, size: Int = 17, color: Color = Color.White, center: Boolean = false
) {
    Text(
        txt,
        if (center) Modifier.fillMaxWidth() else Modifier,
        color, size.sp, null, FontWeight.SemiBold, proximanOverFamily,
        textAlign = if (center) TextAlign.Center else null
    )
}

@Composable
fun TextViewLight(
    txt: String, size: Int = 17, color: Color = Color.White, center: Boolean = false, line: Int = 20
) {
    Text(
        txt,
        if (center) Modifier.fillMaxWidth() else Modifier,
        color, size.sp, null, FontWeight.Light, proximanOverFamily,
        textAlign = if (center) TextAlign.Center else null,
        maxLines = line, overflow = TextOverflow.Ellipsis
    )
}