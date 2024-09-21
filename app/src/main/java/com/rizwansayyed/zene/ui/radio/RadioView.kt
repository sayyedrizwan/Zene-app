package com.rizwansayyed.zene.ui.radio

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.rizwansayyed.zene.R
import com.rizwansayyed.zene.data.api.APIResponse
import com.rizwansayyed.zene.ui.home.view.HorizontalSongView
import com.rizwansayyed.zene.ui.home.view.StyleSize
import com.rizwansayyed.zene.ui.home.view.TextSize
import com.rizwansayyed.zene.ui.radio.view.RadioCountriesView
import com.rizwansayyed.zene.ui.radio.view.RadioLanguagesView
import com.rizwansayyed.zene.ui.theme.DarkCharcoal
import com.rizwansayyed.zene.ui.view.TextPoppinsSemiBold
import com.rizwansayyed.zene.viewmodel.RadioViewModel

@Composable
fun RadioView() {
    val radioViewModel: RadioViewModel = hiltViewModel()
    LazyColumn(
        Modifier
            .fillMaxSize()
            .background(DarkCharcoal)
    ) {
        item {
            Column(Modifier.padding(horizontal = 12.dp)) {
                Spacer(Modifier.height(80.dp))
                TextPoppinsSemiBold(stringResource(R.string.zene_fm), size = 35)
                Spacer(Modifier.height(20.dp))
            }
        }

//        when (val v = radioViewModel.topRadios) {
//            APIResponse.Empty -> {}
//            is APIResponse.Error -> {}
//            APIResponse.Loading -> item {
//                Column(Modifier.padding(top = 50.dp)) {
//                    HorizontalSongView(
//                        APIResponse.Loading,
//                        Pair(TextSize.SMALL, "Trending Now"),
//                        StyleSize.SHOW_AUTHOR,
//                        showGrid = false
//                    )
//                }
//            }
//
//            is APIResponse.Success -> items(v.data) {
//                if (it.list.isNotEmpty()) {
//                    Column(Modifier.padding(top = 50.dp)) {
//                        HorizontalSongView(
//                            APIResponse.Success(it.list),
//                            Pair(TextSize.SMALL, it.name ?: ""),
//                            StyleSize.SHOW_AUTHOR,
//                            showGrid = false
//                        )
//                    }
//                }
//            }
//        }

        item {
            RadioLanguagesView(radioViewModel)
        }

        item {
            RadioCountriesView(radioViewModel)
        }

        item {
            Spacer(Modifier.height(260.dp))
        }
    }

    LaunchedEffect(Unit) {
        radioViewModel.init()
    }
}