package com.rizwansayyed.zene.ui.view.myplaylist

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.bumptech.glide.integration.compose.GlideImage
import com.rizwansayyed.zene.R
import com.rizwansayyed.zene.data.ResponseResult
import com.rizwansayyed.zene.datastore.DataStorageManager.musicPlayerDB
import com.rizwansayyed.zene.service.notification.NavigationUtils
import com.rizwansayyed.zene.service.notification.NavigationUtils.NAV_GO_BACK
import com.rizwansayyed.zene.ui.main.view.share.ShareDataView
import com.rizwansayyed.zene.ui.theme.MainColor
import com.rizwansayyed.zene.ui.view.CircularLoadingView
import com.rizwansayyed.zene.ui.view.ImageIcon
import com.rizwansayyed.zene.ui.view.MiniWithImageAndBorder
import com.rizwansayyed.zene.ui.view.ShimmerEffect
import com.rizwansayyed.zene.ui.view.TextAlertDialog
import com.rizwansayyed.zene.ui.view.TextViewBoldBig
import com.rizwansayyed.zene.ui.view.TextViewNormal
import com.rizwansayyed.zene.utils.MainUtils.toast
import com.rizwansayyed.zene.utils.RefreshPlaylistManager.refreshPlaylistState
import com.rizwansayyed.zene.utils.URLSUtils.LIKED_SONGS_ON_ZENE
import com.rizwansayyed.zene.utils.share.MediaContentUtils.startMedia
import com.rizwansayyed.zene.viewmodel.MyLibraryViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.cancel
import kotlinx.coroutines.delay
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import kotlin.time.Duration.Companion.seconds

@Composable
fun MyPlaylistView(id: String) {
    val myLibraryViewModel: MyLibraryViewModel = hiltViewModel(key = id)
    val playerInfo by musicPlayerDB.collectAsState(null)

    val state = rememberLazyListState()
    var isBottomTriggered by remember { mutableStateOf(false) }


    LazyColumn(
        Modifier
            .fillMaxSize()
            .background(Color.Black)
            .padding(horizontal = 5.dp),
        state,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        item { Spacer(Modifier.height(100.dp)) }

        if (id.contains(LIKED_SONGS_ON_ZENE)) {
            item { TopLikedView() }
        } else {
            item { MyPlaylistTopView(myLibraryViewModel) }
        }

        itemsIndexed(myLibraryViewModel.myPlaylistSongsList) { i, v ->
            MyPlaylistItemView(v, playerInfo, myLibraryViewModel) { status ->
                if (status) myLibraryViewModel.removeMyPlaylistItems(id, v, i)
            }
        }

        if (!myLibraryViewModel.myPlaylistSongsIsLoading && myLibraryViewModel.myPlaylistSongsList.isEmpty()) item {
            Spacer(Modifier.height(60.dp))
            TextViewNormal(stringResource(R.string.no_items_in_your_playlists), 15, center = true)
        }


        item {
            if (myLibraryViewModel.myPlaylistSongsIsLoading) CircularLoadingView()
        }

        item { Spacer(Modifier.height(300.dp)) }
    }


    LaunchedEffect(Unit) {
        myLibraryViewModel.myPlaylistSongsData(id)
        myLibraryViewModel.myPlaylistInfo(id)
    }


    LaunchedEffect(state) {
        snapshotFlow { state.layoutInfo }.collect { layoutInfo ->
            val lastVisibleItemIndex = layoutInfo.visibleItemsInfo.lastOrNull()?.index ?: 0
            val totalItemsCount = layoutInfo.totalItemsCount

            if (lastVisibleItemIndex >= totalItemsCount - 1 && !isBottomTriggered) {
                isBottomTriggered = true
                myLibraryViewModel.myPlaylistSongsData(id)
            } else if (lastVisibleItemIndex < totalItemsCount - 1) {
                isBottomTriggered = false
            }
        }
    }

    BackHandler {
        NavigationUtils.triggerHomeNav(NAV_GO_BACK)
    }
}

@Composable
fun TopLikedView() {
    Column(
        Modifier
            .fillMaxWidth()
            .padding(horizontal = 5.dp)
            .padding(bottom = 40.dp)
    ) {
        TextViewBoldBig(stringResource(R.string.liked_items), 55)
    }
}


