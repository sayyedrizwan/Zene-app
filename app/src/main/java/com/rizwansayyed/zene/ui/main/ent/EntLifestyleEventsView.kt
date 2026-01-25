package com.rizwansayyed.zene.ui.main.ent

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.rizwansayyed.zene.R
import com.rizwansayyed.zene.data.ResponseResult
import com.rizwansayyed.zene.ui.main.ent.discoverview.EntLifestyleLoadingView
import com.rizwansayyed.zene.ui.main.ent.view.EntLifestyleTrendingLoadingView
import com.rizwansayyed.zene.ui.main.ent.view.EntLifestyleTrendingView

@Composable
fun EntLifestyleView(viewModel: EntertainmentViewModel) {
    LazyVerticalGrid(
        GridCells.Fixed(2),
        contentPadding = PaddingValues(bottom = 250.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp),
    ) {
        when (val lifestyle = viewModel.discoverLifeStyle) {
            ResponseResult.Empty -> {}
            is ResponseResult.Error -> {}
            ResponseResult.Loading -> item(
                key = "lifestyle_loading", span = { GridItemSpan(2) }) {
                EntLifestyleTrendingLoadingView()
            }

            is ResponseResult.Success -> if (lifestyle.data.isNotEmpty()) item(
                key = "lifestyle", span = { GridItemSpan(2) }) {
                EntLifestyleTrendingView(lifestyle.data)
            }
        }

    }

    LaunchedEffect(Unit) {
        viewModel.entLifeStyle()
    }
}