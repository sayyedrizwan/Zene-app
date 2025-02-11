package com.rizwansayyed.zene.ui.main.home.view

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
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
import com.rizwansayyed.zene.data.model.UserInfoResponse
import com.rizwansayyed.zene.ui.main.home.HomeNavSelector
import com.rizwansayyed.zene.ui.main.home.HomeSectionSelector
import com.rizwansayyed.zene.ui.theme.MainColor
import com.rizwansayyed.zene.ui.view.ImageIcon
import com.rizwansayyed.zene.ui.view.TextViewSemiBold
import com.rizwansayyed.zene.viewmodel.NavigationViewModel

@OptIn(ExperimentalGlideComposeApi::class)
@Composable
fun HomeScreenTopView(viewModel: NavigationViewModel, userInfo: UserInfoResponse?) {
    Row(
        Modifier
            .horizontalScroll(rememberScrollState())
            .padding(top = 60.dp)
            .fillMaxWidth(),
        Arrangement.Center,
        Alignment.CenterVertically
    ) {
        Spacer(Modifier.width(10.dp))
        Box(Modifier.clickable { viewModel.setHomeNavSections(HomeNavSelector.SETTINGS) }) {
            GlideImage(
                userInfo?.photo,
                userInfo?.name,
                Modifier
                    .clip(RoundedCornerShape(100))
                    .size(40.dp),
                contentScale = ContentScale.Crop
            )
            Box(
                Modifier
                    .align(Alignment.BottomEnd)
                    .offset(y = 4.dp)
            ) {
                ImageIcon(R.drawable.ic_setting, 17)
            }
        }
        Spacer(Modifier.width(3.dp))
        TextSimpleCards(
            viewModel.homeSection == HomeSectionSelector.MUSIC, stringResource(R.string.music)
        ) {
            viewModel.setHomeSections(HomeSectionSelector.MUSIC)
        }

        TextSimpleCards(
            viewModel.homeSection == HomeSectionSelector.PODCAST, stringResource(R.string.podcasts)
        ) {
            viewModel.setHomeSections(HomeSectionSelector.PODCAST)
        }

        TextSimpleCards(
            viewModel.homeSection == HomeSectionSelector.VIDEO, stringResource(R.string.videos)
        ) {
            viewModel.setHomeSections(HomeSectionSelector.VIDEO)
        }

        TextSimpleCards(
            viewModel.homeSection == HomeSectionSelector.AI_MUSIC, stringResource(R.string.ai_music)
        ) {
            viewModel.setHomeSections(HomeSectionSelector.AI_MUSIC)
        }

        TextSimpleCards(
            viewModel.homeSection == HomeSectionSelector.RADIO, stringResource(R.string.radios)
        ) {
            viewModel.setHomeSections(HomeSectionSelector.RADIO)
        }

        TextSimpleCards(
            viewModel.homeSection == HomeSectionSelector.MY_LIBRARY,
            stringResource(R.string.my_library)
        ) {
            viewModel.setHomeSections(HomeSectionSelector.MY_LIBRARY)
        }

        Spacer(Modifier.width(15.dp))
    }
}

@Composable
fun TextSimpleCards(isActive: Boolean, txt: String, click: () -> Unit) {
    Row(Modifier
        .padding(horizontal = 8.dp)
        .clickable { click() }
        .clip(RoundedCornerShape(13.dp))
        .background(if (isActive) MainColor else Color.Black)
        .padding(horizontal = 14.dp, vertical = 3.dp)) {
        TextViewSemiBold(txt, 13)
    }
}