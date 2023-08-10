package com.rizwansayyed.zene.ui.home.homeui

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.rizwansayyed.zene.BaseApplication.Companion.dataStoreManager
import com.rizwansayyed.zene.presenter.model.AlbumsHeadersResponse
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking

@Composable
fun HomepageView() {
    val header by dataStoreManager.albumHeaderData.collectAsState(initial = runBlocking(Dispatchers.IO) { dataStoreManager.albumHeaderData.first() })

    LazyColumn {
        item {
            header?.let { TopHeaderGrid(it) }
        }


    }
}


@OptIn(ExperimentalFoundationApi::class)
@Composable
fun TopHeaderGrid(header: AlbumsHeadersResponse) {
    val screenHeight = LocalConfiguration.current.screenHeightDp.dp

    HorizontalPager(pageCount = header.header?.size ?: 0) {
        AsyncImage(
            model = header.header?.get(it)?.thumbnail,
            contentDescription = null,
            contentScale = ContentScale.Crop,
            modifier = Modifier.fillMaxWidth().height(screenHeight / 2 + 140.dp)
        )
    }
}