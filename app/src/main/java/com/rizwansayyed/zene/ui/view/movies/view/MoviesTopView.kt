package com.rizwansayyed.zene.ui.view.movies.view

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
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
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.bumptech.glide.integration.compose.GlideImage
import com.rizwansayyed.zene.R
import com.rizwansayyed.zene.data.model.MoviesTvShowResponse
import com.rizwansayyed.zene.ui.main.view.share.ShareDataView
import com.rizwansayyed.zene.ui.theme.MainColor
import com.rizwansayyed.zene.ui.view.ButtonWithBorder
import com.rizwansayyed.zene.ui.view.ImageIcon
import com.rizwansayyed.zene.ui.view.TextViewBold
import com.rizwansayyed.zene.ui.view.TextViewBoldBig
import com.rizwansayyed.zene.ui.view.TextViewBorder
import com.rizwansayyed.zene.ui.view.TextViewNormal
import com.rizwansayyed.zene.ui.view.TextViewSemiBold

@OptIn(ExperimentalGlideComposeApi::class)
@Composable
fun MoviesTopView(data: MoviesTvShowResponse) {
    var showShareView by remember { mutableStateOf(false) }
    var showPlayToView by remember { mutableStateOf(false) }

    Box(Modifier.fillMaxWidth()) {
        GlideImage(
            data.poster,
            data.name,
            Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(bottomStart = 14.dp, bottomEnd = 14.dp)),
            contentScale = ContentScale.Crop
        )

        Row(modifier = Modifier
            .padding(bottom = 15.dp, start = 5.dp)
            .align(Alignment.BottomStart)
            .padding(horizontal = 10.dp)
            .clickable { showPlayToView = true }
            .clip(RoundedCornerShape(18.dp))
            .background(Color.White)
            .padding(vertical = 10.dp, horizontal = 25.dp),
            Arrangement.Center,
            Alignment.CenterVertically) {
            ImageIcon(R.drawable.ic_play_filled, 18, Color.Black)
            Spacer(Modifier.width(10.dp))
            TextViewBold(stringResource(R.string.watch), 17, Color.Black, center = false)
        }

        Row(modifier = Modifier
            .padding(bottom = 15.dp, start = 5.dp)
            .align(Alignment.BottomEnd)
            .padding(horizontal = 10.dp)
            .clickable { showShareView = true }
            .clip(RoundedCornerShape(18.dp))
            .background(Color.White)
            .padding(vertical = 10.dp, horizontal = 15.dp),
            Arrangement.Center, Alignment.CenterVertically) {
            ImageIcon(R.drawable.ic_share, 24, Color.Black)
        }
    }


    if (showShareView) ShareDataView(data.asMusicData()) {
        showShareView = false
    }
}

@Composable
fun MoviesCategories(data: MoviesTvShowResponse) {
    Spacer(Modifier.height(24.dp))
    Column(Modifier.fillMaxWidth()) {
        if (data.rank != null) {
            if (data.rank in 1..50) {
                TextViewBorder("${stringResource(R.string.on_trending)} #${data.rank}", MainColor) {

                }
            } else if (data.rank in 51..100) {
                TextViewBorder("#${data.rank}", MainColor) {

                }
            }
        }

        Column(Modifier.padding(top = 20.dp, start = 10.dp)) {
            TextViewBoldBig(data.name ?: "", 45)
            Spacer(Modifier.height(2.dp))
            TextViewSemiBold(data.type ?: "", 15)
            Spacer(Modifier.height(5.dp))
            TextViewNormal(data.description ?: "", 15)
        }

        if (data.isReleased == false) {
            Spacer(Modifier.height(29.dp))
            if (data.releaseOn != null) TextViewBorder(
                "${stringResource(R.string.releasing_on)} ${data.readReleaseDate()}", Color.Red
            ) {}
            else TextViewBorder(stringResource(R.string.releasing_soon), Color.Red) {}

        }

        Spacer(Modifier.height(29.dp))

        Row(
            Modifier
                .padding(start = 10.dp)
                .horizontalScroll(rememberScrollState())
                .fillMaxWidth()
        ) {
            TextViewNormal(data.release.toString(), 18)
            TextViewNormal(" • ", 18)
            TextViewNormal(data.readRuntime(), 18)
            if ((data.certification ?: "").trim().isNotEmpty()) {
                TextViewNormal(" • ", 18)
                TextViewNormal(data.certification.toString(), 18)
            }
        }

        if (data.categories?.split(" , ")?.isNotEmpty() == true) {
            Spacer(Modifier.height(29.dp))
            Row(
                Modifier
                    .horizontalScroll(rememberScrollState())
                    .fillMaxWidth()
            ) {
                data.categories.split(" , ").forEach {
                    ButtonWithBorder(it)
                }
            }
        }
    }
}