package com.rizwansayyed.zene.ui.home.settings

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.rizwansayyed.zene.R
import com.rizwansayyed.zene.presenter.SongsViewModel
import com.rizwansayyed.zene.ui.home.homeui.TopHeaderOf

@Composable
fun SettingsView(songsViewModel: SongsViewModel = hiltViewModel()) {
    Column {
        repeat(30){
            Spacer(modifier = Modifier.height(40.dp))
            TopHeaderOf(stringResource(id = R.string.recently_played))
        }
    }
}