package com.rizwansayyed.zene.ui.musicplayer

import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextLinkStyles
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.fromHtml
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.bumptech.glide.integration.compose.GlideImage
import com.rizwansayyed.zene.R
import com.rizwansayyed.zene.data.ResponseResult
import com.rizwansayyed.zene.ui.theme.proximanOverFamily
import com.rizwansayyed.zene.ui.view.ButtonWithImageAndBorder
import com.rizwansayyed.zene.ui.view.CircularLoadingView
import com.rizwansayyed.zene.ui.view.TextViewNormal
import com.rizwansayyed.zene.ui.view.TextViewSemiBold
import com.rizwansayyed.zene.utils.MediaContentUtils
import com.rizwansayyed.zene.utils.NavigationUtils
import com.rizwansayyed.zene.utils.NavigationUtils.NAV_PODCAST_PAGE
import com.rizwansayyed.zene.viewmodel.NavigationViewModel
import com.rizwansayyed.zene.viewmodel.PlayerViewModel

@OptIn(ExperimentalGlideComposeApi::class)
@Composable
fun MusicPlayerPodcastView(viewModel: PlayerViewModel, navViewModel: NavigationViewModel) {
    when (val v = viewModel.playerPodcastInfo) {
        ResponseResult.Empty -> {}
        is ResponseResult.Error -> {}
        ResponseResult.Loading -> CircularLoadingView()
        is ResponseResult.Success -> LazyColumn(
            Modifier
                .padding(horizontal = 5.dp)
                .fillMaxSize(), horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            item { Spacer(Modifier.height(150.dp)) }
            item {
                GlideImage(
                    v.data.image?.url,
                    v.data.slug,
                    Modifier
                        .size(120.dp)
                        .clip(RoundedCornerShape(14.dp)),
                    contentScale = ContentScale.Crop
                )
            }

            item {
                Spacer(Modifier.height(14.dp))
                TextViewSemiBold(v.data.title ?: "", 18, center = true)
                if (v.data.series?.title != null) {
                    Spacer(Modifier.height(10.dp))
                    TextViewNormal(v.data.series.title, 15, center = true)
                }
            }

            item {
                Spacer(Modifier.height(10.dp))
                Text(
                    AnnotatedString.fromHtml(
                        v.data.description ?: "", linkStyles = TextLinkStyles(
                            style = SpanStyle(
                                textDecoration = TextDecoration.Underline,
                                fontStyle = FontStyle.Italic,
                                color = Color.Black
                            )
                        )
                    ),
                    Modifier
                        .fillMaxWidth()
                        .animateContentSize(),
                    Color.White, 14.sp, null, FontWeight.Normal, proximanOverFamily,
                    textAlign = TextAlign.Center
                )
            }

            item {
                if (v.data.series?.slug != null) {
                    Spacer(Modifier.height(10.dp))

                    ButtonWithImageAndBorder(
                        R.drawable.ic_arrow_up_right,
                        R.string.open_podcast_series,
                        tint = Color.White
                    ) {
                        NavigationUtils.triggerHomeNav("$NAV_PODCAST_PAGE${v.data.series.slug}")
                        navViewModel.setMusicPlayer(false)
                    }
                }
            }

            item {
                if (v.data.series?.home != v.data.home) {
                    Spacer(Modifier.height(10.dp))

                    ButtonWithImageAndBorder(
                        R.drawable.ic_arrow_up_right, R.string.open_episode_page, tint = Color.White
                    ) {
                        MediaContentUtils.openCustomBrowser(v.data.home)
                    }
                }
            }

            item {
                if (v.data.series?.home != null) {
                    Spacer(Modifier.height(10.dp))

                    ButtonWithImageAndBorder(
                        R.drawable.ic_arrow_up_right, R.string.open_home_page, tint = Color.White
                    ) {
                        MediaContentUtils.openCustomBrowser(v.data.series.home)
                    }
                }
            }

            item { Spacer(Modifier.height(250.dp)) }
        }
    }
}