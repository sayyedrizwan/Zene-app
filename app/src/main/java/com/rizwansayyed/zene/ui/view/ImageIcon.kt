package com.rizwansayyed.zene.ui.view

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp

@Composable
fun ImageIcon(id: Int, size: Int) {
    Image(painterResource(id), "", Modifier.size(size.dp))
}