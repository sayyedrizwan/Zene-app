package com.rizwansayyed.zene.ui.view.movies.view

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.rizwansayyed.zene.R
import com.rizwansayyed.zene.data.model.MoviesTvShowResponse
import com.rizwansayyed.zene.ui.view.TextViewBold
import com.rizwansayyed.zene.ui.view.TextViewNormal
import com.rizwansayyed.zene.ui.view.TextViewSemiBold
import com.rizwansayyed.zene.utils.URLSUtils.getSearchOnGoogle
import com.rizwansayyed.zene.utils.share.MediaContentUtils

@Composable
fun MoviesCastInfoView(data: MoviesTvShowResponse) {
    val screenWidth = LocalConfiguration.current.screenWidthDp.dp

    if (data.credits?.isNotEmpty() == true) {
        Spacer(Modifier.height(50.dp))
        Box(
            Modifier
                .padding(horizontal = 6.dp)
                .fillMaxWidth()
        ) {
            TextViewBold(stringResource(R.string.cast_crew), 23)
        }
        Spacer(Modifier.height(12.dp))
        LazyRow(Modifier.fillMaxWidth()) {
            items(data.credits) {
                Column(
                    Modifier
                        .widthIn(max = screenWidth * 0.8f)
                        .clickable {
                            it?.name?.let { v ->
                                MediaContentUtils.openCustomBrowser(getSearchOnGoogle(v))
                            }
                        }
                        .padding(8.dp)
                        .clip(RoundedCornerShape(14.dp))
                        .background(Color.DarkGray.copy(0.3f))
                        .padding(17.dp)
                ) {
                    TextViewSemiBold(it?.characterName ?: "", 20)
                    Spacer(Modifier.height(5.dp))
                    TextViewNormal(it?.name ?: "", 15)
                    Spacer(Modifier.height(5.dp))
                    TextViewNormal(it?.role ?: "", 15)
                }
            }
        }
    }
}