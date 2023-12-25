package com.rizwansayyed.zene.presenter.ui.home.online.radio

import android.util.Log
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImage
import com.rizwansayyed.zene.R
import com.rizwansayyed.zene.data.DataResponse
import com.rizwansayyed.zene.data.db.datastore.DataStorageManager.userIpDetails
import com.rizwansayyed.zene.domain.OnlineRadioResponseItem
import com.rizwansayyed.zene.domain.toMusicData
import com.rizwansayyed.zene.presenter.theme.DarkGreyColor
import com.rizwansayyed.zene.presenter.theme.MainColor
import com.rizwansayyed.zene.presenter.ui.MenuIcon
import com.rizwansayyed.zene.presenter.ui.SearchEditTextView
import com.rizwansayyed.zene.presenter.ui.TextRegular
import com.rizwansayyed.zene.presenter.ui.TextSemiBold
import com.rizwansayyed.zene.presenter.ui.TextThin
import com.rizwansayyed.zene.presenter.ui.home.online.LoadingAlbumsCards
import com.rizwansayyed.zene.presenter.util.UiUtils.GridSpan.TOTAL_ITEMS_GRID
import com.rizwansayyed.zene.presenter.util.UiUtils.GridSpan.TWO_ITEMS_GRID
import com.rizwansayyed.zene.presenter.util.UiUtils.toCapitalFirst
import com.rizwansayyed.zene.presenter.util.UiUtils.toast
import com.rizwansayyed.zene.service.player.utils.Utils.playRadioOnPlayer
import com.rizwansayyed.zene.viewmodel.HomeApiViewModel
import com.rizwansayyed.zene.viewmodel.HomeNavViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlin.time.Duration.Companion.seconds


@OptIn(ExperimentalComposeUiApi::class, ExperimentalFoundationApi::class)
@Composable
fun OnlineRadioViewAllView() {
    val homeApiViewModel: HomeApiViewModel = hiltViewModel()
    val homeNav: HomeNavViewModel = hiltViewModel()
    val ip by userIpDetails.collectAsState(initial = null)

    val keyboard = LocalSoftwareKeyboardController.current
    var searchJob by remember { mutableStateOf<Job?>(null) }
    val sRadio = stringResource(id = R.string.search_radio)
    val enterMoreThreeChar = stringResource(id = R.string.enter_more_then_3_char_to_search)
    var text by remember { mutableStateOf("") }

    LazyVerticalGrid(
        GridCells.Fixed(TOTAL_ITEMS_GRID),
        Modifier
            .fillMaxSize()
            .background(DarkGreyColor)
    ) {
        item(span = { GridItemSpan(TOTAL_ITEMS_GRID) }) {
            Box(
                modifier = Modifier.padding(
                    start = 12.dp,
                    end = 12.dp,
                    bottom = 16.dp,
                    top = 54.dp
                ), contentAlignment = Alignment.Center
            ) {
                SearchEditTextView(sRadio, text, null, {
                    text = it
                    if (it.length <= 3) return@SearchEditTextView

                    searchJob?.cancel()
                    searchJob = CoroutineScope(Dispatchers.IO).launch {
                        delay(1.seconds)
                        homeApiViewModel.onlineRadiosSearch(it.trim())
                    }
                }, {
                    if (text.length <= 3) {
                        enterMoreThreeChar.toast()
                        return@SearchEditTextView
                    }
                    keyboard?.hide()
                })
            }
        }

        item(span = { GridItemSpan(TOTAL_ITEMS_GRID) }) {
            Column(Modifier.padding(start = 12.dp)) {
                Spacer(Modifier.height(30.dp))

                if (text.length <= 3)
                    TextSemiBold(
                        String.format(stringResource(R.string.radio_in_c), ip?.country), size = 25
                    )
                else
                    TextSemiBold(stringResource(R.string.radio_s_worldwide), size = 25)

                Spacer(Modifier.height(30.dp))
            }
        }

        when (val v = homeApiViewModel.onlineRadioAll) {
            DataResponse.Empty -> {}
            is DataResponse.Error -> {}
            DataResponse.Loading -> items(60, span = { GridItemSpan(TWO_ITEMS_GRID) }) {
                LoadingAlbumsCards()
            }

            is DataResponse.Success -> {
                if (v.item.isEmpty())
                    item(span = { GridItemSpan(TOTAL_ITEMS_GRID) }) {
                        Column(Modifier.padding(start = 12.dp)) {
                            Spacer(Modifier.height(30.dp))

                            if (text.length <= 3)
                                TextRegular(stringResource(R.string.radio_in_c))
                            else
                                TextRegular(stringResource(R.string.no_radio_found_search_else))

                            Spacer(Modifier.height(30.dp))
                        }
                    }

                items(v.item, span = { GridItemSpan(TWO_ITEMS_GRID) }) {
                    Box(Modifier.padding(1.dp)) {
                        RadioSearchCard(it, homeNav)
                    }
                }
            }
        }

        item(span = { GridItemSpan(TOTAL_ITEMS_GRID) }) {
            Spacer(Modifier.height(200.dp))
        }
    }

    LaunchedEffect(Unit) {
        homeApiViewModel.onlineRadiosInCountry()
    }
}

@Composable
fun RadioSearchCard(radio: OnlineRadioResponseItem, homeNav: HomeNavViewModel) {
    Column(
        Modifier
            .padding(5.dp)
            .width(LocalConfiguration.current.screenWidthDp.dp / 2)
            .clip(RoundedCornerShape(12.dp))
            .background(MainColor)
            .aspectRatio(1f)
            .clickable {
                playRadioOnPlayer(radio)
            },
        Arrangement.Center, Alignment.CenterHorizontally
    ) {
        Box(
            Modifier
                .fillMaxWidth()
                .padding(top = 6.dp, end = 3.dp)
        ) {
            MenuIcon(Modifier.align(Alignment.CenterEnd)) {
                homeNav.setRadioTemp(radio)
                homeNav.setSongDetailsDialog(radio.toMusicData())
            }
        }

        AsyncImage(
            radio.favicon, radio.name,
            Modifier
                .padding(top = 25.dp)
                .clip(RoundedCornerShape(50))
                .size(50.dp)
        )

        Spacer(Modifier.height(4.dp))
        TextSemiBold(radio.name ?: "", doCenter = true, size = 14)
        Spacer(Modifier.height(4.dp))
        TextThin(radio.language?.toCapitalFirst() ?: "", doCenter = true, size = 12)
        Spacer(Modifier.height(25.dp))
    }
}