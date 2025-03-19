package com.rizwansayyed.zene.ui.musicplayer

import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
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
import com.rizwansayyed.zene.ui.theme.Pink80
import com.rizwansayyed.zene.ui.theme.proximanOverFamily
import com.rizwansayyed.zene.ui.view.ButtonWithImageAndBorder
import com.rizwansayyed.zene.ui.view.CircularLoadingView
import com.rizwansayyed.zene.ui.view.ImageIcon
import com.rizwansayyed.zene.ui.view.TextViewNormal
import com.rizwansayyed.zene.ui.view.TextViewSemiBold
import com.rizwansayyed.zene.utils.NavigationUtils
import com.rizwansayyed.zene.utils.NavigationUtils.NAV_PODCAST_PAGE
import com.rizwansayyed.zene.utils.share.MediaContentUtils
import com.rizwansayyed.zene.viewmodel.PlayerViewModel

@OptIn(ExperimentalGlideComposeApi::class)
@Composable
fun MusicPlayerPodcastInfoView(viewModel: PlayerViewModel) {
    when (val v = viewModel.playerPodcastInfo) {
        ResponseResult.Empty -> {}
        is ResponseResult.Error -> {}
        ResponseResult.Loading -> CircularLoadingView()
        is ResponseResult.Success -> LazyColumn(
            Modifier
                .height(400.dp)
                .padding(horizontal = 20.dp)
                .clip(RoundedCornerShape(15.dp))
                .background(Color.Black)
                .padding(horizontal = 5.dp)
                .fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            item { Spacer(Modifier.height(20.dp)) }
            item {
                Row(Modifier.fillMaxWidth()) {
                    Spacer(Modifier.weight(1f))
                    ImageIcon(R.drawable.ic_information_circle, 19)
                    Spacer(Modifier.width(10.dp))
                }
            }
            item { Spacer(Modifier.height(20.dp)) }
            item {
                if (v.data.image?.url != null) GlideImage(
                    v.data.image.url,
                    v.data.slug,
                    Modifier
                        .size(120.dp)
                        .clip(RoundedCornerShape(14.dp)),
                    contentScale = ContentScale.Crop
                )
                else GlideImage(
                    v.data.series?.image?.url,
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
                                color = Pink80
                            )
                        )
                    ),
                    Modifier
                        .fillMaxWidth()
                        .animateContentSize(),
                    Color.White,
                    14.sp,
                    null,
                    FontWeight.Normal,
                    proximanOverFamily,
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
                    }
                }
            }

            if (v.data.home != null) item {
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

            item { Spacer(Modifier.height(50.dp)) }
        }
    }
}

@OptIn(ExperimentalGlideComposeApi::class)
@Composable
fun MusicPlayerRadioInfoView(viewModel: PlayerViewModel) {
    when (val v = viewModel.playerRadioInfo) {
        ResponseResult.Empty -> {}
        is ResponseResult.Error -> {}
        ResponseResult.Loading -> CircularLoadingView()
        is ResponseResult.Success -> LazyColumn(
            Modifier
                .height(400.dp)
                .padding(horizontal = 20.dp)
                .clip(RoundedCornerShape(15.dp))
                .background(Color.Black)
                .padding(horizontal = 5.dp)
                .fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            item { Spacer(Modifier.height(20.dp)) }
            item {
                Row(Modifier.fillMaxWidth()) {
                    Spacer(Modifier.weight(1f))
                    ImageIcon(R.drawable.ic_information_circle, 19)
                    Spacer(Modifier.width(10.dp))
                }
            }
            item { Spacer(Modifier.height(20.dp)) }
            item {
                GlideImage(
                    v.data.favicon,
                    v.data.name,
                    Modifier
                        .size(120.dp)
                        .clip(RoundedCornerShape(14.dp)),
                    contentScale = ContentScale.Crop
                )
            }

            item {
                Spacer(Modifier.height(14.dp))
                TextViewSemiBold(v.data.name ?: "", 18, center = true)
                if (v.data.country != null || v.data.state != null) {
                    Spacer(Modifier.height(10.dp))
                    TextViewNormal("${v.data.state}, ${v.data.country}", 15, center = true)
                }
            }

            item {
                if (v.data.language != null) {
                    Spacer(Modifier.height(10.dp))
                    TextViewNormal(v.data.language, 15, center = true)
                }
            }

            item {
                if (v.data.homepage != null) {
                    Spacer(Modifier.height(10.dp))

                    ButtonWithImageAndBorder(
                        R.drawable.ic_arrow_up_right, R.string.open_home_page, tint = Color.White
                    ) {
                        MediaContentUtils.openCustomBrowser(v.data.homepage)
                    }
                }
            }

            item { Spacer(Modifier.height(50.dp)) }
        }
    }
}