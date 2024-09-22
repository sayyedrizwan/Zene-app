package com.rizwansayyed.zene.ui.radio

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.rizwansayyed.zene.R
import com.rizwansayyed.zene.data.api.APIResponse
import com.rizwansayyed.zene.ui.mymusic.MyMusicType
import com.rizwansayyed.zene.ui.theme.DarkCharcoal
import com.rizwansayyed.zene.ui.view.LoadingView
import com.rizwansayyed.zene.ui.view.SongDynamicCards
import com.rizwansayyed.zene.ui.view.TextPoppins
import com.rizwansayyed.zene.ui.view.TextPoppinsSemiBold
import com.rizwansayyed.zene.ui.view.isScreenBig
import com.rizwansayyed.zene.utils.Utils.THREE_GRID_SIZE
import com.rizwansayyed.zene.utils.Utils.TOTAL_GRID_SIZE
import com.rizwansayyed.zene.utils.Utils.TWO_GRID_SIZE
import com.rizwansayyed.zene.viewmodel.RadioViewModel
import java.util.Locale

@Composable
fun RadioCountryListView(country: String) {
    val radioViewModel: RadioViewModel = hiltViewModel()
    val isThreeGrid = isScreenBig()

    var page by remember { mutableIntStateOf(0) }

    LazyVerticalGrid(
        GridCells.Fixed(TOTAL_GRID_SIZE),
        Modifier
            .fillMaxSize()
            .background(DarkCharcoal)
    ) {
        item(1, { GridItemSpan(TOTAL_GRID_SIZE) }) {
            Column(Modifier.padding(horizontal = 12.dp)) {
                Spacer(Modifier.height(80.dp))
                TextPoppinsSemiBold(country.replaceFirstChar {
                    if (it.isLowerCase()) it.titlecase(
                        Locale.getDefault()
                    ) else it.toString()
                }, size = 35)
                Spacer(Modifier.height(20.dp))
            }
        }


        item(2, { GridItemSpan(TOTAL_GRID_SIZE) }) {
            Spacer(Modifier.height(20.dp))
        }

        items(radioViewModel.radiosCountriesList,
            span = { GridItemSpan(if (isThreeGrid) THREE_GRID_SIZE else TWO_GRID_SIZE) }) {
            SongDynamicCards(it, radioViewModel.radiosCountriesList)
        }

        item(4, { GridItemSpan(TOTAL_GRID_SIZE) }) {
            Spacer(Modifier.height(20.dp))
        }


        item(1000, { GridItemSpan(TOTAL_GRID_SIZE) }) {
            Column(Modifier.fillMaxWidth(), Arrangement.Center, Alignment.CenterHorizontally) {
                if (radioViewModel.isLoading) {
                    LoadingView(Modifier.size(24.dp))
                }

                if (radioViewModel.seeMoreButton && !radioViewModel.isLoading) {
                    Box(
                        Modifier
                            .padding(vertical = 15.dp, horizontal = 6.dp)
                            .clip(RoundedCornerShape(100))
                            .background(Color.Black)
                            .clickable {
                                page += 1
                                radioViewModel.countriesRadioList(page)
                            }
                            .border(1.dp, Color.White, RoundedCornerShape(100))
                            .padding(vertical = 9.dp, horizontal = 18.dp)) {
                        TextPoppins(stringResource(R.string.load_more), size = 15)
                    }
                }
            }
        }

        item(1001, { GridItemSpan(TOTAL_GRID_SIZE) }) {
            Spacer(Modifier.height(350.dp))
        }
    }

    LaunchedEffect(Unit) {
        radioViewModel.countriesRadioList(page)
    }
}