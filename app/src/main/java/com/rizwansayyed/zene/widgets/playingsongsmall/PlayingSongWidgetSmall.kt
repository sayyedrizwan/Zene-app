package com.rizwansayyed.zene.widgets.playingsongsmall

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.Intent.CATEGORY_APP_MUSIC
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
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
import androidx.glance.layout.fillMaxSize
import androidx.glance.layout.padding
import androidx.glance.layout.size
import androidx.glance.unit.ColorProvider
import com.rizwansayyed.zene.R
import com.rizwansayyed.zene.datastore.DataStorageManager.musicPlayerDB
import com.rizwansayyed.zene.datastore.model.MusicPlayerData
import com.rizwansayyed.zene.datastore.model.YoutubePlayerState
import com.rizwansayyed.zene.service.player.PlayerForegroundService.Companion.getPlayerS
import com.rizwansayyed.zene.ui.main.MainActivity
import com.rizwansayyed.zene.ui.theme.MainColor
import com.rizwansayyed.zene.widgets.utils.GlanceImage
import com.rizwansayyed.zene.widgets.utils.GlanceImageIcon
import com.rizwansayyed.zene.widgets.utils.GlanceTextItemBold

class PlayingSongWidgetSmall : GlanceAppWidget() {
    override suspend fun provideGlance(context: Context, id: GlanceId) {
        provideContent {
            val music by musicPlayerDB.collectAsState(null)

            Box(
                GlanceModifier.background(MainColor).cornerRadius(20.dp).fillMaxSize().clickable {
                    Intent(context, MainActivity::class.java).apply {
                        flags = Intent.FLAG_ACTIVITY_NEW_TASK
                        if (music?.data != null) putExtra(Intent.ACTION_SENDTO, CATEGORY_APP_MUSIC)
                        context.startActivity(this)
                    }
                }, Alignment.Center
            ) {

                if (music?.data != null) {
                    GlanceImage(music?.data?.thumbnail, null, context, 0)
                    ShowPlayPauseImage(music!!)
                } else {
                    GlanceTextItemBold(LocalContext.current.getString(R.string.no_song_played))
                }

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
    fun ShowPlayPauseImage(music: MusicPlayerData) {
        Box(GlanceModifier.padding(5.dp).fillMaxSize(), Alignment.Center) {
            Column(GlanceModifier.padding(5.dp).cornerRadius(100.dp).clickable {
                if (music.isPlaying()) getPlayerS()?.pause()
                else getPlayerS()?.play()
            }.background(Color.White).padding(6.dp)) {
                when (music.state) {
                    YoutubePlayerState.PLAYING -> GlanceImageIcon(R.drawable.ic_pause, 23)

                    YoutubePlayerState.BUFFERING, YoutubePlayerState.UNSTARTED -> CircularProgressIndicator(
                        GlanceModifier.size(26.dp), ColorProvider(MainColor)
                    )

                    else -> GlanceImageIcon(R.drawable.ic_play, 23)
                }
            }
        }
    }

}


class PlayingSongWidgetSmallReceiver : GlanceAppWidgetReceiver() {
    override val glanceAppWidget: GlanceAppWidget = PlayingSongWidgetSmall()
}