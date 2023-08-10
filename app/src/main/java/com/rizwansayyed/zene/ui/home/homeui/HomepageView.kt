package com.rizwansayyed.zene.ui.home.homeui

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.rizwansayyed.zene.BaseApplication.Companion.dataStoreManager
import com.rizwansayyed.zene.R
import com.rizwansayyed.zene.utils.QuickSandSemiBold
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking

@Composable
fun HomepageView() {
    val header by dataStoreManager.albumHeaderData.collectAsState(initial = runBlocking(Dispatchers.IO) { dataStoreManager.albumHeaderData.first() })
    val topArtists by dataStoreManager.topArtistsOfWeekData
        .collectAsState(initial = runBlocking(Dispatchers.IO) { dataStoreManager.topArtistsOfWeekData.first() })

    LazyColumn {
        item {
            header?.let { TopHeaderPager(it) }
        }
        item {
            TopHeaderOf(stringResource(id = R.string.top_artist_of_week))
            Spacer(modifier = Modifier.height(8.dp))
        }

        item {
            LazyRow {
                if (topArtists != null) items(topArtists!!) { artists ->
                    ArtistsView(artists)
                }
            }
        }

        item {
            Spacer(modifier = Modifier.height(100.dp))
        }
    }
}


@Composable
fun TopHeaderOf(v: String) {
    Spacer(modifier = Modifier.height(65.dp))

    QuickSandSemiBold(v, size = 19, modifier = Modifier.padding(5.dp))
}