package com.rizwansayyed.zene.presenter.ui.home.artists

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.rizwansayyed.zene.presenter.ui.TextBold
import com.rizwansayyed.zene.presenter.ui.TextSemiBold
import com.rizwansayyed.zene.viewmodel.HomeNavViewModel

@Composable
fun TopNameView() {
    val homeNav: HomeNavViewModel = hiltViewModel()

    Column(Modifier.padding(horizontal = 8.dp)) {
        Spacer(Modifier.height(38.dp))
        TextSemiBold(homeNav.selectedArtists.substringBefore(" "), size = 45)
        Spacer(Modifier.height(2.dp))
        if (homeNav.selectedArtists.contains(" "))
            TextSemiBold(homeNav.selectedArtists.substringAfter(" "), size = 45)
    }
}