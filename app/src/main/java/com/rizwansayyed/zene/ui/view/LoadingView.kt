package com.rizwansayyed.zene.ui.view

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.rizwansayyed.zene.ui.theme.MainColor

@Composable
fun LoadingView(modifier: Modifier = Modifier) {
    Column(modifier, Arrangement.Center, Alignment.CenterHorizontally) {
        CircularProgressIndicator(
            Modifier.size(32.dp), MainColor, trackColor = Color.White,
        )
    }
}