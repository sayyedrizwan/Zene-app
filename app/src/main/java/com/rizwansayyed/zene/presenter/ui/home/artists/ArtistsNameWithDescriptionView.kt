package com.rizwansayyed.zene.presenter.ui.home.artists

import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.rizwansayyed.zene.R
import com.rizwansayyed.zene.data.DataResponse
import com.rizwansayyed.zene.presenter.ui.TextSemiBold
import com.rizwansayyed.zene.presenter.ui.TextThin
import com.rizwansayyed.zene.viewmodel.ArtistsViewModel
import com.rizwansayyed.zene.viewmodel.HomeNavViewModel

@Composable
fun ArtistsNameWithDescription() {
    val homeNav: HomeNavViewModel = hiltViewModel()
    val artists: ArtistsViewModel = hiltViewModel()

    TextSemiBold(
        homeNav.selectedArtists,
        Modifier
            .padding(start = 6.dp)
            .offset(y = (-45).dp),
        size = 34
    )

    when (val v = artists.artistsDesc) {
        DataResponse.Empty -> {}
        is DataResponse.Error -> {}
        DataResponse.Loading -> {}
        is DataResponse.Success ->
            TextThin(
                v.item.trim(),
                Modifier
                    .padding(horizontal = 10.dp)
                    .offset(y = (-15).dp),
                size = 14
            )
    }
}