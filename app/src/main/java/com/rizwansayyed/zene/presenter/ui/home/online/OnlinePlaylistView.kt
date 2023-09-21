package com.rizwansayyed.zene.presenter.ui.home.online

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImage
import com.rizwansayyed.zene.R
import com.rizwansayyed.zene.presenter.ui.TextRegular
import com.rizwansayyed.zene.presenter.ui.TextRegularTwoLine
import com.rizwansayyed.zene.presenter.ui.TopInfoWithSeeMore
import com.rizwansayyed.zene.presenter.util.UiUtils.toast
import com.rizwansayyed.zene.viewmodel.RoomDbViewModel

@Composable
fun PlaylistList() {
    val roomDb : RoomDbViewModel = hiltViewModel()

    Column(verticalArrangement = Arrangement.Center) {
        Spacer(Modifier.height(80.dp))

        TopInfoWithSeeMore(R.string.saved_playlists, null) {}

        LazyRow {
            items(9) {
                PlaylistItems()
            }
        }
    }
}

const val datee =
    "https://lh3.googleusercontent.com/zMEIYn8d1PaMRfWALe0tFAZsR6HqntGr-bxwaa7l2o-t_lApRz-D4FC2UxN761FtAtVyMcrIinXoPFE=w544-h544-l90-rj"

@Composable
fun PlaylistItems() {
    val width = LocalConfiguration.current.screenWidthDp

    Column(
        Modifier
            .padding(end = 36.dp)
            .width((width / 2.3).dp), Arrangement.Center, Alignment.CenterHorizontally) {
        Box {
            Image(
                painterResource(id = R.drawable.ic_cd_blue),
                "",
                Modifier
                    .align(Alignment.Center)
                    .width((width / 2).dp)
                    .offset(x = 60.dp, y = 0.dp)
                    .padding(35.dp)
            )

            AsyncImage(
                datee,
                "",
                Modifier
                    .align(Alignment.Center)
                    .width((width / 2.2 - 40).dp)
                    .clip(RoundedCornerShape(15.dp))
            )
        }

        TextRegularTwoLine("Spiderman in Spiderverse is", doCenter = true)
    }
}