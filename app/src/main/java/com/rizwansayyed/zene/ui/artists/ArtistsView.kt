package com.rizwansayyed.zene.ui.artists

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.rizwansayyed.zene.R
import com.rizwansayyed.zene.ui.theme.DarkCharcoal
import com.rizwansayyed.zene.ui.view.ImageIcon
import com.rizwansayyed.zene.viewmodel.ZeneViewModel

@Composable
fun ArtistsView(viewModel: ZeneViewModel, id: String?, close: () -> Unit) {
    LazyColumn(
        Modifier
            .fillMaxSize()
            .background(DarkCharcoal)
    ) {
        item(1) {
            Row(Modifier.padding(top = 50.dp, start = 8.dp, bottom = 25.dp)) {
                ImageIcon(R.drawable.ic_arrow_left, close)

                Spacer(Modifier.weight(1f))
            }
        }
    }

    LaunchedEffect(Unit) {
        if (id == null) close()
        else viewModel.artistsInfo(id)
    }

    BackHandler {
        close()
    }

}