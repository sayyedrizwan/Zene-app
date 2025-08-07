package com.rizwansayyed.zene.widgets.playingsongbig

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.Intent.CATEGORY_APP_MUSIC
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.glance.GlanceId
import androidx.glance.GlanceModifier
import androidx.glance.Image
import androidx.glance.ImageProvider
import androidx.glance.LocalContext
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
import androidx.glance.layout.Row
import androidx.glance.layout.Spacer
import androidx.glance.layout.fillMaxSize
import androidx.glance.layout.fillMaxWidth
import androidx.glance.layout.height
import androidx.glance.layout.padding
import androidx.glance.layout.size
import androidx.glance.unit.ColorProvider
import com.rizwansayyed.zene.R
import com.rizwansayyed.zene.datastore.DataStorageManager.isLoopDB
import com.rizwansayyed.zene.datastore.DataStorageManager.isShuffleDB
import com.rizwansayyed.zene.datastore.DataStorageManager.musicPlayerDB
import com.rizwansayyed.zene.datastore.model.MusicPlayerData
import com.rizwansayyed.zene.datastore.model.YoutubePlayerState
import com.rizwansayyed.zene.service.player.PlayerForegroundService.Companion.getPlayerS
import com.rizwansayyed.zene.ui.main.MainActivity
import com.rizwansayyed.zene.ui.theme.MainColor
import com.rizwansayyed.zene.widgets.utils.GlanceImage
import com.rizwansayyed.zene.widgets.utils.GlanceImageIcon
import com.rizwansayyed.zene.widgets.utils.GlanceTextItemBold
import com.rizwansayyed.zene.widgets.utils.GlanceTextItemNormal
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.launch

class PlayingSongWidget : GlanceAppWidget() {
    override suspend fun provideGlance(context: Context, id: GlanceId) {
        provideContent {
            val music by musicPlayerDB.collectAsState(null)
            Box(
                GlanceModifier.background(MainColor).cornerRadius(13.dp).fillMaxSize().clickable {
                    Intent(context, MainActivity::class.java).apply {
                        flags = Intent.FLAG_ACTIVITY_NEW_TASK
                        if (music?.data != null) putExtra(Intent.ACTION_SENDTO, CATEGORY_APP_MUSIC)
                        context.startActivity(this)
                    }
                }, Alignment.Center
            ) {
                PlayingMusicUI(music, context)

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

    @SuppressLint("RestrictedApi")
    @Composable
    fun PlayingMusicUI(music: MusicPlayerData?, context: Context) {
        val coroutine = rememberCoroutineScope()

        val isLoopEnabled by isLoopDB.collectAsState(false)
        val isShuffleEnabled by isShuffleDB.collectAsState(false)


        if (music?.data != null) {
            Row(
                modifier = GlanceModifier.padding(7.dp).fillMaxSize(),
                Alignment.CenterHorizontally, Alignment.CenterVertically,
            ) {
                GlanceImage(music.data?.thumbnail, 80, context)

                Column(
                    GlanceModifier.defaultWeight().padding(horizontal = 8.dp),
                    verticalAlignment = Alignment.Vertical.CenterVertically
                ) {
                    GlanceTextItemBold(music.data?.name ?: "")
                    Spacer(GlanceModifier.height(1.dp))
                    GlanceTextItemNormal(music.data?.artists ?: "")

                    Row(
                        GlanceModifier.padding(top = 10.dp).fillMaxWidth(),
                        Alignment.Horizontal.CenterHorizontally,
                        Alignment.Vertical.CenterVertically
                    ) {
                        GlanceImageIcon(R.drawable.ic_backward, 26) {
                            getPlayerS()?.toBackSong()
                        }

                        Spacer(GlanceModifier.defaultWeight())

                        GlanceImageIcon(
                            if (isLoopEnabled) R.drawable.ic_repeat_one else R.drawable.ic_repeat,
                            23
                        ) {
                            coroutine.launch { isLoopDB = flowOf(!isLoopEnabled) }
                        }

                        Spacer(GlanceModifier.defaultWeight())


                        GlanceImageIcon(
                            if (isShuffleEnabled) R.drawable.ic_shuffle_square else R.drawable.ic_shuffle,
                            23
                        ) {
                            coroutine.launch { isShuffleDB = flowOf(!isShuffleEnabled) }
                        }

                        Spacer(GlanceModifier.defaultWeight())

                        GlanceImageIcon(R.drawable.ic_forward, 26) {
                            getPlayerS()?.toNextSong()
                        }
                    }
                }

                Column(GlanceModifier.padding(5.dp).cornerRadius(100.dp).clickable {
                    if (music.isPlaying()) getPlayerS()?.pause()
                    else getPlayerS()?.play()
                }.background(Color.White).padding(6.dp)) {
                    when (music.state) {
                        YoutubePlayerState.PLAYING -> GlanceImageIcon(R.drawable.ic_pause, 20)

                        YoutubePlayerState.BUFFERING, YoutubePlayerState.UNSTARTED -> CircularProgressIndicator(
                            GlanceModifier.size(26.dp), ColorProvider(MainColor)
                        )

                        else -> GlanceImageIcon(R.drawable.ic_play, 20)
                    }
                }
            }
        } else {
            GlanceTextItemBold(LocalContext.current.getString(R.string.no_song_played))
        }
    }
}

class PlayingSongWidgetReceiver : GlanceAppWidgetReceiver() {
    override val glanceAppWidget: GlanceAppWidget = PlayingSongWidget()
}