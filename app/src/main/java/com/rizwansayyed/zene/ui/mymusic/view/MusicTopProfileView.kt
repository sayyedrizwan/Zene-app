package com.rizwansayyed.zene.ui.mymusic.view

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.rizwansayyed.zene.R
import com.rizwansayyed.zene.data.db.DataStoreManager.userInfoDB
import com.rizwansayyed.zene.ui.mymusic.MyMusicType
import com.rizwansayyed.zene.ui.view.ImageIcon
import com.rizwansayyed.zene.ui.view.TextPoppins
import com.rizwansayyed.zene.ui.view.TextPoppinsThin
import com.rizwansayyed.zene.ui.view.imgBuilder

@Composable
fun TopMusicHeaders() {
    val profile by userInfoDB.collectAsState(initial = null)

    Spacer(Modifier.height(90.dp))

    Row(Modifier.padding(horizontal = 14.dp), Arrangement.Center) {
        Column {
            Row {
                AsyncImage(
                    imgBuilder(profile?.getProfilePicture()),
                    profile?.name,
                    Modifier
                        .size(65.dp)
                        .clip(RoundedCornerShape(100))
                )

                Column(Modifier.padding(top = 7.dp, start = 12.dp)) {
                    TextPoppins(profile?.name ?: "Zene User", size = 24, limit = 1)
                    TextPoppinsThin(profile?.email ?: "", size = 14, limit = 1)
                }
            }
        }
    }

    Spacer(Modifier.height(50.dp))
}

@Composable
fun TopHeaderSwitch(type: MyMusicType, typeClick: (MyMusicType) -> Unit, addPlaylist: () -> Unit) {
    Row {
        Spacer(Modifier.height(6.dp))
        Box(
            Modifier
                .padding(vertical = 2.dp, horizontal = 6.dp)
                .clip(RoundedCornerShape(100))
                .background(Color.Black)
                .clickable { typeClick(MyMusicType.PLAYLISTS) }
                .border(
                    1.dp,
                    if (type == MyMusicType.PLAYLISTS) Color.White else Color.Black,
                    RoundedCornerShape(100)
                )
                .padding(vertical = 9.dp, horizontal = 18.dp)
        ) {
            TextPoppins(stringResource(R.string.playlists_albums), size = 15)
        }

        AnimatedVisibility(visible = type == MyMusicType.PLAYLISTS) {
            Box(
                Modifier
                    .padding(vertical = 2.dp, horizontal = 6.dp)
                    .clip(RoundedCornerShape(100))
                    .background(Color.Black)
                    .clickable { addPlaylist() }
                    .border(1.dp, Color.White, RoundedCornerShape(100))
                    .padding(vertical = 9.dp, horizontal = 18.dp)
            ) {
                ImageIcon(R.drawable.ic_add, size = 20)
            }
        }

        Spacer(Modifier.height(6.dp))

        Box(
            Modifier
                .padding(vertical = 2.dp, horizontal = 6.dp)
                .clip(RoundedCornerShape(100))
                .background(Color.Black)
                .clickable { typeClick(MyMusicType.HISTORY) }
                .border(
                    1.dp,
                    if (type == MyMusicType.HISTORY) Color.White else Color.Black,
                    RoundedCornerShape(100)
                )
                .padding(vertical = 9.dp, horizontal = 18.dp)
        ) {
            TextPoppins(stringResource(R.string.songs_history), size = 15)
        }
    }
}