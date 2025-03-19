package com.rizwansayyed.zene.ui.main.view

import android.util.Log
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheet
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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.bumptech.glide.integration.compose.GlideImage
import com.rizwansayyed.zene.R
import com.rizwansayyed.zene.data.model.MusicDataTypes
import com.rizwansayyed.zene.data.model.ZeneMusicData
import com.rizwansayyed.zene.ui.main.view.share.ShareDataView
import com.rizwansayyed.zene.ui.musicplayer.delimiters
import com.rizwansayyed.zene.ui.theme.MainColor
import com.rizwansayyed.zene.ui.view.ImageIcon
import com.rizwansayyed.zene.ui.view.TextViewNormal
import com.rizwansayyed.zene.ui.view.TextViewSemiBold
import com.rizwansayyed.zene.utils.share.MediaContentUtils.startMedia
import com.rizwansayyed.zene.viewmodel.InfoSheetViewModel
import com.rizwansayyed.zene.viewmodel.NavigationViewModel

@OptIn(ExperimentalMaterial3Api::class, ExperimentalFoundationApi::class)
@Composable
fun LongPressSheetView(viewModel: NavigationViewModel) {
    if (viewModel.showMediaInfoSheet != null) ModalBottomSheet(
        { viewModel.setShowMediaInfo(null) }, contentColor = MainColor, containerColor = MainColor
    ) {
        val infoViewModel: InfoSheetViewModel = hiltViewModel()
        var showShare by remember { mutableStateOf(false) }

        LazyColumn(Modifier.fillMaxWidth()) {
            stickyHeader { MediaItemView(viewModel.showMediaInfoSheet!!) }

            item {
                if (viewModel.showMediaInfoSheet?.type() == MusicDataTypes.SONGS || viewModel.showMediaInfoSheet?.type() == MusicDataTypes.PODCAST_AUDIO || viewModel.showMediaInfoSheet?.type() == MusicDataTypes.RADIO || viewModel.showMediaInfoSheet?.type() == MusicDataTypes.VIDEOS) {
                    LongPressSheetItem(R.drawable.ic_play, R.string.play) {
                        startMedia(viewModel.showMediaInfoSheet)
                        viewModel.setShowMediaInfo(null)
                    }
                } else {
                    LongPressSheetItem(R.drawable.ic_arrow_up_right, R.string.view) {
                        startMedia(viewModel.showMediaInfoSheet)
                        viewModel.setShowMediaInfo(null)
                    }
                }
            }

            item {
                LongPressSheetItem(R.drawable.ic_vynil, R.string.go_to_album) {
                    viewModel.setShowMediaInfo(null)
                }
            }

            item {
                viewModel.showMediaInfoSheet?.artists?.split(*delimiters)?.forEach {
                    LongPressSheetItem(
                        R.drawable.ic_artists, txtS = "${stringResource(R.string.view)} $it"
                    ) {
                        viewModel.setShowMediaInfo(null)
                    }
                }
            }

            item {
//            if (viewModel.showMediaInfoSheet?.type() == MusicDataTypes.PODCAST_AUDIO) {
                LongPressSheetItem(R.drawable.ic_podcast, R.string.view_podcast_series) {
                    viewModel.setShowMediaInfo(null)
                }
//            }
            }

            item {
                LongPressSheetItem(R.drawable.ic_layer_add, R.string.add_to_your_library) {

                }
            }

            item {
                LongPressSheetItem(R.drawable.ic_thumbs_up, R.string.like) {

                }
            }

            item {
                LongPressSheetItem(
                    R.drawable.ic_screen_add_to_home, R.string.add_shortcut_to_home_screen
                ) {
                    showShare = true
                }
            }

            item {
                LongPressSheetItem(R.drawable.ic_share, R.string.share) {
                    showShare = true
                }
            }

            item {
                LongPressSheetItem(R.drawable.ic_cancel_close, R.string.close) {
                    viewModel.setShowMediaInfo(null)
                }
            }

            item { Spacer(Modifier.height(55.dp)) }
        }

        if (showShare) ShareDataView(viewModel.showMediaInfoSheet!!) {
            showShare = false
        }

        LaunchedEffect(Unit) {

            Log.d("TAG", "LongPressSheetView: ${viewModel.showMediaInfoSheet}")
        }
    }
}

@OptIn(ExperimentalGlideComposeApi::class)
@Composable
fun MediaItemView(info: ZeneMusicData) {
    Row(
        Modifier
            .fillMaxWidth()
            .padding(horizontal = 10.dp, vertical = 20.dp)
            .padding(bottom = 20.dp), Arrangement.Center, Alignment.CenterVertically
    ) {
        GlideImage(
            info.thumbnail,
            info.name,
            Modifier
                .size(90.dp)
                .clip(RoundedCornerShape(10.dp)),
            contentScale = ContentScale.Crop
        )
        Column(
            Modifier
                .padding(horizontal = 12.dp)
                .weight(1f), Arrangement.Center, Alignment.Start
        ) {
            TextViewSemiBold(info.name ?: "", 18)
            Spacer(Modifier.height(3.dp))
            TextViewNormal(info.artists ?: "", 14, line = 4)
        }

    }
}


@Composable
fun LongPressSheetItem(img: Int, txt: Int? = null, txtS: String? = null, click: () -> Unit) {
    Row(
        Modifier
            .fillMaxWidth()
            .padding(horizontal = 10.dp, vertical = 15.dp)
            .clickable { click() },
        Arrangement.Center,
        Alignment.CenterVertically
    ) {
        ImageIcon(img, 22)

        Box(
            Modifier
                .weight(1f)
                .padding(horizontal = 9.dp)
        ) {
            if (txt != null) TextViewNormal(stringResource(txt), 17)
            else if (txtS != null) TextViewNormal(txtS, 17)
        }
    }
}