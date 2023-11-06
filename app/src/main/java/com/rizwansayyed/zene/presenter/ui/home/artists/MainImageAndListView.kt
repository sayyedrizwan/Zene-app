package com.rizwansayyed.zene.presenter.ui.home.artists

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImage
import com.rizwansayyed.zene.data.DataResponse
import com.rizwansayyed.zene.presenter.util.UiUtils.toast
import com.rizwansayyed.zene.utils.Utils.homeSetWallpaper
import com.rizwansayyed.zene.utils.Utils.littleVibrate
import com.rizwansayyed.zene.viewmodel.ArtistsViewModel
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.debounce


@OptIn(FlowPreview::class)
@Composable
fun MainImageAndList() {
    val artistsViewModel: ArtistsViewModel = hiltViewModel()
    val listState = rememberLazyListState()

    var img by remember { mutableStateOf("") }

    Spacer(Modifier.height(55.dp))

    AsyncImage(
        img,
        "",
        Modifier.size(LocalConfiguration.current.screenWidthDp.dp),
        contentScale = ContentScale.Crop
    )

    Spacer(Modifier.height(10.dp))

    when (val v = artistsViewModel.artistsImages) {
        DataResponse.Empty -> {}
        is DataResponse.Error -> {}
        DataResponse.Loading -> {}
        is DataResponse.Success -> {
            LazyRow(Modifier.fillMaxWidth(), listState) {
                items(v.item) {
                    SmallImageView(it) { i ->
                        img = i
                        homeSetWallpaper(i)
                    }
                }
            }

            LaunchedEffect(listState) {
                snapshotFlow {
                    listState.firstVisibleItemIndex
                }.debounce(100L).collectLatest {
                    littleVibrate()
                    img = v.item[it]
                }
            }
        }
    }
}


@Composable
fun SmallImageView(s: String, click: (String) -> Unit) {
    AsyncImage(
        s, "",
        Modifier
            .padding(9.dp)
            .size(100.dp)
            .clip(RoundedCornerShape(3))
            .clickable {
                click(s)
            },
        contentScale = ContentScale.Crop
    )
}