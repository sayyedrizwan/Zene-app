package com.rizwansayyed.zene.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.rizwansayyed.zene.ui.main.home.HomeNavSelector
import com.rizwansayyed.zene.ui.main.home.HomeSectionSelector

class NavigationViewModel : ViewModel() {
    var homeSection by mutableStateOf(HomeSectionSelector.MUSIC)
    var homeNavSection by mutableStateOf(HomeNavSelector.HOME)

    fun setHomeSections(v: HomeSectionSelector) {
        homeSection = v
    }

    fun setHomeNavSections(v: HomeNavSelector) {
        homeNavSection = v
    }
}