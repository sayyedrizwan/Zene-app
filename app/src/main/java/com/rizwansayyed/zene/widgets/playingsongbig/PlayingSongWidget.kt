package com.rizwansayyed.zene.widgets.playingsongbig

import android.content.Context
import android.content.Intent
import android.content.Intent.CATEGORY_APP_MUSIC
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.glance.ColorFilter
import androidx.glance.GlanceId
import androidx.glance.GlanceModifier
import androidx.glance.Image
import androidx.glance.ImageProvider
import androidx.glance.action.clickable
import androidx.glance.appwidget.CircularProgressIndicator
import androidx.glance.appwidget.GlanceAppWidget
import androidx.glance.appwidget.GlanceAppWidgetReceiver
import androidx.glance.appwidget.cornerRadius
import androidx.glance.appwidget.provideContent
import androidx.glance.background
import androidx.glance.layout.Alignment
import androidx.glance.layout.Box
import androidx.glance.layout.Column
import androidx.glance.layout.ContentScale
import androidx.glance.layout.Row
import androidx.glance.layout.Spacer
import androidx.glance.layout.fillMaxSize
import androidx.glance.layout.fillMaxWidth
import androidx.glance.layout.height
import androidx.glance.layout.padding
import androidx.glance.layout.size
import androidx.glance.layout.width
import androidx.glance.text.FontWeight
import androidx.glance.text.Text
import androidx.glance.text.TextStyle
import androidx.glance.unit.ColorProvider
import com.bumptech.glide.Glide
import com.rizwansayyed.zene.R
import com.rizwansayyed.zene.datastore.DataStorageManager.isLoopDB
import com.rizwansayyed.zene.datastore.DataStorageManager.isShuffleDB
import com.rizwansayyed.zene.datastore.DataStorageManager.musicPlayerDB
import com.rizwansayyed.zene.datastore.model.YoutubePlayerState
import com.rizwansayyed.zene.di.ZeneBaseApplication.Companion.context
import com.rizwansayyed.zene.service.player.PlayerForegroundService.Companion.getPlayerS
import com.rizwansayyed.zene.ui.main.MainActivity
import com.rizwansayyed.zene.ui.theme.MainColor
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.launch

class PlayingSongWidget : GlanceAppWidget() {
    override suspend fun provideGlance(context: Context, id: GlanceId) {
        provideContent {
            Box(
                GlanceModifier.background(MainColor).cornerRadius(13.dp).fillMaxSize().clickable {
                    Intent(context, MainActivity::class.java).apply {
                        flags = Intent.FLAG_ACTIVITY_NEW_TASK
                        putExtra(Intent.ACTION_SENDTO, CATEGORY_APP_MUSIC)
                        context.startActivity(this)
                    }
                }, Alignment.Center
            ) {
                PlayingMusicUI()

                Box(GlanceModifier.padding(5.dp).fillMaxSize(), Alignment.TopStart) {
                    Image(
                        provider = ImageProvider(R.mipmap.logo),
                        contentDescription = null,
                        modifier = GlanceModifier.size(20.dp)
                    )
                }
            }
        }
    }

