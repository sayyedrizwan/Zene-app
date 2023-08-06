package com.rizwansayyed.zene.ui.home.homenavmodel

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel

class HomeNavViewModel : ViewModel() {

    var homeNavigationView = mutableStateOf(HomeNavigationStatus.MAIN)
        private set

    fun homeNavigationView(nav: HomeNavigationStatus) {
        homeNavigationView.value = nav
    }
}