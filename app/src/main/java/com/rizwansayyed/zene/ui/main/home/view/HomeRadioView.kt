package com.rizwansayyed.zene.ui.main.home.view

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyHorizontalGrid
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import com.rizwansayyed.zene.R
import com.rizwansayyed.zene.data.ResponseResult
import com.rizwansayyed.zene.ui.theme.MainColor
import com.rizwansayyed.zene.ui.view.CircularLoadingView
import com.rizwansayyed.zene.ui.view.HorizontalShimmerLoadingCard
import com.rizwansayyed.zene.ui.view.ItemCardView
import com.rizwansayyed.zene.ui.view.ItemCardViewDynamic
import com.rizwansayyed.zene.ui.view.ItemSmallCardView
import com.rizwansayyed.zene.ui.view.TextViewBold
import com.rizwansayyed.zene.ui.view.TextViewBorder
import com.rizwansayyed.zene.ui.view.TextViewSemiBold
import com.rizwansayyed.zene.utils.FirebaseEvents.FirebaseEventsParams
import com.rizwansayyed.zene.utils.FirebaseEvents.registerEvents
import com.rizwansayyed.zene.viewmodel.HomeViewModel

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun HomeRadioView(homeViewModel: HomeViewModel) {
    var categoryTypeSheet by remember { mutableStateOf<String?>(null) }

    LazyColumn(Modifier.fillMaxSize()) {
        when (val v = homeViewModel.homeRadio) {
            ResponseResult.Empty -> {}
            is ResponseResult.Error -> {}
            ResponseResult.Loading -> {
                item { Spacer(Modifier.height(20.dp)) }

                item {
                    Box(Modifier.padding(horizontal = 6.dp)) {
                        TextViewBold(stringResource(R.string.top_recent), 23)
                    }
                    Spacer(Modifier.height(12.dp))
                    HorizontalShimmerLoadingCard()

                    Spacer(Modifier.height(30.dp))
                }

                item {
                    Box(Modifier.padding(horizontal = 6.dp)) {
                        TextViewBold(stringResource(R.string.recommended_radios_for_you), 23)
                    }
                    Spacer(Modifier.height(12.dp))
                    HorizontalShimmerLoadingCard()

                    Spacer(Modifier.height(30.dp))
                }
            }

            is ResponseResult.Success -> {
                item { Spacer(Modifier.height(20.dp)) }

                if (v.data.recent?.isNotEmpty() == true) item {
                    Box(Modifier.padding(horizontal = 6.dp)) {
                        TextViewBold(stringResource(R.string.top_recent), 23)
                    }
                    Spacer(Modifier.height(12.dp))
                    LazyRow(Modifier.fillMaxWidth()) {
                        items(v.data.recent) {
                            ItemSmallCardView(it)
                        }
                    }
                    Spacer(Modifier.height(30.dp))
                }

                if (v.data.countryRadio?.isNotEmpty() == true) item {
                    Spacer(Modifier.height(30.dp))
                    Box(Modifier.padding(horizontal = 6.dp)) {
                        TextViewBold(stringResource(R.string.radio_in_your_country), 23)
                    }
                    Spacer(Modifier.height(12.dp))
                    LazyRow(Modifier.fillMaxWidth()) {
                        items(v.data.countryRadio) {
                            ItemCardView(it, v.data.countryRadio)
                        }
                    }
                    Spacer(Modifier.height(30.dp))
                }


                if (v.data.countries?.isNotEmpty() == true) item {
                    Column(Modifier.fillMaxWidth()) {
                        Spacer(Modifier.height(50.dp))
                        Box(Modifier.padding(horizontal = 6.dp)) {
                            TextViewBold(stringResource(R.string.languages), 23)
                        }
                        Spacer(Modifier.height(12.dp))
                        FlowRow(Modifier.fillMaxWidth()) {
                            v.data.countries.forEach {
                                TextViewBorder(it?.name ?: "") {
                                    categoryTypeSheet = it?.name
                                }
                            }
                        }
                    }
                }

                if (v.data.recommendedRadio?.isNotEmpty() == true) item {
                    Spacer(Modifier.height(50.dp))
                    Box(Modifier.padding(horizontal = 6.dp)) {
                        TextViewBold(stringResource(R.string.recommended_radios_for_you), 23)
                    }
                    Spacer(Modifier.height(12.dp))

                    LazyHorizontalGrid(
                        GridCells.Fixed(2), Modifier
                            .fillMaxWidth()
                            .heightIn(max = 500.dp)
                    ) {
                        items(v.data.recommendedRadio) {
                            ItemCardView(it, v.data.recommendedRadio)
                        }
                    }
                }

                if (v.data.exploreRadio?.isNotEmpty() == true) item {
                    Spacer(Modifier.height(50.dp))
                    Box(Modifier.padding(horizontal = 6.dp)) {
                        TextViewBold(stringResource(R.string.explore_more_radios), 23)
                    }
                    Spacer(Modifier.height(12.dp))

                    LazyHorizontalGrid(
                        GridCells.Fixed(2), Modifier
                            .fillMaxWidth()
                            .heightIn(max = 500.dp)
                    ) {
                        items(v.data.exploreRadio) {
                            ItemCardView(it, v.data.exploreRadio)
                        }
                    }
                }
            }
        }

        item { Spacer(Modifier.height(300.dp)) }
    }

    if (categoryTypeSheet != null) RadioCategorySheetView(categoryTypeSheet!!) {
        categoryTypeSheet = null
    }

    LaunchedEffect(Unit) {
        homeViewModel.homeRadioData()

        registerEvents(FirebaseEventsParams.RADIO_PAGE_VIEW)
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RadioCategorySheetView(name: String, close: () -> Unit) {
    ModalBottomSheet(close, contentColor = MainColor, containerColor = MainColor) {
        val viewModel: HomeViewModel = hiltViewModel(key = name)

        LazyVerticalGrid(GridCells.Fixed(3), Modifier.fillMaxWidth()) {
            item(span = { GridItemSpan(maxLineSpan) }) {
                Box(
                    Modifier
                        .padding(top = 20.dp)
                        .fillMaxWidth()
                        .padding(horizontal = 5.dp)
                ) {
                    TextViewSemiBold(name, 19)
                }
            }

            item(span = { GridItemSpan(maxLineSpan) }) { Spacer(Modifier.height(10.dp)) }

            when (val v = viewModel.radioByCountry) {
                ResponseResult.Empty -> {}
                is ResponseResult.Error -> {}
                ResponseResult.Loading -> item(span = { GridItemSpan(maxLineSpan) }) {
                    CircularLoadingView()
                }

                is ResponseResult.Success -> items(v.data) {
                    ItemCardViewDynamic(it, v.data)
                }
            }

            item(span = { GridItemSpan(maxLineSpan) }) { Spacer(Modifier.height(70.dp)) }
        }

        LaunchedEffect(Unit) { viewModel.radioByCountry(name) }
    }
}