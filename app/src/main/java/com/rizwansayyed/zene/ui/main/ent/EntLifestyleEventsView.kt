package com.rizwansayyed.zene.ui.main.ent

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.rizwansayyed.zene.data.ResponseResult
import com.rizwansayyed.zene.ui.main.ent.view.EntLifestyleLatestItemView
import com.rizwansayyed.zene.ui.main.ent.view.EntLifestyleLatestView
import com.rizwansayyed.zene.ui.main.ent.view.EntLifestyleTrendingLoadingView
import com.rizwansayyed.zene.ui.main.ent.view.EntLifestyleTrendingView

@Composable
fun EntLifestyleEventsView(viewModel: EntertainmentViewModel) {
    LazyColumn(
        Modifier.fillMaxSize(),
        contentPadding = PaddingValues(bottom = 250.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp),
    ) {
        when (val lifestyle = viewModel.discoverLifeStyle) {
            ResponseResult.Empty -> {}
            is ResponseResult.Error -> {}
            ResponseResult.Loading -> item(key = "lifestyle_loading") {
                EntLifestyleTrendingLoadingView()
            }

            is ResponseResult.Success -> if (lifestyle.data.isNotEmpty()) item(key = "lifestyle") {
                EntLifestyleTrendingView(lifestyle.data)
            }
        }

        item {
            Spacer(Modifier.height(15.dp))
        }

        when (val lifestyle = viewModel.lifeStylesEvents) {
            ResponseResult.Empty -> {}
            is ResponseResult.Error -> {}
            ResponseResult.Loading -> item(key = "latest_lifestyle_loading") {
//                EntLifestyleTrendingLoadingView()
            }

            is ResponseResult.Success -> {
                if (lifestyle.data.isNotEmpty()) item(key = "latest_lifestyle") {
                    EntLifestyleLatestView()
                }

                if (lifestyle.data.isNotEmpty()) items(lifestyle.data) {
                    EntLifestyleLatestItemView(it)
                }
            }
        }

    }

    LaunchedEffect(Unit) {
        viewModel.entLifeStyle()
    }
}