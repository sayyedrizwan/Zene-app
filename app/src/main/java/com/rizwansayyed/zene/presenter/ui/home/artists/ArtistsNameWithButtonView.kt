package com.rizwansayyed.zene.presenter.ui.home.artists

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.rizwansayyed.zene.R
import com.rizwansayyed.zene.data.DataResponse
import com.rizwansayyed.zene.presenter.theme.MainColor
import com.rizwansayyed.zene.presenter.ui.LoadingCircle
import com.rizwansayyed.zene.presenter.ui.SmallIcons
import com.rizwansayyed.zene.presenter.ui.TextRegular
import com.rizwansayyed.zene.presenter.ui.TextSemiBold
import com.rizwansayyed.zene.presenter.ui.TextThin
import com.rizwansayyed.zene.presenter.ui.shimmerBrush
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
        DataResponse.Loading -> repeat(9) {
            Spacer(
                Modifier
                    .padding(horizontal = 10.dp, vertical = 8.dp)
                    .fillMaxWidth()
                    .height(11.dp)
                    .background(shimmerBrush())
            )
        }

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

@Composable
fun ArtistsButtonView() {
    val artists: ArtistsViewModel = hiltViewModel()
    val homeNav: HomeNavViewModel = hiltViewModel()

    Row(Modifier.fillMaxWidth(), Arrangement.Center, Alignment.CenterVertically) {
        Row(
            Modifier
                .padding(10.dp)
                .clickable {}
                .weight(1f)
                .clip(RoundedCornerShape(12.dp))
                .background(MainColor)
                .padding(vertical = 10.dp, horizontal = 22.dp),
            Arrangement.Center, Alignment.CenterVertically
        ) {
            SmallIcons(R.drawable.ic_pin, 22, 5)

            Spacer(Modifier.width(6.dp))

            TextRegular(stringResource(R.string.pin), size = 16)
        }

        Row(
            Modifier
                .padding(10.dp)
                .clickable {
                    artists.startArtistsRadioPlaylist(homeNav.selectedArtists)
                }
                .weight(1f)
                .clip(RoundedCornerShape(12.dp))
                .background(MainColor)
                .padding(vertical = 10.dp, horizontal = 22.dp),
            Arrangement.Center, Alignment.CenterVertically
        ) {
            when(artists.radioStatus){
                DataResponse.Loading -> LoadingCircle(22)
                else -> SmallIcons(R.drawable.ic_airdrop, 22, 5)
            }

            Spacer(Modifier.width(6.dp))

            TextRegular(stringResource(R.string.radio), size = 16)
        }
    }
}