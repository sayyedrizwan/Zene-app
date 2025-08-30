package com.rizwansayyed.zene.widgets.likedsongs

import android.content.Context
import android.content.Intent
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.glance.GlanceId
import androidx.glance.GlanceModifier
import androidx.glance.LocalContext
import androidx.glance.action.clickable
import androidx.glance.appwidget.CircularProgressIndicator
import androidx.glance.appwidget.GlanceAppWidget
import androidx.glance.appwidget.GlanceAppWidgetReceiver
import androidx.glance.appwidget.cornerRadius
import androidx.glance.appwidget.lazy.LazyColumn
import androidx.glance.appwidget.lazy.items
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
import androidx.glance.layout.width
import androidx.glance.layout.wrapContentHeight
import androidx.glance.unit.ColorProvider
import com.rizwansayyed.zene.R
import com.rizwansayyed.zene.data.implementation.ZeneAPIImplementation
import com.rizwansayyed.zene.data.model.MusicDataTypes
import com.rizwansayyed.zene.data.model.ZeneMusicData
import com.rizwansayyed.zene.datastore.DataStorageManager
import com.rizwansayyed.zene.ui.main.MainActivity
import com.rizwansayyed.zene.ui.theme.MainColor
import com.rizwansayyed.zene.utils.URLSUtils.LIKED_SONGS_ON_ZENE
import com.rizwansayyed.zene.utils.safeLaunch
import com.rizwansayyed.zene.utils.share.MediaContentUtils
import com.rizwansayyed.zene.widgets.utils.GlanceImage
import com.rizwansayyed.zene.widgets.utils.GlanceImageIcon
import com.rizwansayyed.zene.widgets.utils.GlanceTextItemBold
import com.rizwansayyed.zene.widgets.utils.GlanceTextItemNormal
import dagger.hilt.EntryPoint
import dagger.hilt.InstallIn
import dagger.hilt.android.EntryPointAccessors
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.cancel
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch

class LikedMediaWidgets : GlanceAppWidget() {

    @EntryPoint
    @InstallIn(SingletonComponent::class)
    interface GlanceZeneAPIEntryPoint {
        fun repository(): ZeneAPIImplementation
    }

    override suspend fun provideGlance(context: Context, id: GlanceId) {
        val zeneAPI =
            EntryPointAccessors.fromApplication(context, GlanceZeneAPIEntryPoint::class.java)

        provideContent {
            val itemList = remember { mutableStateListOf<ZeneMusicData>() }
            var isLoading by remember { mutableStateOf(false) }
            var isLoggedIn by remember { mutableStateOf(false) }

            fun getLikesItems() = CoroutineScope(Dispatchers.IO).safeLaunch {
                val email = DataStorageManager.userInfo.firstOrNull()
                val likedPlaylistId = "${email?.email}$LIKED_SONGS_ON_ZENE"
                if (email?.isLoggedIn() == true) {
                    itemList.clear()
                    zeneAPI.repository().myPlaylistsSongs(likedPlaylistId, 0).onStart {
                        isLoading = true
                    }.catch {
                        isLoading = false
                    }.collectLatest {
                        isLoading = false
                        itemList.clear()
                        itemList.addAll(it)
                    }
                } else {
                    isLoggedIn = false
                }

                if (isActive) cancel()
            }


            LazyColumn(
                GlanceModifier.fillMaxSize().padding(horizontal = 10.dp).padding(top = 20.dp)
                    .cornerRadius(14.dp).background(MainColor)
            ) {
                item {
                    Row(
                        GlanceModifier.padding(horizontal = 5.dp).fillMaxWidth(),
                        Alignment.Horizontal.CenterHorizontally,
                        Alignment.Vertical.CenterVertically
                    ) {
                        Box(GlanceModifier.clickable {
                            Intent(context, MainActivity::class.java).apply {
                                flags = Intent.FLAG_ACTIVITY_NEW_TASK
                                context.startActivity(this)
                            }
                        }) {
                            GlanceImageIcon(R.mipmap.logo, 39, null)
                        }
                        Spacer(GlanceModifier.width(10.dp))
                        GlanceTextItemBold(LocalContext.current.getString(R.string.liked_items), 18)
                        Spacer(GlanceModifier.defaultWeight())
                        Box(GlanceModifier.clickable { getLikesItems() }
                            .padding(horizontal = 6.dp)) {
                            GlanceImageIcon(R.drawable.ic_reload, 26, Color.White)
                        }
                    }
                }

                item { Spacer(GlanceModifier.height(40.dp)) }

                item {
                    if (isLoggedIn) {
                        GlanceTextItemBold(
                            LocalContext.current.getString(R.string.login_to_view_liked_items), 14
                        )
                    }
                }

                item {
                    if (isLoading) Box(GlanceModifier.fillMaxWidth(), Alignment.Center) {
                        CircularProgressIndicator(
                            GlanceModifier.size(46.dp), ColorProvider(Color.White)
                        )
                    }
                }

                items(itemList) {
                    LikedWidgetItems(it, context)
                }

            }

            LaunchedEffect(Unit) { getLikesItems() }
        }
    }
}


@Composable
fun LikedWidgetItems(data: ZeneMusicData, context: Context) {
    Box(GlanceModifier.fillMaxWidth().clickable { MediaContentUtils.startMedia(data) }) {
        Column(
            GlanceModifier.fillMaxWidth().padding(bottom = 30.dp),
            Alignment.Vertical.CenterVertically,
            Alignment.Horizontal.CenterHorizontally
        ) {
            Row(
                GlanceModifier.fillMaxWidth(),
                Alignment.Horizontal.CenterHorizontally,
                Alignment.Vertical.CenterVertically
            ) {
                GlanceImage(data.thumbnail, 80, context)
                Spacer(GlanceModifier.width(8.dp))
                Column(
                    GlanceModifier.defaultWeight().wrapContentHeight(),
                    Alignment.Vertical.CenterVertically
                ) {
                    if (data.type() == MusicDataTypes.VIDEOS)
                        GlanceTextItemBold(data.name, 14, 2)
                    else
                        GlanceTextItemBold(data.name, 14)

                    Spacer(GlanceModifier.height(5.dp))
                    GlanceTextItemNormal(data.artists, 13)
                }
            }

            Spacer(GlanceModifier.height(10.dp))
            Spacer(
                GlanceModifier.padding(horizontal = 40.dp).padding(top = 10.dp)
                    .width(40.dp)
                    .cornerRadius(7.dp).background(Color.White).height(1.dp)
            )
        }

        Box(
            GlanceModifier
                .padding(10.dp)
                .cornerRadius(100.dp)
                .background(Color.Black)
        ) {
            if (data.type() == MusicDataTypes.SONGS) {
                GlanceImageIcon(R.drawable.ic_music_note, 14, Color.White)
            } else if (data.type() == MusicDataTypes.AI_MUSIC) {
                GlanceImageIcon(R.drawable.ic_robot_singing, 14, Color.White)
            } else if (data.type() == MusicDataTypes.PODCAST_AUDIO) {
                GlanceImageIcon(R.drawable.ic_podcast, 14, Color.White)
            } else if (data.type() == MusicDataTypes.RADIO) {
                GlanceImageIcon(R.drawable.ic_radio, 14, Color.White)
            } else if (data.type() == MusicDataTypes.VIDEOS) {
                GlanceImageIcon(R.drawable.ic_video_replay, 14, Color.White)
            }
        }
    }
}

class LikedMediaWidgetsReceiver : GlanceAppWidgetReceiver() {
    override val glanceAppWidget: GlanceAppWidget = LikedMediaWidgets()
}