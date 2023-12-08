package com.rizwansayyed.zene.presenter.ui.home.views

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.rizwansayyed.zene.data.db.datastore.DataStorageSettingsManager.userAuthData
import com.rizwansayyed.zene.presenter.theme.DarkGreyColor
import com.rizwansayyed.zene.presenter.ui.home.mymusic.HistoryItemsList
import com.rizwansayyed.zene.presenter.ui.home.mymusic.LinkedToBrowser
import com.rizwansayyed.zene.presenter.ui.home.mymusic.MyMusicPlaylistsList
import com.rizwansayyed.zene.presenter.ui.home.mymusic.TopMyMusicHeader
import com.rizwansayyed.zene.presenter.util.UiUtils.GridSpan.TOTAL_ITEMS_GRID
import com.rizwansayyed.zene.viewmodel.MyMusicViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking

@Composable
fun MyMusicView() {
    val myMusic: MyMusicViewModel = hiltViewModel()
    val userAuth by userAuthData.collectAsState(initial = runBlocking(Dispatchers.IO) { userAuthData.first() })

    LazyVerticalGrid(
        GridCells.Fixed(TOTAL_ITEMS_GRID), Modifier
            .fillMaxSize()
            .background(DarkGreyColor)
    ) {
        item(span = { GridItemSpan(TOTAL_ITEMS_GRID) }) {
            Column(Modifier.padding(horizontal = 9.dp), Arrangement.Center) {
                TopMyMusicHeader(userAuth)

                Spacer(Modifier.height(22.dp))
            }
        }

        item(span = { GridItemSpan(TOTAL_ITEMS_GRID) }) {
            LinkedToBrowser(userAuth)
        }
        item(span = { GridItemSpan(TOTAL_ITEMS_GRID) }) {
            HistoryItemsList(myMusic)
        }
        item(span = { GridItemSpan(TOTAL_ITEMS_GRID) }) {
            MyMusicPlaylistsList(myMusic)
        }
        item(span = { GridItemSpan(TOTAL_ITEMS_GRID) }) {
            Spacer(Modifier.height(150.dp))
        }
    }

    LaunchedEffect(Unit) {
        myMusic.init()
    }
}