package com.rizwansayyed.zene.ui.main.ent

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.rizwansayyed.zene.ui.main.ent.discoverview.EntCelebDatingView
import com.rizwansayyed.zene.ui.main.ent.discoverview.EntCelebStoriesView
import com.rizwansayyed.zene.ui.main.ent.discoverview.EntDiscoverTopView
import com.rizwansayyed.zene.ui.main.ent.discoverview.EntLatestTrailerView
import com.rizwansayyed.zene.ui.main.ent.discoverview.EntLifestyleView
import com.rizwansayyed.zene.ui.main.ent.discoverview.EntTrendingMoviesView
import com.rizwansayyed.zene.ui.main.ent.discoverview.EntTrendingTopicsView
import com.rizwansayyed.zene.ui.main.ent.discoverview.NewsRow
import com.rizwansayyed.zene.ui.main.ent.discoverview.ViewAllButton
import com.rizwansayyed.zene.ui.main.ent.discoverview.sampleNews

data class StoryItem(
    val name: String, val imageUrl: String, val isLive: Boolean = false
)

object CelebrityImages {
    const val ZENDAYA = "https://wallpapercave.com/wp/wp11967293.jpg"
    const val TAYLOR_SWIFT = "https://wallpapercave.com/wp/wp14806034.webp"
    const val HARRY_STYLES = "https://wallpapercave.com/wp/wp2368497.jpg"
}

@Composable
fun EntertainmentDiscoverView() {
    val listState = rememberLazyListState()
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black),
        contentPadding = PaddingValues(bottom = 250.dp)
    ) {
        item(key = "top") { EntDiscoverTopView() }

        item(key = "stories") { EntCelebStoriesView() }

        item(key = "trending") { EntTrendingTopicsView() }

        item(key = "dating") { EntCelebDatingView() }

        item(key = "movies") { EntTrendingMoviesView() }

        item(key = "trailer") { EntLatestTrailerView() }

        item(key = "lifestyle") { EntLifestyleView() }

        items(sampleNews, key = { it.title }) { item ->
            NewsRow(item)
            Spacer(modifier = Modifier.height(18.dp))
        }

        item(key = "view_all_buzz") {
            ViewAllButton {

            }
        }
    }
}