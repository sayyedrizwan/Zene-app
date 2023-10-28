package com.rizwansayyed.zene.service


import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Intent
import android.graphics.BitmapFactory
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.content.ContextCompat
import androidx.media3.common.Player
import androidx.media3.common.util.UnstableApi
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.exoplayer.source.DefaultMediaSourceFactory
import androidx.media3.session.MediaController
import androidx.media3.session.MediaSession
import androidx.media3.session.MediaSessionService
import androidx.media3.ui.PlayerNotificationManager
import com.rizwansayyed.zene.R
import com.rizwansayyed.zene.domain.MusicData
import com.rizwansayyed.zene.domain.MusicType
import com.rizwansayyed.zene.service.Utils.PLAYER_NOTIFICATION_CHANNEL_ID
import com.rizwansayyed.zene.service.Utils.PLAYER_NOTIFICATION_CHANNEL_NAME
import com.rizwansayyed.zene.service.Utils.PLAYER_NOTIFICATION_ID
import com.rizwansayyed.zene.service.Utils.toMediaItem
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.InputStream
import javax.inject.Inject


@UnstableApi
@AndroidEntryPoint
class PlayerService : MediaSessionService() {

    @Inject
    lateinit var player: ExoPlayer

    @Inject
    lateinit var mediaSession: MediaSession

    override fun onGetSession(controllerInfo: MediaSession.ControllerInfo) = mediaSession

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        super.onStartCommand(intent, flags, startId)
        CoroutineScope(Dispatchers.IO).launch {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                createNotificationChannel()
            }
            buildNotification()
            startForegroundNotification()
        }

        CoroutineScope(Dispatchers.Main).launch {
            val music = MusicData(
                "https://wallpaperaccess.com/download/manifest-tv-show-8350796",
                "the name",
                "the artiststssss",
                "233",
                MusicType.MUSIC
            )

            val i =
                music.toMediaItem("https://rs1.seedr.cc/ff_get/1653950113/02%20-%20Right%20Heres%20The%20Spot.mp3?st=udLh1c2SzDBFG2VXV2bORA&e=1698590797")

            val l = object : Player.Listener {

            }

//            if (controllerFuture == null) {
//                val controllerFuture = MediaController.Builder(this@PlayerService, sessionToken).buildAsync()
//                controllerFuture.addListener({
//                    val controller = controllerFuture.get()
//                    controller.addListener(l)
//                }, ContextCompat.getMainExecutor(this@PlayerService))
//            }


            player.apply {
                setMediaItem(music.toMediaItem("https://rs1.seedr.cc/ff_get/1653950113/02%20-%20Right%20Heres%20The%20Spot.mp3?st=udLh1c2SzDBFG2VXV2bORA&e=1698590797"))
                playWhenReady = true
                prepare()
                play()
            }
        }
        return START_STICKY
    }

    override fun onDestroy() {
        player.release()
        mediaSession.release()
        super.onDestroy()
    }

    @UnstableApi
    private suspend fun buildNotification() {
        withContext(Dispatchers.IO) {
//            val loader = ImageLoader(this@PlayerService)
//            val request = ImageRequest.Builder(this@PlayerService)
//                .data("https://wallpaperaccess.com/download/manifest-tv-show-8350796")
//                .allowHardware(false)
//                .build()
//
//            val result = (loader.execute(request) as SuccessResult).drawable
//            val bitmap = (result as BitmapDrawable).bitmap

            val imageStream: InputStream = resources.openRawResource(R.raw.temp_img)
            val bitmap = BitmapFactory.decodeStream(imageStream)


            withContext(Dispatchers.Main) {
                PlayerNotificationManager.Builder(
                    this@PlayerService,
                    PLAYER_NOTIFICATION_ID,
                    PLAYER_NOTIFICATION_CHANNEL_ID
                )
                    .setMediaDescriptionAdapter(
                        SimpleMediaNotificationAdapter(
                            context = this@PlayerService,
                            pendingIntent = mediaSession.sessionActivity,
                            bitmap
                        )
                    )
                    .setSmallIconResourceId(R.mipmap.logo)
                    .build()
                    .also {
                        it.setUseFastForwardActionInCompactView(true)
                        it.setUseRewindActionInCompactView(true)
                        it.setUseNextActionInCompactView(false)
                        it.setPriority(NotificationCompat.PRIORITY_LOW)
                        it.setMediaSessionToken(mediaSession.sessionCompatToken)
                        it.setPlayer(player)
                    }
            }
        }
    }

    private fun startForegroundNotification() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val notification = Notification.Builder(this, PLAYER_NOTIFICATION_CHANNEL_ID)
                .setCategory(Notification.CATEGORY_SERVICE)
                .build()
            startForeground(PLAYER_NOTIFICATION_ID, notification)
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun createNotificationChannel() {
        val notificationManager = NotificationManagerCompat.from(this)
        val channel = NotificationChannel(
            PLAYER_NOTIFICATION_CHANNEL_ID,
            PLAYER_NOTIFICATION_CHANNEL_NAME,
            NotificationManager.IMPORTANCE_LOW
        )
        notificationManager.createNotificationChannel(channel)
    }


}