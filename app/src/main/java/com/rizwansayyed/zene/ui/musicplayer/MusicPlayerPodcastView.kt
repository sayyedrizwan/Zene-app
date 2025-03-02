package com.rizwansayyed.zene.ui.musicplayer

import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
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
import com.rizwansayyed.zene.data.ResponseResult
import com.rizwansayyed.zene.ui.theme.proximanOverFamily
import com.rizwansayyed.zene.ui.view.CircularLoadingView
import com.rizwansayyed.zene.ui.view.TextViewNormal
import com.rizwansayyed.zene.ui.view.TextViewSemiBold
import com.rizwansayyed.zene.viewmodel.PlayerViewModel

@OptIn(ExperimentalGlideComposeApi::class)
@Composable
fun MusicPlayerPodcastView(viewModel: PlayerViewModel) {
    when (val v = viewModel.playerPodcastInfo) {
        ResponseResult.Empty -> {}
        is ResponseResult.Error -> {}
        ResponseResult.Loading -> CircularLoadingView()
        is ResponseResult.Success -> Column(
            Modifier.padding(horizontal = 5.dp).fillMaxSize(), Arrangement.Center, Alignment.CenterHorizontally
        ) {
            GlideImage(
                v.data.image?.url,
                v.data.slug,
                Modifier
                    .size(120.dp)
                    .clip(RoundedCornerShape(14.dp)),
                contentScale = ContentScale.Crop
            )
            Spacer(Modifier.height(14.dp))
            TextViewSemiBold(v.data.title ?: "", 18, center = true)
            Spacer(Modifier.height(10.dp))
            TextViewNormal(v.data.series?.subtitle ?: "", 15, center = true)
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
    }
}