package com.rizwansayyed.zene.ui.artists.view

import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImage
import com.rizwansayyed.zene.R
import com.rizwansayyed.zene.ui.artists.artistviewmodel.ArtistsViewModel
import com.rizwansayyed.zene.ui.theme.LightGreenBlue
import com.rizwansayyed.zene.ui.theme.TwitterColor
import com.rizwansayyed.zene.utils.QuickSandLight
import com.rizwansayyed.zene.utils.QuickSandSemiBold
import com.rizwansayyed.zene.utils.Utils.capitalizeWords
import com.rizwansayyed.zene.utils.Utils.convertToAbbreviation
import com.rizwansayyed.zene.utils.Utils.convertToNumber
import com.rizwansayyed.zene.utils.Utils.openOnInstagramVideo
import com.rizwansayyed.zene.utils.Utils.showToast

@Composable
fun TopArtistsInfo(artistsViewModel: ArtistsViewModel = hiltViewModel()) {

    var showFullFollowerNumber by remember { mutableStateOf(false) }

    val monthlyTxt = stringResource(id = R.string.monthly_listeners)

    Column(
        Modifier.fillMaxWidth(),
        Arrangement.Center, Alignment.CenterHorizontally
    ) {
        if (artistsViewModel.artistsMainImages.isNotEmpty()) {
            AsyncImage(
                artistsViewModel.artistsMainImages,
                artistsViewModel.artistName,
                Modifier
                    .padding(top = 120.dp)
                    .size(160.dp)
                    .clip(RoundedCornerShape(10))
            )

            Spacer(Modifier.height(14.dp))
        } else {
            CircularProgressIndicator(
                Modifier
                    .padding(top = 120.dp)
                    .padding(18.dp)
                    .size(24.dp), Color.White
            )
        }

        QuickSandSemiBold(artistsViewModel.artistName.capitalizeWords(), size = 22)

        Spacer(Modifier.height(7.dp))

        if (artistsViewModel.listeners != null) {
            Row(
                Modifier.clickable { showFullFollowerNumber = !showFullFollowerNumber },
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
            ) {
                Image(
                    painterResource(id = R.drawable.ic_play_icon),
                    "",
                    Modifier
                        .size(21.dp)
                        .offset(x = 0.dp, y = 2.dp),
                    colorFilter = ColorFilter.tint(Color.White)
                )

                Spacer(Modifier.height(2.dp))

                if (showFullFollowerNumber)
                    QuickSandSemiBold("${artistsViewModel.listeners?.listenerNumber!!} $monthlyTxt", size = 17)
                else
                    QuickSandSemiBold(artistsViewModel.listeners?.listenerAtt!!, size = 17)
            }
        }

        if (artistsViewModel.bio != null) {
            Spacer(Modifier.height(7.dp))

            QuickSandLight(
                artistsViewModel.bio!!,
                size = 15,
                modifier = Modifier.padding(horizontal = 4.dp)
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ShowInstagramInfo(artistsViewModel: ArtistsViewModel = hiltViewModel()) {

    var showFullFollowerNumber by remember { mutableStateOf(false) }

    Spacer(Modifier.height(54.dp))

    if (artistsViewModel.artistsInstagramPosts != null) {
        Card(
            onClick = {
                openOnInstagramVideo(artistsViewModel.artistsInstagramPosts?.username ?: "")
            },
            modifier = Modifier
                .padding(10.dp)
                .fillMaxWidth(),
            shape = RoundedCornerShape(15.dp),
            colors = CardDefaults.cardColors(LightGreenBlue)
        ) {
            Column(
                Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {

                Spacer(Modifier.height(22.dp))

                AsyncImage(
                    artistsViewModel.artistsInstagramPosts?.profilePic,
                    artistsViewModel.artistsInstagramPosts?.name,
                    Modifier
                        .size(60.dp)
                        .clip(RoundedCornerShape(100))
                )

                Spacer(Modifier.height(2.dp))

                Row(
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Image(painterResource(id = R.drawable.ic_instagram), "", Modifier.size(16.dp))

                    Spacer(Modifier.width(4.dp))

                    QuickSandLight(
                        "/${artistsViewModel.artistsInstagramPosts?.username}",
                        size = 15
                    )
                }

                QuickSandLight(
                    artistsViewModel.artistsInstagramPosts?.bio.toString(),
                    size = 14,
                    modifier = Modifier.padding(top = 10.dp)
                )

                Row(
                    horizontalArrangement = Arrangement.Center,
                    modifier = Modifier
                        .padding(vertical = 40.dp)
                        .fillMaxWidth()
                ) {
                    Spacer(modifier = Modifier.width(30.dp))

                    Column(
                        verticalArrangement = Arrangement.Center,
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        QuickSandLight(
                            artistsViewModel.artistsInstagramPosts?.postCount.toString(),
                            size = 17
                        )

                        QuickSandSemiBold(stringResource(id = R.string.posts), size = 19)
                    }

                    Spacer(modifier = Modifier.weight(1f))

                    Column(
                        Modifier
                            .animateContentSize()
                            .clickable {
                                showFullFollowerNumber = !showFullFollowerNumber
                            },
                        verticalArrangement = Arrangement.Center,
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        QuickSandLight(
                            if (showFullFollowerNumber)
                                artistsViewModel.artistsInstagramPosts?.followers?.toInt()
                                    ?.convertToNumber().toString()
                            else
                                convertToAbbreviation(
                                    artistsViewModel.artistsInstagramPosts?.followers?.toInt() ?: 0
                                ),
                            size = 17
                        )

                        QuickSandSemiBold(stringResource(id = R.string.followers), size = 19)
                    }

                    Spacer(modifier = Modifier.width(30.dp))
                }
            }
        }
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ShowTwitterInfo(artistsViewModel: ArtistsViewModel = hiltViewModel()) {

    var showFullFollowerNumber by remember { mutableStateOf(true) }

    Spacer(Modifier.height(54.dp))

    if (artistsViewModel.artistsTwitterInfo != null) {
        Card(
            onClick = {
                openOnInstagramVideo(artistsViewModel.artistsTwitterInfo?.username ?: "")
            },
            modifier = Modifier
                .padding(10.dp)
                .fillMaxWidth(),
            shape = RoundedCornerShape(15.dp),
            colors = CardDefaults.cardColors(TwitterColor)
        ) {
            Column(
                Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {

                Spacer(Modifier.height(22.dp))

                AsyncImage(
                    artistsViewModel.artistsTwitterInfo?.profilePic,
                    artistsViewModel.artistsTwitterInfo?.name,
                    Modifier
                        .size(60.dp)
                        .clip(RoundedCornerShape(100))
                )

                Spacer(Modifier.height(2.dp))

                Row(
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Image(
                        painterResource(id = R.drawable.twitter_x_logo),
                        "",
                        Modifier
                            .offset(y = 5.dp, x = 2.dp)
                            .size(16.dp)
                    )

                    Spacer(Modifier.width(4.dp))

                    QuickSandLight(
                        "@${artistsViewModel.artistsInstagramPosts?.username}",
                        size = 15
                    )
                }


                Row(
                    horizontalArrangement = Arrangement.Center,
                    modifier = Modifier
                        .padding(vertical = 40.dp)
                        .fillMaxWidth()
                ) {
                    Spacer(modifier = Modifier.width(30.dp))

                    Column(
                        verticalArrangement = Arrangement.Center,
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        QuickSandLight(
                            artistsViewModel.artistsInstagramPosts?.postCount.toString(),
                            size = 17
                        )

                        QuickSandSemiBold(stringResource(id = R.string.posts), size = 19)
                    }

                    Spacer(modifier = Modifier.weight(1f))

                    Column(
                        Modifier
                            .animateContentSize()
                            .clickable {
                                showFullFollowerNumber = !showFullFollowerNumber
                            },
                        verticalArrangement = Arrangement.Center,
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        QuickSandLight(
                            if (showFullFollowerNumber)
                                artistsViewModel.artistsTwitterInfo?.followers?.convertToNumber()
                                    .toString()
                            else
                                convertToAbbreviation(
                                    artistsViewModel.artistsTwitterInfo?.followers ?: 0
                                ),
                            size = 17
                        )

                        QuickSandSemiBold(stringResource(id = R.string.followers), size = 19)
                    }

                    Spacer(modifier = Modifier.width(30.dp))
                }
            }
        }
    }
}