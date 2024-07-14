package com.rizwansayyed.zene.ui.view

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.rizwansayyed.zene.R


@Composable
fun ImageIcon(id: Int, click: () -> Unit) {
    Image(
        painterResource(id), "",
        Modifier
            .size(30.dp)
            .clickable { click() },
        colorFilter = ColorFilter.tint(Color.White)
    )
}