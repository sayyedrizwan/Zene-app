package com.rizwansayyed.zene.ui.main.ent.nav

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
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
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.bumptech.glide.integration.compose.GlideImage
import com.rizwansayyed.zene.R
import com.rizwansayyed.zene.data.ResponseResult
import com.rizwansayyed.zene.data.model.LifeStyleEventsInfo
import com.rizwansayyed.zene.data.model.ZeneMusicData
import com.rizwansayyed.zene.service.notification.NavigationUtils
import com.rizwansayyed.zene.ui.main.ent.EntertainmentViewModel
import com.rizwansayyed.zene.ui.main.ent.view.extractDate
import com.rizwansayyed.zene.ui.main.ent.view.removeLifestyleDate
import com.rizwansayyed.zene.ui.main.view.share.ShareDataView
import com.rizwansayyed.zene.ui.theme.DarkCharcoal
import com.rizwansayyed.zene.ui.theme.MainColor
import com.rizwansayyed.zene.ui.view.ImageIcon
import com.rizwansayyed.zene.ui.view.ShimmerEffect
import com.rizwansayyed.zene.ui.view.TextViewBold
import com.rizwansayyed.zene.ui.view.TextViewNormal
import com.rizwansayyed.zene.ui.view.TextViewSemiBold
import com.rizwansayyed.zene.utils.share.MediaContentUtils.openCustomBrowser
import com.rizwansayyed.zene.utils.share.MediaContentUtils.startMedia


@Composable
fun LifeStyleShimmerView() {
    LazyVerticalGrid(
        columns = GridCells.Fixed(2),
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black)
    ) {

        item(span = { GridItemSpan(2) }) {
            ShimmerEffect(
               Modifier
                    .fillMaxWidth()
                    .aspectRatio(9f / 16f)
            )
        }

        item(span = { GridItemSpan(2) }) {
            Column(
                Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 8.dp, vertical = 12.dp)
            ) {

                ShimmerEffect(
                    Modifier
                        .fillMaxWidth(0.7f)
                        .height(32.dp)
                )

                Spacer(Modifier.height(8.dp))

                ShimmerEffect(
                    Modifier
                        .fillMaxWidth(0.5f)
                        .height(18.dp)
                )

                Spacer(Modifier.height(6.dp))

                ShimmerEffect(
                    Modifier
                        .fillMaxWidth(0.35f)
                        .height(16.dp)
                )
            }
        }


        item(span = { GridItemSpan(2) }) {
            Spacer(Modifier.height(40.dp))
        }

        items(4, span = { GridItemSpan(2) }) {
            Box(Modifier.padding(10.dp)) {
                ShimmerEffect(Modifier.fillMaxWidth().height(30.dp).clip(RoundedCornerShape(20.dp)))
            }
        }

        item(span = { GridItemSpan(2) }) {
            Spacer(Modifier.height(40.dp))
        }

        items(6) {
            Box(Modifier.padding(10.dp)) {
                ShimmerEffect(Modifier.fillMaxWidth().height(90.dp).clip(RoundedCornerShape(20.dp)))
            }
        }

        item(span = { GridItemSpan(2) }) {
            Spacer(Modifier.height(200.dp))
        }
    }
}


