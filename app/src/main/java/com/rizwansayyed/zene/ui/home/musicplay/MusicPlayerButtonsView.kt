package com.rizwansayyed.zene.ui.home.musicplay

import android.content.Intent
import android.content.Intent.FLAG_ACTIVITY_NEW_TASK
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.rizwansayyed.zene.R
import com.rizwansayyed.zene.presenter.SongsViewModel
import com.rizwansayyed.zene.presenter.model.MusicPlayerDetails
import com.rizwansayyed.zene.ui.home.homenavmodel.HomeNavViewModel
import com.rizwansayyed.zene.ui.home.musicplay.video.FullVideoPlayerActivity
import com.rizwansayyed.zene.ui.home.musicplay.video.VideoPlayerViewStatus
import com.rizwansayyed.zene.utils.Utils.EXTRA.SONG_NAME_EXTRA

@androidx.annotation.OptIn(androidx.media3.common.util.UnstableApi::class)
@Composable
fun MusicPlayerButtonsView(
    music: MusicPlayerDetails, nav: HomeNavViewModel, songs: SongsViewModel = hiltViewModel()
) {
    val context = LocalContext.current.applicationContext
    Spacer(modifier = Modifier.height(40.dp))

    Row(Modifier.horizontalScroll(rememberScrollState())) {

        Spacer(modifier = Modifier.height(8.dp))

        IconsForMusicShortcut(R.drawable.ic_video_replay) {
            nav.doPlayer(true)
            val searchName = "${music.songName?.lowercase()?.replace("official video", "")} - ${
                music.artists?.substringBefore(",")?.substringBefore("&")
            }".lowercase()
            Intent(context, FullVideoPlayerActivity::class.java).apply {
                putExtra(SONG_NAME_EXTRA, searchName)
                flags = FLAG_ACTIVITY_NEW_TASK
                context.startActivity(this)
            }
        }

        IconsForMusicShortcut(R.drawable.ic_closed_caption) {
            nav.musicViewType(VideoPlayerViewStatus.LYRICS)
        }

        IconsForMusicShortcut(R.drawable.ic_download_icon) {
            songs.insertOfflineSongs(music)
        }

        IconsForMusicShortcut(R.drawable.ic_instagram_simple) {

        }

        IconsForMusicShortcut(R.drawable.ic_bookmark) {

        }

        IconsForMusicShortcut(R.drawable.ic_information_circle) {

        }

        Spacer(modifier = Modifier.height(8.dp))

    }
}


@Composable
fun IconsForMusicShortcut(ic: Int, click: () -> Unit) {
    Image(
        painter = painterResource(id = ic),
        contentDescription = "",
        modifier = Modifier
            .padding(18.dp)
            .size(24.dp)
            .clickable {
                click()
            },
        colorFilter = ColorFilter.tint(Color.White)
    )
}