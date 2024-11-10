package com.rizwansayyed.zene.ui.zeneconnect

import androidx.compose.runtime.Composable
import com.rizwansayyed.zene.R
import com.rizwansayyed.zene.ui.home.view.TextSize
import com.rizwansayyed.zene.ui.home.view.TextTitleHeader

@Composable
fun ZeneConnectHomeView() {
    TextTitleHeader(Pair(TextSize.BIG, R.string.zene_connect))
}