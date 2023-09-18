package com.rizwansayyed.zene.presenter.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.hilt.navigation.compose.hiltViewModel
import com.rizwansayyed.zene.presenter.theme.DarkBlack
import com.rizwansayyed.zene.presenter.ui.home.offline.OfflineSongsView
import com.rizwansayyed.zene.presenter.ui.home.online.OnlineSongsView
import com.rizwansayyed.zene.viewmodel.HomeNavViewModel


@Composable
fun MainHomePageView() {
    val nav : HomeNavViewModel = hiltViewModel()

    val columnModifier = Modifier
        .fillMaxSize()
        .verticalScroll(rememberScrollState())
        .background(if (isSystemInDarkTheme()) DarkBlack else Color.White)

    Column(columnModifier) {
        if (nav.isOnline.value)
            OnlineSongsView()
         else
            OfflineSongsView()
    }
}