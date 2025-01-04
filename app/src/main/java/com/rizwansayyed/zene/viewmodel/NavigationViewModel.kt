package com.rizwansayyed.zene.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.rizwansayyed.zene.ui.main.home.HomeSectionSelector

class NavigationViewModel : ViewModel() {
    var homeSection by mutableStateOf(HomeSectionSelector.MUSIC)

    fun setHomeSections(v: HomeSectionSelector) {
        homeSection = v
    }
}