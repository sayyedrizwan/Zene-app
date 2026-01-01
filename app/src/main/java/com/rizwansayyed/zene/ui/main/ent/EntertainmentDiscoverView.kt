package com.rizwansayyed.zene.ui.main.ent

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.rizwansayyed.zene.ui.main.ent.discoverview.EntCelebDatingView
import com.rizwansayyed.zene.ui.main.ent.discoverview.EntCelebStoriesView
import com.rizwansayyed.zene.ui.main.ent.discoverview.EntDiscoverTopView
import com.rizwansayyed.zene.ui.main.ent.discoverview.EntLatestTrailerView
import com.rizwansayyed.zene.ui.main.ent.discoverview.EntTrendingMoviesView
import com.rizwansayyed.zene.ui.main.ent.discoverview.EntTrendingTopicsView

data class StoryItem(
    val name: String, val imageUrl: String, val isLive: Boolean = false
)

object CelebrityImages {
    const val ZENDAYA = "https://wallpapercave.com/wp/wp11967293.jpg"

    const val TAYLOR_SWIFT = "https://wallpapercave.com/wp/wp14806034.webp"

    const val HARRY_STYLES = "https://wallpapercave.com/wp/wp2368497.jpg"

    const val PROFILE = "https://randomuser.me/api/portraits/women/44.jpg"
}

@Composable
fun EntertainmentDiscoverView() {
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black)
    ) {
        item {
            EntDiscoverTopView()
        }
        item {
            EntCelebStoriesView()
        }
        item {
            EntTrendingTopicsView()
        }

        item {
            EntCelebDatingView()
        }

        item {
            EntTrendingMoviesView()
        }

        item {
            EntLatestTrailerView()
        }

        item {
            Spacer(Modifier.height(800.dp))
        }
    }
}