@OptIn(ExperimentalGlideComposeApi::class)
@Composable
fun LifeStyleNavView(id: String) {
    val viewModel: EntertainmentViewModel = hiltViewModel()
    var showShareView by remember { mutableStateOf<ZeneMusicData?>(null) }

    Box(
        Modifier
            .fillMaxSize()
            .background(Color.Black)
    ) {
        when (val v = viewModel.lifeStylesInfo) {
            ResponseResult.Empty -> {}
            is ResponseResult.Error -> {}
            ResponseResult.Loading -> LifeStyleShimmerView()
            is ResponseResult.Success -> LazyVerticalGrid(
                GridCells.Fixed(2), Modifier.fillMaxSize()
            ) {
                item(span = { GridItemSpan(2) }) {
                    GlideImage(
                        model = v.data.image,
                        contentDescription = v.data.name,
                        modifier = Modifier
                            .fillMaxWidth()
                            .aspectRatio(9f / 16f),
                        contentScale = ContentScale.Crop
                    )
                }
                item(span = { GridItemSpan(2) }) {
                    Row(Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 5.dp, vertical = 10.dp), verticalAlignment = Alignment.CenterVertically) {
                        Column(Modifier.weight(1f)) {
                            TextViewBold(v.data.name.orEmpty(), 39)
                            TextViewSemiBold(removeLifestyleDate(v.data.title.orEmpty()), 15)
                            TextViewNormal(extractDate(v.data.title.orEmpty()), 14, Color.Gray)
                            Spacer(Modifier.height(10.dp))
                        }

                        Box(
                            Modifier
                                .padding(horizontal = 4.dp)
                                .clickable {
                                    showShareView = v.data.shareData()
                                }) {
                            ImageIcon(R.drawable.ic_share, 25)
                        }

                    }
                }
                item(span = { GridItemSpan(2) }) {
                    Column {
                        Spacer(Modifier.height(20.dp))
                        Box(Modifier.padding(horizontal = 6.dp)) {
                            TextViewBold(stringResource(R.string.wearing), 23)
                        }
                        Spacer(Modifier.height(5.dp))
                    }
                }
                items(v.data.products ?: emptyList(), span = { GridItemSpan(2) }) {
                    WearingItemCard(it)
                }

                item(span = { GridItemSpan(2) }) {
                    Column {
                        Spacer(Modifier.height(20.dp))
                        Box(Modifier.padding(horizontal = 6.dp)) {
                            TextViewBold(stringResource(R.string.other_of_their_lifestyles), 23)
                        }
                        Spacer(Modifier.height(5.dp))
                    }
                }

                items(v.data.similar) {
                    SimilarItemCard(it)
                }

                item(span = { GridItemSpan(2) }) {
                    Spacer(Modifier.height(250.dp))
                }
            }
        }
    }

    if (showShareView != null) ShareDataView(showShareView) {
        showShareView = null
    }

    LaunchedEffect(Unit) {
        viewModel.entLifeStyleInfo(id)
    }
}

@OptIn(ExperimentalGlideComposeApi::class)
@Composable
fun WearingItemCard(data: LifeStyleEventsInfo.Products) {
    Card(
        Modifier
            .padding(horizontal = 7.dp, vertical = 8.dp)
            .fillMaxWidth(),
        RoundedCornerShape(24.dp),
        CardDefaults.cardColors(DarkCharcoal),
        CardDefaults.cardElevation(8.dp)
    ) {
        Row(
            Modifier
                .clickable {
                    if (data.url?.trim().orEmpty().length > 4) openCustomBrowser(data.url)
                }
                .fillMaxSize()
                .padding(10.dp), Arrangement.SpaceBetween, Alignment.CenterVertically
        ) {
            GlideImage(
                data.img, data.title,
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .size(90.dp)
                    .clip(RoundedCornerShape(16.dp))
                    .background(Color.White)
            )
            Spacer(Modifier.width(10.dp))
            Column(Modifier.weight(1f), Arrangement.Center) {
                Spacer(Modifier.height(5.dp))
                TextViewBold(data.title.orEmpty(), 16, line = 3)
                Spacer(Modifier.height(5.dp))
            }
            Spacer(Modifier.width(10.dp))

            if (data.url?.trim().orEmpty().length > 4) Button(
                onClick = { openCustomBrowser(data.url) },
                shape = RoundedCornerShape(50),
                colors = ButtonDefaults.buttonColors(containerColor = MainColor),
            ) {
                TextViewNormal(stringResource(R.string.shop))
            }
        }
    }
}

@OptIn(ExperimentalGlideComposeApi::class)
@Composable
fun SimilarItemCard(item: ZeneMusicData) {
    Column(Modifier
        .padding(7.dp)
        .combinedClickable(onLongClick = {
            NavigationUtils.triggerInfoSheet(item)
        }, onClick = {
            startMedia(item)
        })) {
        GlideImage(
            model = item.thumbnail,
            contentDescription = null,
            modifier = Modifier
                .fillMaxWidth()
                .height(180.dp)
                .clip(RoundedCornerShape(16.dp)),
            contentScale = ContentScale.Crop
        )
        Spacer(Modifier.height(6.dp))

        TextViewNormal(extractDate(item.artists.orEmpty()), 14, Color.Gray)
        TextViewSemiBold(removeLifestyleDate(item.artists.orEmpty()))
    }
}
