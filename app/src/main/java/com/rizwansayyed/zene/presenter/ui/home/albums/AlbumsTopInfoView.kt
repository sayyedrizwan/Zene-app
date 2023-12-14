package com.rizwansayyed.zene.presenter.ui.home.albums

import androidx.compose.foundation.Image
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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImage
import com.rizwansayyed.zene.R
import com.rizwansayyed.zene.data.DataResponse
import com.rizwansayyed.zene.presenter.theme.MainColor
import com.rizwansayyed.zene.presenter.ui.SmallIcons
import com.rizwansayyed.zene.presenter.ui.TextRegular
import com.rizwansayyed.zene.presenter.ui.TextSemiBold
import com.rizwansayyed.zene.presenter.ui.TextThin
import com.rizwansayyed.zene.presenter.ui.TextThinArtistsDesc
import com.rizwansayyed.zene.presenter.ui.shimmerBrush
import com.rizwansayyed.zene.service.player.utils.Utils
import com.rizwansayyed.zene.service.player.utils.Utils.addAllPlayer
import com.rizwansayyed.zene.viewmodel.HomeNavViewModel
import com.rizwansayyed.zene.viewmodel.PlaylistAlbumViewModel


@Composable
fun AlbumsTopThumbnail() {
    val playlistAlbum: PlaylistAlbumViewModel = hiltViewModel()
    val height = LocalConfiguration.current.screenHeightDp.dp / 2

    when (val v = playlistAlbum.playlistAlbum) {
        DataResponse.Loading -> Spacer(
            Modifier
                .fillMaxWidth()
                .height(height)
                .padding(15.dp)
                .background(shimmerBrush())
        )

        is DataResponse.Success -> AsyncImage(
            v.item.thumbnail, v.item.name, Modifier
                .fillMaxWidth()
                .height(height)
                .clip(RoundedCornerShape(12.dp))
        )

        else -> {}
    }
}

@Composable
fun AlbumNameWithPlayAllButton() {
    val playlistAlbum: PlaylistAlbumViewModel = hiltViewModel()
    var desc by remember { mutableStateOf(false) }

    when (val v = playlistAlbum.playlistAlbum) {
        DataResponse.Loading -> repeat(9) {
            Spacer(
                Modifier
                    .padding(horizontal = 10.dp, vertical = 8.dp)
                    .fillMaxWidth()
                    .height(11.dp)
                    .background(shimmerBrush())
            )
        }

        is DataResponse.Success -> {
            Row(
                Modifier
                    .padding(top = 20.dp)
                    .padding(horizontal = 10.dp)
                    .fillMaxWidth(),
                Arrangement.Center, Alignment.CenterVertically
            ) {
                Column(Modifier.weight(1f)) {
                    AlbumNames(v.item.name ?: "", v.item.artistsName ?: "")
                }

                Image(
                    painterResource(R.drawable.ic_play),
                    "",
                    Modifier
                        .padding(5.dp)
                        .clip(RoundedCornerShape(100))
                        .background(MainColor)
                        .clickable {
                            addAllPlayer(playlistAlbum.playlistSongsItem.toTypedArray(), 0)
                        }
                        .padding(vertical = 15.dp)
                        .padding(start = 18.dp, end = 15.dp),
                    colorFilter = ColorFilter.tint(Color.White)
                )
            }


            if ((v.item.description?.length ?: 0) > 2) {
                TextThinArtistsDesc(v.item.description ?: "", desc)
                Spacer(Modifier.height(5.dp))

                Row(Modifier.fillMaxWidth(), Arrangement.Center, Alignment.CenterVertically) {
                    if (desc) Box(Modifier.rotate(180f)) {
                        SmallIcons(R.drawable.ic_arrow_down_sharp) {
                            desc = !desc
                        }
                    }
                    else SmallIcons(R.drawable.ic_arrow_down_sharp) {
                        desc = !desc
                    }
                }
            }
            Spacer(Modifier.height(5.dp))

        }

        else -> {}
    }
}

@Composable
fun AlbumNames(name: String, artists: String) {
    val homeNav: HomeNavViewModel = hiltViewModel()

    TextSemiBold(name, size = 25)
    Spacer(Modifier.height(4.dp))
    TextRegular(artists, Modifier.clickable {
        homeNav.setArtists(artists)
    })
}