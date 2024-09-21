package com.rizwansayyed.zene.ui.home.view

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.rizwansayyed.zene.R
import com.rizwansayyed.zene.data.api.model.ZeneArtistsDataList
import com.rizwansayyed.zene.ui.view.CardsViewDesc
import com.rizwansayyed.zene.ui.view.LoadingCardView
import com.rizwansayyed.zene.ui.view.SimpleCardsView
import com.rizwansayyed.zene.ui.view.TextPoppins
import com.rizwansayyed.zene.ui.view.TextPoppinsThin
import com.rizwansayyed.zene.ui.view.VideoCardsViewWithSong
import com.rizwansayyed.zene.ui.view.bouncingClickable
import com.rizwansayyed.zene.ui.view.imgBuilder
import com.rizwansayyed.zene.ui.view.openSpecificIntent
import com.rizwansayyed.zene.ui.view.shimmerEffectBrush

@Composable
fun HomeArtistsSimilarToView(m: ZeneArtistsDataList) {
    Column(
        Modifier
            .padding(top = 10.dp, bottom = 60.dp)
            .fillMaxWidth()
    ) {
        Row(Modifier.padding(horizontal = 5.dp), Arrangement.Center) {
            AsyncImage(
                imgBuilder(m.artists.thumbnail),
                m.artists.name,
                Modifier
                    .size(80.dp)
                    .clip(RoundedCornerShape(100))
                    .background(Color.DarkGray)
                    .bouncingClickable { openSpecificIntent(m.artists, emptyList()) },
                contentScale = ContentScale.Crop
            )

            Column(
                Modifier
                    .padding(horizontal = 5.dp)
                    .height(80.dp), Arrangement.Center
            ) {
                Row(
                    Modifier
                        .padding(top = 5.dp)
                        .padding(horizontal = 5.dp)
                ) {
                    TextPoppinsThin(stringResource(R.string.recommended), size = 15, limit = 1)
                }

                Row(
                    Modifier
                        .padding(top = 2.dp)
                        .padding(horizontal = 5.dp)
                        .clickable { }
                ) {
                        TextPoppins(m.artists.name ?: "", size = 17, limit = 1)
                }

            }
        }

        Spacer(Modifier.height(20.dp))

        LazyRow {
            items(m.songs) {
                CardsViewDesc(it, m.songs)
            }
        }

        Spacer(Modifier.height(30.dp))

        LazyRow {
            items(m.playlists) {
                SimpleCardsView(it, m.playlists)
            }
        }

        Spacer(Modifier.height(30.dp))

        LazyRow {
            items(m.videos) {
                VideoCardsViewWithSong(it, m.videos)
            }
        }
    }
}

@Composable
fun HomeArtistsSimilarLoading() {
    Column(
        Modifier
            .padding(top = 10.dp, bottom = 40.dp)
            .fillMaxWidth()
    ) {
        Row(Modifier.padding(horizontal = 5.dp), Arrangement.Center) {
            Spacer(
                Modifier
                    .size(80.dp)
                    .clip(RoundedCornerShape(100))
                    .background(shimmerEffectBrush())
            )

            Column(
                Modifier
                    .padding(horizontal = 5.dp)
                    .height(80.dp), Arrangement.Center
            ) {

                Spacer(
                    Modifier
                        .padding(horizontal = 5.dp)
                        .size(100.dp, 10.dp)
                        .clip(RoundedCornerShape(40))
                        .background(shimmerEffectBrush())
                )

                Spacer(
                    Modifier
                        .padding(top = 7.dp)
                        .padding(horizontal = 5.dp)
                        .size(155.dp, 10.dp)
                        .clip(RoundedCornerShape(40))
                        .background(shimmerEffectBrush())
                )
            }
        }

        Spacer(Modifier.height(10.dp))

        LazyRow {
            items(9) {
                LoadingCardView()
            }
        }

    }
}