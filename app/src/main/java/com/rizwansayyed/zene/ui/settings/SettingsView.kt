package com.rizwansayyed.zene.ui.settings

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.rizwansayyed.zene.R
import com.rizwansayyed.zene.presenter.SongsViewModel
import com.rizwansayyed.zene.ui.home.homeui.TopHeaderOf
import com.rizwansayyed.zene.ui.settings.view.OfflineOptionSettings
import com.rizwansayyed.zene.utils.QuickSandBold
import com.rizwansayyed.zene.utils.QuickSandLight
import com.rizwansayyed.zene.utils.QuickSandSemiBold

@Composable
fun SettingsView(songsViewModel: SongsViewModel = hiltViewModel()) {
    Column(
        Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
    ) {
        Spacer(modifier = Modifier.height(60.dp))

        QuickSandBold(
            stringResource(id = R.string.settings),
            size = 28,
            modifier = Modifier.padding(15.dp)
        )
        OfflineOptionSettings()
    }
}