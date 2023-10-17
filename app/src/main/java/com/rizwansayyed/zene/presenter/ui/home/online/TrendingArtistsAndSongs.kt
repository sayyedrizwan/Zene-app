package com.rizwansayyed.zene.presenter.ui.home.online

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyHorizontalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.rizwansayyed.zene.R
import com.rizwansayyed.zene.data.DataResponse
import com.rizwansayyed.zene.data.db.datastore.DataStorageManager
import com.rizwansayyed.zene.data.db.datastore.DataStorageManager.userIpDetails
import com.rizwansayyed.zene.presenter.ui.TopInfoWithImage
import com.rizwansayyed.zene.viewmodel.HomeApiViewModel
import kotlinx.coroutines.flow.flowOf

@Composable
fun TrendingSongsCountryList() {
    val homeViewModel: HomeApiViewModel = hiltViewModel()
    val country by userIpDetails.collectAsState(initial = null)

    when (val v = homeViewModel.topCountryTrendingSongs) {
        DataResponse.Empty -> {}
        is DataResponse.Error -> {}
        DataResponse.Loading -> {
            if (country?.city != null)
                TopInfoWithImage(
                    String.format(stringResource(id = R.string.top_songs_in_c), country?.country),
                    null
                ) {}
        }

        is DataResponse.Success -> {
            if (country?.city != null)
                TopInfoWithImage(
                    String.format(stringResource(id = R.string.top_songs_in_c), country?.country),
                    null
                ) {}



            LazyHorizontalGrid(
                GridCells.Fixed(2), Modifier
                    .fillMaxWidth()
                    .animateContentSize()
                    .height(((if (fullView) 245 else 170) * 2).dp)
            ) {
                items(v.item) {
                    AnimatedContent(
                        fullView, Modifier, {
                            fadeIn(animationSpec = tween(durationMillis = 500)) togetherWith fadeOut(
                                animationSpec = tween(durationMillis = 500)
                            )
                        }, label = ""
                    ) { targetState ->
                        if (targetState) GlobalSongsItemsFull(it)
                        else GlobalSongsItemsImg(it)
                    }
                }
            }
        }
    }
}