package com.rizwansayyed.zene.ui.home.homeui

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
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
        horizontalArrangement = Arrangement.SpaceBetween
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

            AnimatedVisibility(visible = homeNavViewModel.homeNavigationView.value == HomeNavigationStatus.MAIN) {
                Spacer(
                    modifier = Modifier
                        .padding(bottom = 10.dp)
                        .clip(RoundedCornerShape(100))
                        .background(Color.White)
                        .size(5.dp)
                )
            }
        }


        AsyncImage(
            "https://i.scdn.co/image/ab67616d00001e02f5dc36d5000145375a41c3b8",
            contentDescription = "",
            modifier = Modifier
                .padding(horizontal = 30.dp, vertical = 10.dp)
                .size(50.dp)
                .padding(7.dp)
                .clip(RoundedCornerShape(100))
        )

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
            AnimatedVisibility(visible = homeNavViewModel.homeNavigationView.value == HomeNavigationStatus.SETTINGS) {
                Spacer(
                    modifier = Modifier
                        .padding(bottom = 10.dp)
                        .clip(RoundedCornerShape(100))
                        .background(Color.White)
                        .size(5.dp)
                )
            }
        }
    }
}

