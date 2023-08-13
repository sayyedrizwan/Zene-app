package com.rizwansayyed.zene.ui.home.homeui

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import com.rizwansayyed.zene.ui.theme.BlackLight
import com.rizwansayyed.zene.ui.theme.Purple40
import com.rizwansayyed.zene.ui.theme.WhiteLight

@Composable
fun HomeNavBar(modifier: Modifier) {
    Row(
        modifier
            .padding(10.dp)
            .padding(bottom = 36.dp)
            .clip(shape = RoundedCornerShape(13.dp))
            .background(WhiteLight),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Image(
            imageVector = Icons.Default.Home,
            contentDescription = "",
            modifier = Modifier
                .padding(horizontal = 30.dp)
                .size(50.dp)
        )
        Image(
            imageVector = Icons.Default.Home,
            contentDescription = "",
            modifier = Modifier
                .padding(horizontal = 30.dp)
                .size(50.dp)
        )
        Image(
            imageVector = Icons.Default.Home,
            contentDescription = "",
            modifier = Modifier
                .padding(horizontal = 30.dp)
                .size(50.dp)
        )
    }
}