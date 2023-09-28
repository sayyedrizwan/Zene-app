package com.rizwansayyed.zene.presenter.ui.home.online

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImage
import com.rizwansayyed.zene.R
import com.rizwansayyed.zene.data.DataResponse
import com.rizwansayyed.zene.presenter.theme.LightBlack
import com.rizwansayyed.zene.presenter.ui.LoadingStateBar
import com.rizwansayyed.zene.presenter.ui.TextSemiBold
import com.rizwansayyed.zene.presenter.ui.TextSemiBoldBig
import com.rizwansayyed.zene.presenter.ui.TextSemiBoldDualLines
import com.rizwansayyed.zene.presenter.ui.TextThin
import com.rizwansayyed.zene.presenter.ui.TextThinBig
import com.rizwansayyed.zene.presenter.util.UiUtils.toMoneyFormat
import com.rizwansayyed.zene.viewmodel.HomeApiViewModel


@Composable
fun CurrentMostPlayingSong() {
    val homeApiViewModel: HomeApiViewModel = hiltViewModel()

    when (val v = homeApiViewModel.mostPlayingSong) {
        DataResponse.Empty -> {}
        is DataResponse.Error -> {}
        DataResponse.Loading -> {
            LoadingStateBar()

            TextThinBig(
                stringResource(id = R.string.currently_playing_song_on_app).lowercase(),
                Modifier
                    .padding(14.dp)
                    .fillMaxWidth(),
                singleLine = true,
                doCenter = true
            )
        }

        is DataResponse.Success -> {
            Row(Modifier.fillMaxWidth(), Arrangement.Center, Alignment.CenterVertically) {
                AsyncImage(
                    v.item.first?.thumbnail,
                    "",
                    Modifier
                        .padding(3.dp)
                        .weight(3f)
                        .clip(RoundedCornerShape(12.dp)),
                    contentScale = ContentScale.Crop
                )

                Column(
                    Modifier
                        .padding(3.dp)
                        .weight(1f), Arrangement.Center, Alignment.CenterHorizontally
                ) {
                    Column(
                        Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(9.dp))
                            .background(LightBlack)
                            .padding(vertical = 15.dp),
                        Arrangement.Center, Alignment.CenterHorizontally
                    ) {
                        AsyncImage(v.item.second?.image, "", Modifier.fillMaxWidth())

                        Spacer(Modifier.height(7.dp))

                        v.item.first?.artists?.let { TextThin(it) }

                    }

                    Spacer(Modifier.height(5.dp))

                    Column(
                        Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(9.dp))
                            .background(LightBlack)
                            .padding(vertical = 20.dp),
                        Arrangement.Center, Alignment.CenterHorizontally
                    ) {
                        TextSemiBoldBig(v.item.second?.listeners?.toMoneyFormat() ?: "137,196")

                        Spacer(Modifier.height(3.dp))

                        TextThin(stringResource(id = R.string.listeners))
                    }

                    Spacer(Modifier.height(5.dp))

                    v.item.first?.name?.let {
                        Column(
                            Modifier
                                .fillMaxWidth()
                                .clip(RoundedCornerShape(9.dp))
                                .background(LightBlack)
                                .padding(vertical = 20.dp),
                            Arrangement.Center, Alignment.CenterHorizontally
                        ) {
                            TextSemiBoldDualLines(it, doCenter = true)
                        }
                    }
                }
            }

            TextThinBig(
                stringResource(id = R.string.currently_playing_song_on_app).lowercase(),
                Modifier
                    .padding(14.dp)
                    .fillMaxWidth(),
                singleLine = true,
                doCenter = true
            )
        }
    }
}