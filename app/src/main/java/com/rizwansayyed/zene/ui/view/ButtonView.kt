package com.rizwansayyed.zene.ui.view

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@Composable
fun ButtonWithImageAndBorder(border: Color = Color.White) {
    Row(
        modifier = Modifier
            .padding(horizontal = 10.dp)
            .fillMaxSize()
            .border(1.dp, border, RoundedCornerShape(14.dp)),
        Arrangement.Center, Alignment.CenterVertically
    ) {

    }
}