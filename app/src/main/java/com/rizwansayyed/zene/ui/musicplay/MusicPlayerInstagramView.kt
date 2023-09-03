package com.rizwansayyed.zene.ui.musicplay

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyHorizontalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.rizwansayyed.zene.BaseApplication
import com.rizwansayyed.zene.R
import com.rizwansayyed.zene.presenter.SongsViewModel
import com.rizwansayyed.zene.presenter.model.ApiResponse
import com.rizwansayyed.zene.presenter.model.ApiResponse.*
import com.rizwansayyed.zene.ui.artists.view.InstagramPostsPosts
import com.rizwansayyed.zene.ui.artists.view.InstagramPostsPostsBig
import com.rizwansayyed.zene.utils.QuickSandSemiBold
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking

@Composable
fun MusicPlayerInstagram(songs: SongsViewModel = hiltViewModel()) {

    val musicPlayer by BaseApplication.dataStoreManager.musicPlayerData
        .collectAsState(initial = runBlocking(Dispatchers.IO) { BaseApplication.dataStoreManager.musicPlayerData.first() })

    when (songs.artistsInstagram.response) {
        SUCCESS -> {
            if (songs.artistsInstagram.instagram?.isEmpty() == true)
                QuickSandSemiBold(
                    stringResource(id = R.string.no_instagram_account_found),
                    size = 17,
                    modifier = Modifier.padding(50.dp)
                )
            else {
                Spacer(Modifier.height(30.dp))

                songs.artistsInstagram.instagram?.forEach {
                    QuickSandSemiBold("${it.name} (@${it.username})", Modifier.padding(5.dp), size = 19)

                    LazyRow {
                        items(it.posts!!) { p ->
                            InstagramPostsPostsBig(p)
                        }
                    }

                    Spacer(Modifier.height(30.dp))
                }
            }


        }

        LOADING -> Row(
            modifier = Modifier
                .padding(50.dp)
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            CircularProgressIndicator(Modifier.size(30.dp), Color.White)
        }

        ERROR ->
            QuickSandSemiBold(stringResource(id = R.string.error_loading_instagram), size = 17)

    }


    LaunchedEffect(Unit) {
        songs.artistsInstagram(musicPlayer?.artists)
    }

}