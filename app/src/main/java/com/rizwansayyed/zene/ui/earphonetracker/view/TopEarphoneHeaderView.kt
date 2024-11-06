package com.rizwansayyed.zene.ui.earphonetracker.view

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.rizwansayyed.zene.R
import com.rizwansayyed.zene.ui.home.view.TextSize
import com.rizwansayyed.zene.ui.home.view.TextTitleHeader
import com.rizwansayyed.zene.ui.view.TextPoppins

@Composable
fun TopEarphoneHeaderView() {
    Spacer(Modifier.height(60.dp))
    Column(Modifier.padding(start = 5.dp)) {
        TextPoppins(stringResource(R.string.earphones_tracker_finder_), size = 40, lineHeight = 43)
        TextPoppins(stringResource(R.string.earphones_tracker_finder_desc), size = 14)
    }
}

@Composable
fun NoHeadphoneAddedView() {
    Spacer(Modifier.height(100.dp))
    TextPoppins(stringResource(R.string.no_devices_added), true, size = 16)
    Spacer(Modifier.height(100.dp))
}