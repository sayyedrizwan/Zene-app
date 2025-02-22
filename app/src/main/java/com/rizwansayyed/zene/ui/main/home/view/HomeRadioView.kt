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
import androidx.compose.foundation.lazy.grid.LazyHorizontalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.rizwansayyed.zene.R
import com.rizwansayyed.zene.data.ResponseResult
import com.rizwansayyed.zene.ui.view.CircularLoadingView
import com.rizwansayyed.zene.ui.view.ItemCardView
import com.rizwansayyed.zene.ui.view.ItemSmallCardView
import com.rizwansayyed.zene.ui.view.TextViewBold
import com.rizwansayyed.zene.ui.view.TextViewBorder
import com.rizwansayyed.zene.viewmodel.HomeViewModel

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun HomeRadioView(homeViewModel: HomeViewModel) {
    LazyColumn(Modifier.fillMaxSize()) {
        when (val v = homeViewModel.homeRadio) {
            ResponseResult.Empty -> {}
            is ResponseResult.Error -> {}
            ResponseResult.Loading -> item {
                Spacer(Modifier.height(50.dp))
                CircularLoadingView()
            }

            is ResponseResult.Success -> {
                item {
                    Spacer(Modifier.height(20.dp))
                }

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
                            TextViewBold(stringResource(R.string.categories), 23)
                        }
                        Spacer(Modifier.height(12.dp))
                        FlowRow(Modifier.fillMaxWidth()) {
                            v.data.countries.forEach {
                                TextViewBorder(it?.name ?: "") {

                                }
                            }
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
                        GridCells.Fixed(2),
                        Modifier
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

    LaunchedEffect(Unit) { homeViewModel.homeRadioData() }
}