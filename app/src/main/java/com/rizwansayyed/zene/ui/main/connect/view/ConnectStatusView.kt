package com.rizwansayyed.zene.ui.main.connect.view

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.rizwansayyed.zene.R
import com.rizwansayyed.zene.ui.theme.MainColor
import com.rizwansayyed.zene.ui.view.TextViewBold

@Composable
fun ConnectStatusView() {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .heightIn(
                max = (0.95f * androidx.compose.ui.platform.LocalConfiguration.current.screenHeightDp).dp
            )
    ) {
        LazyColumn(
            Modifier
                .fillMaxSize()
                .background(MainColor)
        ) {
            item {
                Box(Modifier.padding(horizontal = 9.dp)) {
                    TextViewBold(stringResource(R.string.songs_trending_near_you), 18)
                }
                Spacer(Modifier.height(12.dp))
            }

        }
    }
}