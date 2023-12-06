package com.rizwansayyed.zene.presenter.ui.home.mymusic

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.rizwansayyed.zene.R
import com.rizwansayyed.zene.presenter.ui.TopInfoWithSeeMore

@Composable
fun HistoryItemsList() {
    Column(Modifier.padding(horizontal = 9.dp), Arrangement.Center) {
        TopInfoWithSeeMore(R.string.history, R.string.view_all) {

        }
    }
}
