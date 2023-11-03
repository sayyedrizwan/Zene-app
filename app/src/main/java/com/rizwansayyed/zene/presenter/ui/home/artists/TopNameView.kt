package com.rizwansayyed.zene.presenter.ui.home.artists

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.rizwansayyed.zene.presenter.ui.TextAntroSemiBold
import com.rizwansayyed.zene.presenter.ui.TextBold
import com.rizwansayyed.zene.presenter.ui.TextSemiBold
import com.rizwansayyed.zene.viewmodel.HomeNavViewModel

@Composable
fun TopNameView() {
    val homeNav: HomeNavViewModel = hiltViewModel()

    Column(Modifier.padding(horizontal = 13.dp)) {
        Spacer(Modifier.height(58.dp))
        TextAntroSemiBold(homeNav.selectedArtists.substringBefore(" "), size = 70)

        if (homeNav.selectedArtists.contains(" "))
            TextAntroSemiBold(
                homeNav.selectedArtists.substringAfter(" "),
                Modifier.offset(y = (-15).dp),
                size = 70
            )
    }
}