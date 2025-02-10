package com.rizwansayyed.zene.ui.main.view

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
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.rizwansayyed.zene.R
import com.rizwansayyed.zene.ui.main.home.HomeNavSelector
import com.rizwansayyed.zene.ui.view.ImageIcon
import com.rizwansayyed.zene.ui.view.TextViewSemiBold
import com.rizwansayyed.zene.viewmodel.NavigationViewModel

@Composable
fun HomeBottomNavigationView(
    modifier: Modifier = Modifier, vm: NavigationViewModel
) {
    Box(
        modifier
            .padding(bottom = 40.dp)
            .fillMaxWidth()
            .background(Color.Black)
            .padding(vertical = 20.dp)
    ) {
        Row(
            Modifier
                .fillMaxWidth()
                .horizontalScroll(rememberScrollState())
        ) {
            HomeBottomNavItems(R.drawable.ic_home, R.string.home, HomeNavSelector.HOME, vm)
            HomeBottomNavItems(R.drawable.ic_hotspot, R.string.connect, HomeNavSelector.CONNECT, vm)
            HomeBottomNavItems(R.drawable.ic_search, R.string.search, HomeNavSelector.SEARCH, vm)
            HomeBottomNavItems(R.drawable.ic_diamond, R.string.ai, HomeNavSelector.AI, vm)
            HomeBottomNavItems(
                R.drawable.ic_audio_book, R.string.entertainment_short, HomeNavSelector.ENT, vm
            )
            HomeBottomNavItems(
                R.drawable.ic_setting, R.string.settings, HomeNavSelector.SETTINGS, vm
            )
            Spacer(Modifier.width(30.dp))
        }
    }
}

@Composable
fun HomeBottomNavItems(icon: Int, txt: Int, nav: HomeNavSelector, vm: NavigationViewModel) {
    Column(Modifier
        .padding(horizontal = 10.dp)
        .clickable { vm.setHomeNavSections(nav) }
        .padding(horizontal = 10.dp), Arrangement.Center, Alignment.CenterHorizontally) {
        if (vm.homeNavSection == nav) {
            ImageIcon(icon, 25)
            Spacer(Modifier.height(4.dp))
            TextViewSemiBold(stringResource(txt), 14, line = 1)
        } else {
            ImageIcon(icon, 25, Color.Gray)
            Spacer(Modifier.height(4.dp))
            TextViewSemiBold(stringResource(txt), 14, Color.Gray, line = 1)
        }
    }
}