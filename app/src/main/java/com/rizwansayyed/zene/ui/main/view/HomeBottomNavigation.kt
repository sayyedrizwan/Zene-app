package com.rizwansayyed.zene.ui.main.view

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
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
import com.rizwansayyed.zene.utils.NavigationUtils
import com.rizwansayyed.zene.utils.NavigationUtils.NAV_MAIN_PAGE
import com.rizwansayyed.zene.utils.NavigationUtils.NAV_SETTINGS_PAGE
import com.rizwansayyed.zene.viewmodel.NavigationViewModel

@Composable
fun HomeBottomNavigationView(
    modifier: Modifier = Modifier, vm: NavigationViewModel
) {
    Row(
        modifier
            .padding(bottom = 40.dp)
            .fillMaxWidth()
            .background(Color.Black)
            .padding(vertical = 17.dp, horizontal = 5.dp),
        Arrangement.Absolute.SpaceBetween,
        Alignment.CenterVertically
    ) {
        HomeBottomNavItems(R.drawable.ic_home, R.string.home, HomeNavSelector.HOME, vm)
        HomeBottomNavItems(R.drawable.ic_hotspot, R.string.connect, HomeNavSelector.CONNECT, vm)
        HomeBottomNavItems(R.drawable.ic_search, R.string.search, HomeNavSelector.SEARCH, vm)
        HomeBottomNavItems(R.drawable.ic_audio_book, R.string.ent_, HomeNavSelector.ENT, vm)
    }
}

@Composable
fun HomeBottomNavItems(icon: Int, txt: Int, nav: HomeNavSelector, vm: NavigationViewModel) {
    Column(Modifier
        .padding(horizontal = 5.dp)
        .clickable {
            NavigationUtils.triggerHomeNav(NAV_MAIN_PAGE)
            vm.setHomeNavSections(nav)
        }
        .padding(horizontal = 15.dp), Arrangement.Center, Alignment.CenterHorizontally) {
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