@OptIn(ExperimentalGlideComposeApi::class)
@Composable
fun MyPlaylistTopView(myLibraryViewModel: MyLibraryViewModel) {
    var changeNameView by remember { mutableStateOf(false) }
    var changeImageView by remember { mutableStateOf(false) }
    var deleteView by remember { mutableStateOf(false) }
    var shareView by remember { mutableStateOf(false) }

    val coroutines = rememberCoroutineScope()

    Column(
        Modifier
            .fillMaxWidth()
            .padding(horizontal = 5.dp)
            .padding(bottom = 40.dp)
    ) {
        when (val v = myLibraryViewModel.playlistInfo) {
            ResponseResult.Empty -> {}
            is ResponseResult.Error -> {}
            ResponseResult.Loading -> {
                val width = (LocalConfiguration.current.screenWidthDp / 1.5).dp

                ShimmerEffect(
                    Modifier
                        .size(width)
                        .clip(RoundedCornerShape(14.dp))
                )

                Spacer(Modifier.height(12.dp))
                ShimmerEffect(
                    Modifier
                        .padding(horizontal = 3.dp)
                        .size(120.dp, 5.dp), durationMillis = 1000
                )
                Spacer(Modifier.height(6.dp))
                ShimmerEffect(
                    Modifier
                        .padding(horizontal = 3.dp)
                        .size(80.dp, 5.dp), durationMillis = 1000
                )
            }

            is ResponseResult.Success -> {
                GlideImage(
                    v.data.thumbnail,
                    v.data.name,
                    Modifier
                        .size(250.dp)
                        .clip(RoundedCornerShape(20)),
                    contentScale = ContentScale.Crop
                )
                TextViewBoldBig(v.data.name ?: "", 55)

                Spacer(Modifier.height(50.dp))

                Row(Modifier.fillMaxWidth(), Arrangement.Center, Alignment.CenterVertically) {
                    if (myLibraryViewModel.isPlaylistOfSameUser) {
                        Box(
                            Modifier
                                .padding(horizontal = 7.dp)
                                .clickable { changeNameView = true }) {
                            ImageIcon(R.drawable.ic_edit, 24)
                        }

                        Box(
                            Modifier
                                .padding(horizontal = 7.dp)
                                .clickable { changeImageView = true }) {
                            ImageIcon(R.drawable.ic_image_edit, 24)
                        }

                        Box(
                            Modifier
                                .padding(horizontal = 7.dp)
                                .clickable { deleteView = true }) {
                            ImageIcon(R.drawable.ic_delete, 24)
                        }

                        Box(
                            Modifier
                                .padding(horizontal = 7.dp)
                                .clickable { shareView = true }) {
                            ImageIcon(R.drawable.ic_share, 24)
                        }
                    } else {
                        LaunchedEffect(Unit) {
                            "not mine save it".toast()
                        }
                    }

                    Spacer(Modifier.weight(1f))
                    if (myLibraryViewModel.myPlaylistSongsList.isNotEmpty()) MiniWithImageAndBorder(
                        R.drawable.ic_play, R.string.play, MainColor
                    ) {
                        if (myLibraryViewModel.myPlaylistSongsList.isNotEmpty()) startMedia(
                            myLibraryViewModel.myPlaylistSongsList.first(),
                            myLibraryViewModel.myPlaylistSongsList
                        )
                    }
                }

                if (deleteView) TextAlertDialog(
                    R.string.delete_playlist,
                    R.string.delete_playlist_desc,
                    {
                        deleteView = false
                    },
                    {
                        deleteView = false
                        coroutines.launch {
                            v.data.id?.let { myLibraryViewModel.deleteMyPlaylist(it) }
                            delay(500)
                            CoroutineScope(Dispatchers.IO).launch {
                                delay(1.seconds)
                                refreshPlaylistState()
                                if (isActive) cancel()
                            }
                            NavigationUtils.triggerHomeNav(NAV_GO_BACK)
                        }
                    })

                if (shareView) ShareDataView(v.data) {
                    shareView = false
                }

                if (changeNameView) EditPlaylistNameDialog(v.data) {
                    if (it) myLibraryViewModel.myPlaylistInfo(v.data.id ?: "")
                    changeNameView = false
                }

                if (changeImageView) CustomImageOnMyPlaylist(v.data) {
                    if (it) myLibraryViewModel.myPlaylistInfo(v.data.id ?: "")
                    changeImageView = false
                }
            }
        }
    }
}
