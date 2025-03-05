package com.rizwansayyed.zene.ui.view

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.jakewharton.processphoenix.ProcessPhoenix
import com.rizwansayyed.zene.data.ResponseResult
import com.rizwansayyed.zene.data.model.ZeneMusicData
import com.rizwansayyed.zene.utils.NavigationUtils
import com.rizwansayyed.zene.utils.NavigationUtils.NAV_MAIN_PAGE
import com.rizwansayyed.zene.viewmodel.HomeViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlin.time.Duration.Companion.seconds

@Composable
fun ArtistsView(artistsID: String) {
    val viewModel: HomeViewModel = hiltViewModel()
    val context = LocalContext.current

    when (val v = viewModel.artistsInfo) {
        ResponseResult.Empty -> {}
        is ResponseResult.Error -> {}
        ResponseResult.Loading -> {
            Box(Modifier.fillMaxSize(), Alignment.Center) {
                CircularLoadingView()
            }
        }

        is ResponseResult.Success -> {
            LazyColumn(Modifier.fillMaxSize()) {
                item {
                    Spacer(Modifier.height(14.dp))
                    TopArtistsInfoView(v.data.data)
                }
            }
        }
    }


    LaunchedEffect(Unit) {
        if (artistsID.length <= 3) {
            NavigationUtils.triggerHomeNav(NAV_MAIN_PAGE)
            return@LaunchedEffect
        }

        if (viewModel.artistsInfo !is ResponseResult.Success)
            viewModel.artistsInfo(artistsID) {
                CoroutineScope(Dispatchers.IO).launch {
                    delay(5.seconds)
                    ProcessPhoenix.triggerRebirth(context)
                }
            }
    }
}

@Composable
fun TopArtistsInfoView(info: ZeneMusicData?) {
    Row { }
}