package com.rizwansayyed.zene.ui.main.view

import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.palette.graphics.Palette
import com.bumptech.glide.Glide
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.bumptech.glide.integration.compose.GlideImage
import com.bumptech.glide.request.target.CustomTarget
import com.bumptech.glide.request.transition.Transition
import com.rizwansayyed.zene.R
import com.rizwansayyed.zene.datastore.DataStorageManager
import com.rizwansayyed.zene.datastore.model.YoutubePlayerState
import com.rizwansayyed.zene.service.player.PlayerForegroundService.Companion.getPlayerS
import com.rizwansayyed.zene.ui.main.home.HomeNavSelector
import com.rizwansayyed.zene.ui.theme.MainColor
import com.rizwansayyed.zene.ui.view.ImageIcon
import com.rizwansayyed.zene.ui.view.TextViewBold
import com.rizwansayyed.zene.ui.view.TextViewNormal
import com.rizwansayyed.zene.ui.view.TextViewSemiBold
import com.rizwansayyed.zene.utils.NavigationUtils
import com.rizwansayyed.zene.utils.NavigationUtils.NAV_MAIN_PAGE
import com.rizwansayyed.zene.viewmodel.NavigationViewModel

@Composable
fun HomeBottomNavigationView(
    modifier: Modifier = Modifier, vm: NavigationViewModel
) {
    Column(
        modifier
            .padding(bottom = 40.dp)
            .fillMaxWidth()
    ) {
        MusicPlayerMiniView {
            vm.setMusicPlayer(true)
        }
        Row(
            Modifier
                .fillMaxWidth()
                .background(Color.Black)
                .padding(vertical = 17.dp, horizontal = 5.dp),
            Arrangement.Absolute.SpaceBetween,
            Alignment.CenterVertically
        ) {
            HomeBottomNavItems(R.drawable.ic_home, R.string.home, HomeNavSelector.HOME, vm)
            HomeBottomNavItems(R.drawable.ic_hotspot, R.string.connect, HomeNavSelector.CONNECT, vm)
            HomeBottomNavItems(R.drawable.ic_search, R.string.search, HomeNavSelector.SEARCH, vm)
            HomeBottomNavItems(R.drawable.ic_audio_book, R.string.ent_, HomeNavSelector.ENT, vm)
        }
    }
}

@OptIn(ExperimentalGlideComposeApi::class)
@Composable
fun MusicPlayerMiniView(openPlayer: () -> Unit) {
    val context = LocalContext.current.applicationContext
    val player by DataStorageManager.musicPlayerDB.collectAsState(null)
    var gradientColors by remember { mutableStateOf<List<Color>>(emptyList()) }
    var loadedImage by remember { mutableStateOf<Bitmap?>(null) }

    if (player?.data?.id != null) Row(Modifier
        .clickable(
            indication = null,
            interactionSource = remember { MutableInteractionSource() }
        ) { openPlayer() }
        .padding(5.dp)
        .fillMaxWidth()
        .clip(RoundedCornerShape(5.dp))
        .background(MainColor)
        .background(
            Brush.linearGradient(
                colors = gradientColors.ifEmpty { listOf(MainColor, Color.Black) },
                start = Offset(0f, 0f),
                end = Offset(Float.POSITIVE_INFINITY, Float.POSITIVE_INFINITY)
            )
        )
        .padding(10.dp), Arrangement.Absolute.SpaceBetween, Alignment.CenterVertically
    ) {
        GlideImage(
            loadedImage,
            player?.data?.name,
            Modifier
                .size(40.dp)
                .clip(RoundedCornerShape(5.dp)),
            contentScale = ContentScale.Crop
        )

        Column(
            Modifier
                .weight(1f)
                .padding(horizontal = 12.dp)
                .offset(y = 3.dp)
        ) {
            TextViewBold(player!!.data?.name ?: "", 13, line = 1)
            Box(Modifier.offset(y = (-3).dp)) {
                TextViewNormal(player!!.data?.artists ?: "", 12, line = 1)
            }
        }

        Box(
            Modifier
                .padding(end = 10.dp)
                .clickable(
                    indication = null,
                    interactionSource = remember { MutableInteractionSource() }
                ) {
                    if (player!!.state == YoutubePlayerState.PLAYING) getPlayerS()?.pause()
                    else getPlayerS()?.play()
                }, Alignment.Center
        ) {
            when (player!!.state) {
                YoutubePlayerState.BUFFERING -> CircularProgressIndicator(
                    Modifier.size(24.dp), Color.White, 4.dp, MainColor
                )

                YoutubePlayerState.PLAYING -> ImageIcon(R.drawable.ic_pause, 30)
                else -> ImageIcon(R.drawable.ic_play, 30)
            }
        }
    }

    LaunchedEffect(player?.data?.thumbnail) {
        Glide.with(context).asBitmap().load(player?.data?.thumbnail)
            .into(object : CustomTarget<Bitmap>() {
                override fun onResourceReady(resource: Bitmap, p1: Transition<in Bitmap>?) {
                    loadedImage = resource
                    Palette.from(resource).generate { palette ->
                        palette?.let {
                            val vibrant = it.vibrantSwatch?.rgb?.let { Color(it) } ?: MainColor
                            val darkVibrant =
                                it.darkVibrantSwatch?.rgb?.let { Color(it) } ?: Color.Black
                            val darkMuted = it.darkMutedSwatch?.rgb?.let { Color(it) }

                            gradientColors = listOfNotNull(vibrant, darkVibrant, darkMuted)
                        }
                    }
                }

                override fun onLoadCleared(placeholder: Drawable?) {}
            })
    }
}

@Composable
fun HomeBottomNavItems(icon: Int, txt: Int, nav: HomeNavSelector, vm: NavigationViewModel) {
    Column(Modifier
        .padding(horizontal = 5.dp)
        .clickable(
            interactionSource = remember { MutableInteractionSource() },
            indication = null
        ) {
            NavigationUtils.triggerHomeNav(NAV_MAIN_PAGE)
            vm.setHomeNavSections(nav)
        }
        .padding(horizontal = 15.dp), Arrangement.Center, Alignment.CenterHorizontally) {
        if (vm.homeNavSection == nav) {
            ImageIcon(icon, 25)
            Spacer(Modifier.height(4.dp))
            TextViewSemiBold(stringResource(txt), 14, line = 1)
        } else {
            ImageIcon(icon, 25, Color.Gray)
            Spacer(Modifier.height(4.dp))
            TextViewSemiBold(stringResource(txt), 14, Color.Gray, line = 1)
        }
    }
}