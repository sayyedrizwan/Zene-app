package com.rizwansayyed.zene.ui.main.ent

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.bumptech.glide.integration.compose.GlideImage
import com.rizwansayyed.zene.R
import com.rizwansayyed.zene.data.ResponseResult
import com.rizwansayyed.zene.data.model.ZeneMusicData
import com.rizwansayyed.zene.ui.main.ent.discoverview.CategoryChip
import com.rizwansayyed.zene.ui.main.ent.discoverview.EntBuzzNewsViewItem
import com.rizwansayyed.zene.ui.main.ent.utils.BrandCache
import com.rizwansayyed.zene.ui.main.ent.utils.BrandCache.downloadFavicon
import com.rizwansayyed.zene.ui.main.ent.utils.BrandCache.extractBrandName
import com.rizwansayyed.zene.ui.main.ent.utils.BrandCache.extractDarkColor
import com.rizwansayyed.zene.ui.main.ent.utils.BrandInfo
import com.rizwansayyed.zene.ui.view.CircularLoadingView
import com.rizwansayyed.zene.ui.view.ShimmerEffect
import com.rizwansayyed.zene.ui.view.TextViewBold
import com.rizwansayyed.zene.ui.view.TextViewNormal
import com.rizwansayyed.zene.utils.FirebaseEvents.FirebaseEventsParams
import com.rizwansayyed.zene.utils.FirebaseEvents.registerEvents
import com.rizwansayyed.zene.viewmodel.EntertainmentViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.net.URI

@Composable
fun EntertainmentBuzzView(viewModel: EntertainmentViewModel) {
    LazyVerticalGrid(
        GridCells.Fixed(2),
        contentPadding = PaddingValues(bottom = 250.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp),
    ) {
        when (val v = viewModel.discover) {
            ResponseResult.Empty -> {}
            is ResponseResult.Error -> {}
            ResponseResult.Loading -> {
                items(6) {
                    ShimmerEffect(
                        Modifier
                            .padding(horizontal = 5.dp)
                            .aspectRatio(0.75f)
                            .clip(RoundedCornerShape(16.dp))
                    )
                }
            }

            is ResponseResult.Success -> {
                item(span = { GridItemSpan(2) }) {
                    Box(
                        Modifier
                            .padding(horizontal = 6.dp)
                            .padding(top = 15.dp)
                    ) {
                        TextViewBold(stringResource(R.string.hot_buzz), 23)
                    }
                }
                items(v.data.news ?: emptyList()) { item ->
                    NewsTrendingCard(item)
                }

                item(span = { GridItemSpan(2) }) {
                    Spacer(modifier = Modifier.height(15.dp))
                }
            }
        }


        when (val v = viewModel.buzzNews) {
            ResponseResult.Empty -> {}
            is ResponseResult.Error -> {}
            ResponseResult.Loading -> {
                item(span = { GridItemSpan(2) }) {
                    Box(
                        Modifier
                            .padding(horizontal = 6.dp)
                            .padding(top = 15.dp)
                    ) {
                        TextViewBold(stringResource(R.string.latest_buzz), 23)
                    }
                }
                item(span = { GridItemSpan(2) }) {
                    Column(Modifier.fillMaxWidth()) {
                        CircularLoadingView()
                    }
                }
            }

            is ResponseResult.Success -> {
                item(span = { GridItemSpan(2) }) {
                    Box(
                        Modifier
                            .padding(horizontal = 6.dp)
                            .padding(top = 15.dp)
                    ) {
                        TextViewBold(stringResource(R.string.latest_buzz), 23)
                    }
                }
                items(v.data, span = { GridItemSpan(2) }) { item ->
                    EntBuzzNewsViewItem(item)
                }
            }
        }
    }


    LaunchedEffect(Unit) {
        viewModel.entBuzzNews()

        registerEvents(FirebaseEventsParams.ENT_BUZZ_PAGE_VIEW)
    }
}

@OptIn(ExperimentalGlideComposeApi::class)
@Composable
fun NewsTrendingCard(item: ZeneMusicData) {
    var webColor by remember { mutableStateOf<BrandInfo?>(null) }

    Box(
        Modifier
            .padding(horizontal = 9.dp)
            .aspectRatio(0.75f)
            .clip(RoundedCornerShape(16.dp))
    ) {
        GlideImage(
            item.thumbnail, item.name, Modifier.fillMaxSize(),
            contentScale = ContentScale.Crop
        )

        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    Brush.verticalGradient(
                        colors = listOf(
                            Color.Transparent,
                            Color.Black.copy(alpha = 0.85f)
                        )
                    )
                )
        )

        Box(
            Modifier
                .align(Alignment.TopEnd)
                .padding(12.dp)
        ) {
            CategoryChip(extractBrandName(item.id), webColor?.darkColor)
        }

        Column(
            modifier = Modifier
                .align(Alignment.BottomStart)
                .padding(12.dp)
        ) {
            TextViewNormal("${item.extra} ${stringResource(R.string.ago)}", size = 14)
            Spacer(modifier = Modifier.height(4.dp))
            TextViewBold(item.name ?: "", 14, line = 4)
            Spacer(modifier = Modifier.height(2.dp))
        }
    }

    LaunchedEffect(item.id) {
        BrandCache.brandData[item.id]?.let {
            webColor = it
            return@LaunchedEffect
        }

        val brandInfo = withContext(Dispatchers.IO) {
            val brandName = extractBrandName(item.id ?: "")
            val faviconUrl = "https://${URI(item.id ?: "").host}/favicon.ico"

            val bitmap = downloadFavicon(faviconUrl)
            val darkColor = bitmap?.let { extractDarkColor(it) } ?: Color.DarkGray
            BrandInfo(brandName, darkColor)
        }

        BrandCache.brandData[item.id ?: ""] = brandInfo
        webColor = brandInfo
    }
}
