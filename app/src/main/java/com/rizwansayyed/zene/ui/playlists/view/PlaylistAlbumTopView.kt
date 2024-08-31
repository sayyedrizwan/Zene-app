package com.rizwansayyed.zene.ui.playlists.view

import android.graphics.Bitmap
import android.util.Log
import androidx.compose.foundation.background
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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AlertDialog
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.core.graphics.drawable.toBitmap
import coil.compose.AsyncImage
import com.rizwansayyed.zene.R
import com.rizwansayyed.zene.data.api.model.MusicType
import com.rizwansayyed.zene.data.api.model.ZeneMusicDataItems
import com.rizwansayyed.zene.data.db.DataStoreManager
import com.rizwansayyed.zene.ui.view.AlertDialogView
import com.rizwansayyed.zene.ui.view.ImageIcon
import com.rizwansayyed.zene.ui.view.TextPoppins
import com.rizwansayyed.zene.ui.view.TextPoppinsSemiBold
import com.rizwansayyed.zene.ui.view.TextPoppinsThin
import com.rizwansayyed.zene.ui.view.imgBuilder
import com.rizwansayyed.zene.ui.view.isScreenBig
import com.rizwansayyed.zene.ui.view.shareUrl
import com.rizwansayyed.zene.ui.view.shimmerEffectBrush
import com.rizwansayyed.zene.utils.FirebaseLogEvents
import com.rizwansayyed.zene.utils.FirebaseLogEvents.logEvents
import com.rizwansayyed.zene.utils.Utils.shareTxtImage
import com.rizwansayyed.zene.viewmodel.ZeneViewModel
import kotlinx.coroutines.flow.firstOrNull

@Composable
fun PlaylistAlbumTopView(
    v: ZeneMusicDataItems?, zeneViewModel: ZeneViewModel, added: Boolean?, close: () -> Unit
) {
    val screenWidth = LocalConfiguration.current.screenWidthDp.dp
    val isBig = isScreenBig()

    var fullDesc by remember { mutableStateOf(false) }
    var bitmap by remember { mutableStateOf<Bitmap?>(null) }
    var isAdded by remember { mutableStateOf(false) }
    var removeDialog by remember { mutableStateOf(false) }
    var isUserOwner by remember { mutableStateOf(false) }

    Column(
        Modifier
            .padding(horizontal = 5.dp)
            .fillMaxWidth(),
        Arrangement.Center, Alignment.CenterHorizontally
    ) {
        AsyncImage(
            imgBuilder(v?.thumbnail), v?.name,
            Modifier
                .clip(RoundedCornerShape(12.dp))
                .size(if (isBig) (screenWidth / 2) else (screenWidth - 120.dp)),
            contentScale = ContentScale.Crop,
            onSuccess = {
                bitmap = it.result.drawable.toBitmap()
            }
        )

        Spacer(Modifier.height(15.dp))
        TextPoppins(v?.name ?: "", true, size = 25)
        Spacer(Modifier.height(5.dp))

        if (v?.type() == MusicType.ALBUMS) {
            TextPoppinsThin(v.artists ?: "", true, size = 17)
        }

        Spacer(Modifier.height(7.dp))

        TextPoppinsThin(
            v?.extra?.substringBefore("From Wikipedia")?.trim() ?: "",
            true, size = 15, limit = if (fullDesc) 10000 else 3
        )
        if ((v?.extra?.length ?: 0) > 350) {
            Spacer(Modifier.height(10.dp))
            Box(
                Modifier
                    .rotate(if (fullDesc) 90f else -90f)
                    .clickable { fullDesc = !fullDesc }) {
                ImageIcon(R.drawable.ic_arrow_left, 30)
            }
        }

        Spacer(Modifier.height(27.dp))


        if (isUserOwner) Row(
            Modifier.fillMaxWidth(),
            Arrangement.Center,
            Alignment.CenterVertically
        ) {
            Box(Modifier.clickable { v?.let { shareTxtImage(shareUrl(v)) } }) {
                ImageIcon(R.drawable.ic_share, 27)
            }
            Spacer(Modifier.width(15.dp))

            Box(Modifier.clickable {
                if (isAdded) {
                    logEvents(FirebaseLogEvents.FirebaseEvents.REMOVED_CURRENT_PLAYLIST)
                    removeDialog = true
                } else {
                    logEvents(FirebaseLogEvents.FirebaseEvents.SAVED_CURRENT_PLAYLIST)
                    isAdded = true
                    zeneViewModel.createNewPlaylist(v?.name ?: "", bitmap, v?.id)
                }
            }) {
                ImageIcon(if (isAdded) R.drawable.ic_tick else R.drawable.ic_add, 27)
            }
        }

        Spacer(Modifier.height(27.dp))
    }

    LaunchedEffect(Unit) {
        isAdded = added ?: false
        isUserOwner = if (v?.id?.contains("zene_p_") == true) {
            v.artists == DataStoreManager.userInfoDB.firstOrNull()?.email
        } else {
            true
        }
    }

    if (removeDialog) AlertDialogView(
        R.string.are_you_sure_want_to_remove,
        R.string.are_you_sure_want_to_remove_desc,
        R.string.remove
    ) {
        if (it) {
            close()
            isAdded = false
            v?.id?.let { it1 -> zeneViewModel.deletePlaylists(it1) }
        }
        removeDialog = false
    }
}

@Composable
fun LoadingAlbumTopView(modifier: Modifier = Modifier) {
    val screenWidth = LocalConfiguration.current.screenWidthDp.dp
    val isBig = isScreenBig()

    Column(
        modifier
            .padding(horizontal = 5.dp)
            .fillMaxWidth(),
        Arrangement.Center, Alignment.CenterHorizontally
    ) {
        Spacer(
            Modifier
                .clip(RoundedCornerShape(12.dp))
                .size(if (isBig) (screenWidth / 2) else (screenWidth - 120.dp))
                .background(shimmerEffectBrush())
        )

        Spacer(Modifier.height(15.dp))

        Spacer(
            Modifier
                .padding(top = 9.dp)
                .padding(horizontal = 5.dp)
                .size(160.dp, 13.dp)
                .clip(RoundedCornerShape(40))
                .background(shimmerEffectBrush())
        )

        Spacer(Modifier.height(7.dp))


        Spacer(
            Modifier
                .padding(top = 9.dp)
                .padding(horizontal = 5.dp)
                .size(120.dp, 13.dp)
                .clip(RoundedCornerShape(40))
                .background(shimmerEffectBrush())
        )
    }
}