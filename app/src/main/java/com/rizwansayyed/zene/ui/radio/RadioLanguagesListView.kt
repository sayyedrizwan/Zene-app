package com.rizwansayyed.zene.ui.radio

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.capitalize
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.rizwansayyed.zene.R
import com.rizwansayyed.zene.data.api.APIResponse
import com.rizwansayyed.zene.ui.theme.DarkCharcoal
import com.rizwansayyed.zene.ui.view.LoadingView
import com.rizwansayyed.zene.ui.view.SongDynamicCards
import com.rizwansayyed.zene.ui.view.TextPoppinsSemiBold
import com.rizwansayyed.zene.ui.view.isScreenBig
import com.rizwansayyed.zene.utils.Utils.THREE_GRID_SIZE
import com.rizwansayyed.zene.utils.Utils.TOTAL_GRID_SIZE
import com.rizwansayyed.zene.utils.Utils.TWO_GRID_SIZE
import com.rizwansayyed.zene.viewmodel.RadioViewModel
import java.util.Locale

@Composable
fun RadioLanguagesListView(languages: String) {
    val radioViewModel: RadioViewModel = hiltViewModel()
    val isThreeGrid = isScreenBig()

    LazyVerticalGrid(
        GridCells.Fixed(TOTAL_GRID_SIZE),
        Modifier
            .fillMaxSize()
            .background(DarkCharcoal)
    ) {
        item(1, { GridItemSpan(TOTAL_GRID_SIZE) }) {
            Column(Modifier.padding(horizontal = 12.dp)) {
                Spacer(Modifier.height(80.dp))
                TextPoppinsSemiBold(languages.replaceFirstChar {
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

        when (val v = radioViewModel.radiosLanguagesList) {
            APIResponse.Empty -> {}
            is APIResponse.Error -> {}
            APIResponse.Loading -> {
                item(7, { GridItemSpan(TOTAL_GRID_SIZE) }) {
                    LoadingView(Modifier.size(32.dp))
                }
            }

            is APIResponse.Success -> if (v.data.isNotEmpty()) {
                items(v.data,
                    span = { GridItemSpan(if (isThreeGrid) THREE_GRID_SIZE else TWO_GRID_SIZE) }) {
                    SongDynamicCards(it, v.data)
                }
            } else
                item(8, { GridItemSpan(TOTAL_GRID_SIZE) }) {
                    TextPoppinsSemiBold(stringResource(R.string.no_radio_of_this_lang), true, size = 16)
                }

        }

        item(3, { GridItemSpan(TOTAL_GRID_SIZE) }) {
            Spacer(Modifier.height(250.dp))
        }
    }


    LaunchedEffect(Unit) {
        radioViewModel.languagesRadioList(languages)
    }

}