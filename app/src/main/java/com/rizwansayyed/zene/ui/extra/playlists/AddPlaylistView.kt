package com.rizwansayyed.zene.ui.extra.playlists

import android.content.Context
import android.util.DisplayMetrics
import android.view.WindowManager
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.requiredSize
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import coil.compose.AsyncImage
import com.rizwansayyed.zene.R
import com.rizwansayyed.zene.ui.theme.MainColor
import com.rizwansayyed.zene.ui.view.ImageIcon
import com.rizwansayyed.zene.ui.view.TextPoppins
import com.rizwansayyed.zene.ui.view.imgBuilder
import com.rizwansayyed.zene.ui.view.isScreenBig

@Composable
fun AddPlaylistDialog(onDismiss: () -> Unit) {
    Dialog(
        properties = DialogProperties(usePlatformDefaultWidth = false),
        onDismissRequest = onDismiss
    ) {
        val context = LocalContext.current
        val windowManager =
            remember { context.getSystemService(Context.WINDOW_SERVICE) as WindowManager }

        val metrics = DisplayMetrics().apply {
            windowManager.defaultDisplay.getRealMetrics(this)
        }
        val (width, height) = with(LocalDensity.current) {
            Pair(metrics.widthPixels.toDp(), metrics.heightPixels.toDp())
        }

        Column(
            modifier = Modifier
                .requiredSize(width, height)
                .background(color = Color.Black),
            Arrangement.Center, Alignment.CenterHorizontally
        ) {
            AddPlaylistView()
        }
    }

}


@Composable
fun AddPlaylistView() {
    val screenWidth = LocalConfiguration.current.screenWidthDp.dp
    val isBig = isScreenBig()

    val img =
        "https://cdn10.phillymag.com/wp-content/uploads/sites/3/2020/09/monthly-playlist.jpg"

    TextPoppins(stringResource(id = R.string.new_playlists), size = 16)
    Spacer(Modifier.height(30.dp))
    AsyncImage(
        imgBuilder(img), "", Modifier
            .clip(RoundedCornerShape(12.dp))
            .size(if (isBig) (screenWidth / 2) else (screenWidth - 120.dp)),
        contentScale = ContentScale.Crop
    )
    Row(Modifier.padding(top = 30.dp), Arrangement.Center, Alignment.CenterVertically) {
        Row(Modifier.clickable { }) {
            ImageIcon(R.drawable.ic_search, 26)
        }
        Spacer(Modifier.width(30.dp))
        Row(Modifier.clickable { }) {
            ImageIcon(R.drawable.ic_folder, 26)
        }
    }

}