package com.rizwansayyed.zene.ui.home.homeui

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.AnimatedVisibilityScope
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImage
import com.rizwansayyed.zene.R
import com.rizwansayyed.zene.ui.home.homenavmodel.HomeNavViewModel
import com.rizwansayyed.zene.ui.home.homenavmodel.HomeNavigationStatus
import com.rizwansayyed.zene.ui.theme.Purple
import com.rizwansayyed.zene.utils.Utils.showToast

@Composable
fun HomeNavBar(modifier: Modifier, homeNavViewModel: HomeNavViewModel = hiltViewModel()) {

    Row(
        modifier
            .padding(10.dp)
            .padding(bottom = 36.dp)
            .clip(shape = RoundedCornerShape(13.dp))
            .background(Purple),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Image(
                painterResource(id = R.drawable.ic_home_nav),
                contentDescription = "",
                modifier = Modifier
                    .padding(horizontal = 30.dp)
                    .padding(top = 10.dp)
                    .size(50.dp)
                    .padding(7.dp)
                    .clickable {
                        homeNavViewModel.homeNavigationView(HomeNavigationStatus.MAIN)
                    },
                colorFilter = ColorFilter.tint(Color.White)
            )
            Box {
                SpacerDots(Color.Transparent)

                this@Column.AnimatedVisibility(visible = homeNavViewModel.homeNavigationView.value == HomeNavigationStatus.MAIN) {
                    SpacerDots()
                }
            }
        }

        Box {
            AnimationSliderVertical(!homeNavViewModel.showMusicPlayerView.value, Alignment.Top) {
                AsyncImage(
                    "https://i.scdn.co/image/ab67616d00001e02f5dc36d5000145375a41c3b8",
                    contentDescription = "",
                    modifier = Modifier
                        .padding(horizontal = 30.dp, vertical = 10.dp)
                        .size(58.dp)
                        .padding(7.dp)
                        .clip(RoundedCornerShape(100))
                        .clickable {
                            if (homeNavViewModel.showMusicPlayerView.value)
                                homeNavViewModel.hideMusicPlayer()
                            else
                                homeNavViewModel.showMusicPlayer()
                        },
                    contentScale = ContentScale.Crop
                )
            }

            AnimationSliderVertical(homeNavViewModel.showMusicPlayerView.value, Alignment.Bottom) {
                Image(
                    painterResource(id = R.drawable.ic_arrow_down),
                    contentDescription = "",
                    modifier = Modifier
                        .padding(horizontal = 30.dp, vertical = 10.dp)
                        .size(58.dp)
                        .padding(7.dp)
                        .clip(RoundedCornerShape(100))
                        .clickable {
                            if (homeNavViewModel.showMusicPlayerView.value)
                                homeNavViewModel.hideMusicPlayer()
                            else
                                homeNavViewModel.showMusicPlayer()
                        },
                    colorFilter = ColorFilter.tint(Color.White)
                )
            }
        }


        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Image(
                painterResource(id = R.drawable.ic_setting_nav),
                contentDescription = "",
                modifier = Modifier
                    .padding(horizontal = 30.dp)
                    .padding(top = 10.dp)
                    .size(50.dp)
                    .padding(7.dp)
                    .clickable {
                        homeNavViewModel.homeNavigationView(HomeNavigationStatus.SETTINGS)
                    },
                colorFilter = ColorFilter.tint(Color.White)
            )
            Box {
                SpacerDots(Color.Transparent)

                this@Column.AnimatedVisibility(visible = homeNavViewModel.homeNavigationView.value == HomeNavigationStatus.SETTINGS) {
                    SpacerDots()
                }
            }
        }
    }
}


@Composable
fun AnimationSliderVertical(
    b: Boolean,
    state: Alignment.Vertical,
    content: @Composable() AnimatedVisibilityScope.() -> Unit
) {
    val density = LocalDensity.current
    AnimatedVisibility(
        b,
        enter = slideInVertically { with(density) { -40.dp.roundToPx() } } + expandVertically(
            expandFrom = state
        ) + fadeIn(initialAlpha = 0.3f),
        exit = slideOutVertically() + shrinkVertically() + fadeOut(),
    ) {
        content()
    }
}

@Composable
fun SpacerDots(color: Color = Color.White) {
    Spacer(
        modifier = Modifier
            .padding(bottom = 10.dp)
            .clip(RoundedCornerShape(100))
            .background(color)
            .size(5.dp)
    )
}

