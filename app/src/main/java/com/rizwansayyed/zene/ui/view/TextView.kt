package com.rizwansayyed.zene.ui.view

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.rizwansayyed.zene.R
import com.rizwansayyed.zene.ui.theme.AntroVenctraFamily

@Composable
fun TextAntroVenctra(
    v: String, center: Boolean = false, color: Color = Color.White, size: Int = 90
) {
    Text(
        v,
        if (center) Modifier.fillMaxSize() else Modifier,
        color,
        size.sp,
        FontStyle.Normal,
        fontFamily = AntroVenctraFamily,
        textAlign = if (center) TextAlign.Center else TextAlign.Start
    )
}