package com.rizwansayyed.zene.ui.view

import android.graphics.BitmapFactory
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImage
import com.rizwansayyed.zene.R
import com.rizwansayyed.zene.data.api.APIResponse
import com.rizwansayyed.zene.data.api.model.MusicType.ALBUMS
import com.rizwansayyed.zene.data.api.model.MusicType.SONGS
import com.rizwansayyed.zene.data.api.model.MusicType.VIDEO
import com.rizwansayyed.zene.data.api.model.ZeneMusicDataItems
import com.rizwansayyed.zene.ui.playlists.view.RemovePlaylistDialog
import com.rizwansayyed.zene.utils.Utils.addSongToLast
import com.rizwansayyed.zene.utils.Utils.addSongToNext
import com.rizwansayyed.zene.utils.Utils.loadBitmap
import com.rizwansayyed.zene.viewmodel.HomeViewModel
import com.rizwansayyed.zene.viewmodel.ZeneViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.net.URL

@Composable
fun TopInfoItemSheet(m: ZeneMusicDataItems) {
    Row(
        Modifier
            .fillMaxWidth()
            .padding(horizontal = 14.dp),
        Arrangement.Center,
        Alignment.CenterVertically
    ) {
        Column(
            Modifier
                .weight(1f)
                .padding(end = 9.dp)
        ) {
            TextPoppins(m.name ?: "", false, size = 24, lineHeight = 30)

            if (m.type() == SONGS || m.type() == ALBUMS || m.type() == VIDEO) {
                Spacer(Modifier.height(3.dp))

                TextPoppinsThin(m.artists ?: "", false, size = 15)
            }
        }

        AsyncImage(
            imgBuilder(m.thumbnail),
            m.name,
            Modifier
                .size(140.dp)
                .clip(RoundedCornerShape(13.dp)),
            contentScale = ContentScale.Crop
        )

    }
}

@Composable
fun SongInfoItemSheet(m: ZeneMusicDataItems, close: () -> Unit) {
    val addToPlaylistSongs by remember { mutableStateOf(false) }

    SheetDialogSheet(R.drawable.ic_play, R.string.play) {
        openSpecificIntent(m, listOf(m))
        close()
    }

    Spacer(Modifier.height(20.dp))

    SheetDialogSheet(R.drawable.ic_next, R.string.play_next) {
        addSongToNext(m)
        close()
    }

    Spacer(Modifier.height(20.dp))

    SheetDialogSheet(R.drawable.ic_play_queue, R.string.add_in_queue) {
        addSongToLast(m)
        close()
    }

    Spacer(Modifier.height(20.dp))

    SheetDialogSheet(R.drawable.ic_add_playlist, R.string.add_to_playlist) {

    }


    if (addToPlaylistSongs) AddSongToPlaylist()
}

@Composable
fun AddSongToPlaylist() {

}

@Composable
fun AlbumPlaylistInfoItemSheet(m: ZeneMusicDataItems, close: () -> Unit) {
    val zeneViewModel: ZeneViewModel = hiltViewModel()
    val homeViewModel: HomeViewModel = hiltViewModel()

    var removeDialog by remember { mutableStateOf(false) }

    when (val v = homeViewModel.albumPlaylistData) {
        APIResponse.Empty -> {}
        is APIResponse.Error -> {}
        APIResponse.Loading -> LoadingText()
        is APIResponse.Success -> {
            val i = v.data
            if (i.isAdded == true)
                SheetDialogSheet(R.drawable.ic_tick, R.string.added_to_library) {
                    removeDialog = true
                }
            else
                SheetDialogSheet(R.drawable.ic_folder_library, R.string.add_to_library) {
                    CoroutineScope(Dispatchers.IO).launch {
                        val image = loadBitmap(i.info?.thumbnail.toString())
                        zeneViewModel
                            .createNewPlaylist(i.info?.name ?: "", image, i.info?.id)
                        close()
                    }
                }

            if (removeDialog) RemovePlaylistDialog {
                if (it) {
                    i.info?.id?.let { it1 -> zeneViewModel.deletePlaylists(it1) }
                }
                removeDialog = false
            }
        }
    }

    LaunchedEffect(Unit) {
        homeViewModel.playlistsData(m.id ?: "")
    }
}