package com.rizwansayyed.zene.ui.main.ent

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.bumptech.glide.integration.compose.GlideImage
import com.rizwansayyed.zene.R
import com.rizwansayyed.zene.data.ResponseResult
import com.rizwansayyed.zene.data.model.WhoDatedWhoData
import com.rizwansayyed.zene.ui.theme.LoveBuzzBg
import com.rizwansayyed.zene.ui.view.CircularLoadingView
import com.rizwansayyed.zene.ui.view.ImageIcon
import com.rizwansayyed.zene.ui.view.ShimmerEffect
import com.rizwansayyed.zene.ui.view.TextViewSemiBold

@Composable
fun EntertainmentDatingView(viewModel: EntertainmentViewModel) {
    LazyVerticalGrid(
        GridCells.Fixed(2),
        contentPadding = PaddingValues(bottom = 250.dp, top = 10.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp),
    ) {
        when (val v = viewModel.dating) {
            ResponseResult.Empty -> {}
            is ResponseResult.Error -> {}
            ResponseResult.Loading -> {
                items(20) {
                    ShimmerEffect(
                        Modifier
                            .padding(horizontal = 5.dp)
                            .aspectRatio(1f)
                            .clip(RoundedCornerShape(16.dp))
                    )
                }
            }

            is ResponseResult.Success -> {
                items(v.data) { item ->
                    CoupleCardFull(item)
                }
            }
        }
    }


    LaunchedEffect(Unit) {
        viewModel.entDating()
    }
}

@Composable
fun CoupleCardFull(dated: WhoDatedWhoData) {
    Card(
        modifier = Modifier
            .padding(horizontal = 7.dp)
            .fillMaxWidth()
            .clickable { },
        shape = RoundedCornerShape(28.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 6.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .background(
                    Brush.verticalGradient(
                        colors = listOf(
                            LoveBuzzBg, LoveBuzzBg.copy(alpha = 0.85f)
                        )
                    )
                )
                .padding(vertical = 22.dp, horizontal = 16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            OverlappingAvatars(dated)

            Spacer( Modifier.height(18.dp))

            TextViewSemiBold(dated.coupleComparison?.personA?.name ?: "", size = 14, line = 1)
            TextViewSemiBold("&", size = 14, line = 1)
            TextViewSemiBold(dated.coupleComparison?.personB?.name ?: "", size = 14, line = 1)
        }
    }
}

@OptIn(ExperimentalGlideComposeApi::class)
@Composable
fun OverlappingAvatars(data: WhoDatedWhoData) {
    Box(Modifier.height(80.dp), Alignment.Center) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy((-18).dp)
        ) {
            DatingAvatar(data.coupleComparison?.personA?.image)
            DatingAvatar(data.coupleComparison?.personB?.image)
        }

        Box(
            Modifier
                .size(30.dp)
                .background(data.relationshipBadge()?.color ?: Color.Red, CircleShape)
                .border(2.dp, Color.White, CircleShape), contentAlignment = Alignment.Center
        ) {
            ImageIcon(
                data.relationshipBadge()?.icon ?: R.drawable.ic_romance_couple, 16, Color.White
            )
        }
    }
}

@OptIn(ExperimentalGlideComposeApi::class)
@Composable
private fun DatingAvatar(image: String?) {
    GlideImage(
        model = image ?: "",
        contentDescription = null,
        contentScale = ContentScale.Crop,
        modifier = Modifier
            .size(72.dp)
            .border(2.dp, Color.White, CircleShape)
            .clip(CircleShape)
    )
}