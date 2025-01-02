package com.rizwansayyed.zene.ui.view

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.rizwansayyed.zene.ui.theme.MainColor

@Composable
fun CircularLoadingView() {
    Row(
        Modifier
            .padding(20.dp)
            .fillMaxWidth(),
        Arrangement.Center, Alignment.CenterVertically
    ) {
        CircularProgressIndicator(Modifier.width(35.dp), Color.White, trackColor = MainColor)
    }
}