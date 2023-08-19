package com.rizwansayyed.zene.ui.home.musicplay

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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.rizwansayyed.zene.R
import com.rizwansayyed.zene.presenter.model.MusicPlayerDetails
import com.rizwansayyed.zene.ui.home.homenavmodel.HomeNavViewModel

@Composable
fun MusicPlayerButtonsView(music: MusicPlayerDetails, nav: HomeNavViewModel) {
    Spacer(modifier = Modifier.height(40.dp))

    Row(Modifier.horizontalScroll(rememberScrollState())) {

        Spacer(modifier = Modifier.height(8.dp))


        IconsForMusicShortcut(R.drawable.ic_video_replay) {
            val searchName = "${music.songName?.lowercase()?.replace("official video", "")} - ${
                music.artists?.substringBefore(",")?.substringBefore("&")
            }".lowercase()
            nav.playingVideo(searchName)
        }

        IconsForMusicShortcut(R.drawable.ic_closed_caption) {

        }

        IconsForMusicShortcut(R.drawable.ic_download_icon) {

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