    @Composable
    fun PlayingMusicUI() {
        val music by musicPlayerDB.collectAsState(null)
        var bitmap by remember { mutableStateOf<ImageProvider?>(null) }
        val coroutine = rememberCoroutineScope()

        val isLoopEnabled by isLoopDB.collectAsState(false)
        val isShuffleEnabled by isShuffleDB.collectAsState(false)


        if (music != null) Row(
            modifier = GlanceModifier.padding(7.dp).fillMaxSize(),
            Alignment.CenterHorizontally, Alignment.CenterVertically,
        ) {
            if (bitmap != null) Image(
                provider = bitmap!!,
                contentDescription = null,
                contentScale = ContentScale.Crop,
                modifier = GlanceModifier.padding(horizontal = 5.dp).width(80.dp).height(80.dp)
                    .cornerRadius(13.dp)
            )

            Column(
                GlanceModifier.defaultWeight().padding(horizontal = 8.dp),
                verticalAlignment = Alignment.Vertical.CenterVertically
            ) {
                Text(
                    music?.data?.name ?: "", GlanceModifier, TextStyle(
                        color = ColorProvider(Color.White),
                        fontSize = 16.sp,
                        FontWeight.Bold,
                    ), 1
                )

                Text(
                    music?.data?.artists ?: "", GlanceModifier.padding(top = 2.dp), TextStyle(
                        color = ColorProvider(Color.White), fontSize = 12.sp, FontWeight.Normal
                    ), 1
                )

                Row(
                    GlanceModifier.padding(top = 15.dp).fillMaxWidth(),
                    Alignment.Horizontal.CenterHorizontally,
                    Alignment.Vertical.CenterVertically
                ) {
                    Image(provider = ImageProvider(R.drawable.ic_backward),
                        contentDescription = null,
                        colorFilter = ColorFilter.tint(ColorProvider(Color.White)),
                        modifier = GlanceModifier.size(25.dp).clickable {
                            getPlayerS()?.toBackSong()
                        })

                    Spacer(GlanceModifier.defaultWeight())

                    Image(provider = ImageProvider(if (isLoopEnabled) R.drawable.ic_repeat_one else R.drawable.ic_repeat),
                        contentDescription = null,
                        colorFilter = ColorFilter.tint(ColorProvider(Color.White)),
                        modifier = GlanceModifier.size(22.dp).clickable {
                            coroutine.launch { isLoopDB = flowOf(!isLoopEnabled) }
                        })

                    Spacer(GlanceModifier.defaultWeight())

                    Image(provider = ImageProvider(if (isShuffleEnabled) R.drawable.ic_shuffle_square else R.drawable.ic_shuffle),
                        contentDescription = null,
                        colorFilter = ColorFilter.tint(ColorProvider(Color.White)),
                        modifier = GlanceModifier.size(22.dp).clickable {
                            coroutine.launch { isShuffleDB = flowOf(!isShuffleEnabled) }
                        })
                    Spacer(GlanceModifier.defaultWeight())

                    Image(provider = ImageProvider(R.drawable.ic_forward),
                        contentDescription = null,
                        colorFilter = ColorFilter.tint(ColorProvider(Color.White)),
                        modifier = GlanceModifier.size(25.dp).clickable {
                            getPlayerS()?.toNextSong()
                        })

                }
            }

            Column(GlanceModifier.padding(5.dp).cornerRadius(100.dp).clickable {
                if (music?.state == YoutubePlayerState.PLAYING) getPlayerS()?.pause()
                else getPlayerS()?.play()
            }.background(Color.White).padding(6.dp)) {
                when (music?.state) {
                    YoutubePlayerState.PLAYING -> Image(
                        provider = ImageProvider(R.drawable.ic_pause),
                        contentDescription = null,
                        colorFilter = ColorFilter.tint(ColorProvider(Color.Black)),
                        modifier = GlanceModifier.size(20.dp)
                    )

                    YoutubePlayerState.BUFFERING -> CircularProgressIndicator(
                        GlanceModifier.size(26.dp), ColorProvider(MainColor)
                    )

                    else -> Image(
                        provider = ImageProvider(R.drawable.ic_play),
                        contentDescription = null,
                        colorFilter = ColorFilter.tint(ColorProvider(Color.Black)),
                        modifier = GlanceModifier.size(20.dp)
                    )
                }
            }
        }

        LaunchedEffect(music?.data?.thumbnail) {
            coroutine.launch(Dispatchers.IO) {
                if (music?.data?.thumbnail != null) {
                    val b =
                        Glide.with(context).asBitmap().load(music?.data?.thumbnail).submit().get()
                    bitmap = ImageProvider(b)
                }
            }
        }
    }
}

class PlayingSongWidgetReceiver : GlanceAppWidgetReceiver() {
    override val glanceAppWidget: GlanceAppWidget = PlayingSongWidget()
}