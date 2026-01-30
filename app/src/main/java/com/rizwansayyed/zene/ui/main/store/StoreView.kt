package com.rizwansayyed.zene.ui.main.store

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.bumptech.glide.integration.compose.GlideImage
import com.rizwansayyed.zene.R
import com.rizwansayyed.zene.data.ResponseResult
import com.rizwansayyed.zene.ui.main.store.view.StoreChipsTypeView
import com.rizwansayyed.zene.ui.main.store.view.StoreTopDealsView
import com.rizwansayyed.zene.ui.view.CircularLoadingView
import com.rizwansayyed.zene.ui.view.TextViewBold
import com.rizwansayyed.zene.viewmodel.HomeViewModel

@OptIn(ExperimentalGlideComposeApi::class)
@Composable
fun StoreView(viewModel: HomeViewModel) {
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(top = 60.dp, bottom = 250.dp)
    ) {
        item {
            Row(Modifier.padding(horizontal = 6.dp)) {
                Column(horizontalAlignment = Alignment.End) {
                    TextViewBold(stringResource(R.string.store).uppercase(), 27)
                    Spacer(Modifier.height(1.dp))
                    GlideImage(R.drawable.ic_amazon_logo, "", Modifier.width(50.dp))
                }
                Spacer(Modifier.weight(1f))
            }
        }

        item {
            StoreChipsTypeView()
        }

        item {
//            Spacer(modifier = Modifier.height(5.dp))
//            StoreBannerAdsView()
        }

        when(val v = viewModel.topStoreDeals) {
            ResponseResult.Empty -> {}
            is ResponseResult.Error -> {}
            ResponseResult.Loading -> item {
                Box(Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
                    CircularLoadingView()
                }
            }
            is ResponseResult.Success -> {
                item {
                    Spacer(Modifier.height(20.dp))
                    Box(Modifier.padding(horizontal = 6.dp)) {
                        TextViewBold(stringResource(R.string.top_deals_on_amazon), 23)
                    }
                    Spacer(Modifier.height(12.dp))
                }

                items(v.data) { v ->
                    StoreTopDealsView(v)
                }
            }
        }
    }

    LaunchedEffect(Unit) {
        viewModel.storeTopDeals()
    }
}