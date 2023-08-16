package com.rizwansayyed.zene.service.musicplayer

import android.app.Notification
import android.app.PendingIntent
import android.content.ComponentName
import android.content.Intent
import android.graphics.Bitmap
import android.support.v4.media.session.MediaControllerCompat
import android.widget.ImageView
import androidx.core.app.NotificationCompat
import androidx.core.content.ContextCompat
import androidx.core.graphics.drawable.toBitmap
import androidx.core.net.toUri
import androidx.media3.common.AudioAttributes
import androidx.media3.common.C
import androidx.media3.common.C.AUDIO_CONTENT_TYPE_MUSIC
import androidx.media3.common.ForwardingPlayer
import androidx.media3.common.MediaItem
import androidx.media3.common.MediaMetadata
import androidx.media3.common.Player
import androidx.media3.common.util.UnstableApi
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.session.MediaController
import androidx.media3.session.MediaNotification
import androidx.media3.session.MediaSession
import androidx.media3.session.MediaSessionService
import androidx.media3.session.MediaStyleNotificationHelper
import androidx.media3.session.SessionToken
import androidx.media3.ui.PlayerNotificationManager
import com.google.common.util.concurrent.MoreExecutors
import com.rizwansayyed.zene.R
import com.rizwansayyed.zene.ui.home.MainActivity
import com.rizwansayyed.zene.utils.Utils.EXTRA.PLAY_URL_PATH


class MediaPlayerServiceTestT : MediaSessionService(), MediaSession.Callback, Player.Listener {

    private var mediaSession: MediaSession? = null
    override fun onGetSession(controllerInfo: MediaSession.ControllerInfo): MediaSession? =
        mediaSession

    private var mediaURL = ""

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        mediaURL = intent?.getStringExtra(PLAY_URL_PATH) ?: ""
        onCreate()
        return super.onStartCommand(intent, flags, startId)
    }

    @androidx.annotation.OptIn(androidx.media3.common.util.UnstableApi::class)
    override fun onCreate() {
        super.onCreate()

        if (mediaURL.isEmpty()) return

        val player = ExoPlayer.Builder(this)
            .setAudioAttributes(
                AudioAttributes.Builder().setContentType(AUDIO_CONTENT_TYPE_MUSIC)
//                    .setUsage(USAGE_MEDIA)
                    .build(), true
            )
            .setHandleAudioBecomingNoisy(true)
            .setWakeMode(C.WAKE_MODE_LOCAL)
            .build()


        val forwardingPlayer =
            object : ForwardingPlayer(player) {
                override fun getAvailableCommands(): Player.Commands {
                    return super.getAvailableCommands()
                        .buildUpon()
                        .remove(COMMAND_SEEK_TO_PREVIOUS)
                        .remove(COMMAND_SEEK_TO_NEXT)
                        .build()
                }
            }
        mediaSession = MediaSession.Builder(this, forwardingPlayer)
            .build()
//
//        val forwardingPlayer = object : ForwardingPlayer(player) {
//            override fun play() {
//
//                "played".showToast()
//                // Add custom logic
//                super.play()
//            }
//
//            override fun seekTo(positionMs: Long) {
//                super.seekTo(positionMs)
//
//            }
//
//
//            override fun pause() {
//                super.pause()
//
//                "pause".showToast()
//            }
//        }
//        mediaSession = MediaSession.Builder(this, player).setCallback(this).build()


        val mediaController = MediaControllerCompat(this, mediaSession?.sessionCompatToken!!)
//
//        val notificationManager = PlayerNotificationManager.Builder(
//            this,
//            111, "Music app"
//        )
//            .setChannelNameResourceId(R.string.app_name)
//            .setChannelDescriptionResourceId(R.string.app_name)
//            .setMediaDescriptionAdapter(DescAdapter(mediaController))
//            .setNotificationListener(notiListener)
//            .build()
//
//        val notificationManager = PlayerNotificationManager.Builder(this, 111, "Music app")
//            .setMediaDescriptionAdapter(DescAdapter())
//            .setNotificationListener(notiListener)
//            .setChannelImportance(IMPORTANCE_DEFAULT)
//            .setSmallIconResourceId(R.mipmap.logo)
//            .setChannelDescriptionResourceId(R.string.app_name)
//            .setChannelDescriptionResourceId(R.string.app_name)
//            .setChannelNameResourceId(R.string.app_name)
//            .build()
//
//        notificationManager.setPlayer(player)
//        notificationManager.setPriority(PRIORITY_DEFAULT)
//        notificationManager.setUseRewindAction(false)
//        notificationManager.setUseFastForwardAction(true)
//        notificationManager.setUseFastForwardAction(true)


//        notificationManager.setPlayer(player)


        val itemss = MediaItem.Builder()
            .setUri(mediaURL)
            .setMediaId("id")
            .setMediaMetadata(
                MediaMetadata.Builder()
                    .setTitle("Test title")
                    .setArtist("Test artist")
                    .setArtworkUri("https://images.unsplash.com/photo-1679663877752-c00ad7f1c5c5?ixlib=rb-4.0.3&ixid=MnwxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8&auto=format&fit=crop&w=2940&q=80".toUri())
                    .build()
            )
            .build()

        player.addMediaItem(itemss)
        player.addListener(this)
        player.playWhenReady = true
        player.prepare()

        val sessionToken  by lazy {
            SessionToken(this, ComponentName(this, MediaPlayerServiceTestT::class.java))
        }

        val controllerFuture by lazy {
            MediaController.Builder(this, sessionToken).buildAsync()
        }

        controllerFuture.addListener({
            // MediaController is available here with controllerFuture.get()
        }, MoreExecutors.directExecutor())


//        val adapter = SimpleMediaNotificationAdapter(this, mediaSession!!.sessionActivity)
//
//        PlayerNotificationManager.Builder(this, 22, "channel_idd")
//            .setMediaDescriptionAdapter(adapter)
//            .setSmallIconResourceId(R.mipmap.logo)
//            .build()
//            .also {

//                it.setMediaSessionToken(mediaSession!!.sessionCompatToken)
//                it.setUseFastForwardActionInCompactView(true)
//                it.setUseRewindActionInCompactView(true)
//                it.setUseNextActionInCompactView(false)
//                it.setPriority(NotificationCompat.PRIORITY_LOW)
//                it.setPlayer(player)
//            }

    }

    @UnstableApi
    val notiListener = object : PlayerNotificationManager.NotificationListener {
        override fun onNotificationCancelled(notificationId: Int, dismissedByUser: Boolean) {
            super.onNotificationCancelled(notificationId, dismissedByUser)
            stopForeground(true)
            //pause if layin
        }

        override fun onNotificationPosted(
            notificationId: Int,
            notification: Notification,
            ongoing: Boolean
        ) {
            super.onNotificationPosted(notificationId, notification, ongoing)
            startForeground(notificationId, notification)
        }
    }

    @UnstableApi
    inner class DescAdapter(mediaController: Any) :
        PlayerNotificationManager.MediaDescriptionAdapter {
        override fun getCurrentContentTitle(player: Player): CharSequence {
            return player.currentMediaItem?.mediaMetadata?.title ?: ""
        }

        override fun createCurrentContentIntent(player: Player): PendingIntent? {
            val open = Intent(this@MediaPlayerServiceTestT, MainActivity::class.java)

            return PendingIntent.getActivities(
                this@MediaPlayerServiceTestT, 0,
                arrayOf(open), PendingIntent.FLAG_IMMUTABLE
            )
        }

        override fun getCurrentContentText(player: Player): CharSequence? {
            return "Nooooooo"
        }

        override fun getCurrentLargeIcon(
            player: Player,
            callback: PlayerNotificationManager.BitmapCallback
        ): Bitmap? {
            val view = ImageView(this@MediaPlayerServiceTestT).apply {
                setImageURI(player.currentMediaItem?.mediaMetadata?.artworkUri)
            }.drawable
                ?: return ContextCompat.getDrawable(this@MediaPlayerServiceTestT, R.mipmap.logo)
                    ?.toBitmap()

            return view.toBitmap()
        }

    }


//    @UnstableApi
//    val notificationProvider = object : MediaNotification.Provider {
//        override fun createNotification(
//            mediaSession: MediaSession,
//            customLayout: ImmutableList<CommandButton>,
//            actionFactory: MediaNotification.ActionFactory,
//            onNotificationChangedCallback: MediaNotification.Provider.Callback
//        ): MediaNotification {
//            val intentAction = Intent(this@MediaPlayerService, SongNotifyReceiver::class.java)
//
////This is optional if you have more than one buttons and want to differentiate between two
//
////This is optional if you have more than one buttons and want to differentiate between two
//            intentAction.putExtra("action", "actionName")
//
//            val pIntentlogin = PendingIntent.getBroadcast(
//                this@MediaPlayerService,
//                1,
//                intentAction,
//                PendingIntent.FLAG_UPDATE_CURRENT
//            )
//
//            val notificationBuilder = NotificationCompat.Builder(this@MediaPlayerService, "ddd")
//                .setContentTitle("the songs")
//                .setContentText("the texts")
//                .setSmallIcon(R.mipmap.logo)
//                .setPriority(NotificationCompat.PRIORITY_LOW)
//                .addAction(android.R.drawable.ic_media_play, "Play", pIntentlogin)
//                .addAction(android.R.drawable.ic_media_pause, "Pause", pIntentlogin)
//                .addAction(android.R.drawable.ic_media_next, "Next", pIntentlogin)
//                .addAction(
//                    android.R.drawable.ic_media_previous,
//                    "Previous",
//                    pIntentlogin
//                )
//            return MediaNotification(5, notificationBuilder.build())
//        }
//
//        override fun handleCustomCommand(
//            session: MediaSession,
//            action: String,
//            extras: Bundle
//        ): Boolean {
//            return true
//        }
//
//    }

//    override fun onPostConnect(session: MediaSession, controller: MediaSession.ControllerInfo) {
//        val playPauseButton = CommandButton.Builder()
//            .setDisplayName(if (session.player.isPlaying) "Play" else "Pause")
//            .setIconResId(if (session.player.isPlaying) android.R.drawable.ic_media_play else android.R.drawable.ic_media_pause)
//            .setPlayerCommand(Player.COMMAND_PLAY_PAUSE)
//            .build()
//
//        val likeButton = CommandButton.Builder()
//            .setDisplayName("Like")
//            .setIconResId(android.R.drawable.ic_media_pause)
//            .setSessionCommand(SessionCommand(SessionCommand.COMMAND_CODE_SESSION_SET_RATING))
//            .build()
//
//        val favoriteButton = CommandButton.Builder()
//            .setDisplayName("Save to favorites")
//            .setIconResId(android.R.drawable.ic_media_ff)
//            .setSessionCommand(SessionCommand("SAVE_TO_FAVORITES", Bundle()))
//            .build()
//
//        // Pass in a list of the controls that the client app should display to users. The arrangement
//        // of these controls in the UI is managed by the client app.
//        session.setCustomLayout(controller, listOf(playPauseButton, likeButton, favoriteButton))
//        // Use session.setCustomLayout(listOf(playPauseButton, likeButton, favoriteButton))
//        // for clients using a legacy controller
//        super.onPostConnect(session, controller)
//    }

    private fun updateNotification(/*parameter*/) {

//        nBuilder.setContentTitle("text")
//        nBuilder.setContentText("subtext")
//        nManager.notify(NOTIFICATION_ID,nBuilder.build())
    }

    @androidx.annotation.OptIn(androidx.media3.common.util.UnstableApi::class)
    private fun updateNotification(session: MediaSession): MediaNotification {
        val notify = NotificationCompat.Builder(this, "Radio")
            .setSmallIcon(R.mipmap.logo)
            // This is globally changed every time when
            // I add a new MediaItem from background service
            .setContentTitle("Test title")
            .setContentText("Test artist")
            .setStyle(MediaStyleNotificationHelper.MediaStyle(session))
            .build()

        return MediaNotification(1, notify)
    }


    override fun onDestroy() {
        mediaSession?.run {
            player.release()
            release()
            mediaSession = null
        }

        //
        super.onDestroy()
    }


//    @androidx.annotation.OptIn(androidx.media3.common.util.UnstableApi::class)
//    fun createNotificationSession(session: MediaSession): NotificationCompat.Builder {
//        val notificationManager: NotificationManager =
//            this.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
//            notificationManager.createNotificationChannel(
//                NotificationChannel(
//                    "channel_idd",
//                    "Channel",
//                    NotificationManager.IMPORTANCE_HIGH
//                )
//            )
//        }
//
//        // NotificationCompat.Builder here.
//
//
//        return NotificationCompat.Builder(this, "channel_idd")
//            .setSmallIcon(R.mipmap.logo)
//            .setContentTitle("your Content title")
//            .setContentText("your content text")
//            // set session here
//            .setStyle(MediaStyleNotificationHelper.MediaStyle(session))
//    }
//
//    @UnstableApi
//    class SimpleMediaNotificationAdapter(
//        private val context: Context,
//        private val pendingIntent: PendingIntent?
//    ) : PlayerNotificationManager.MediaDescriptionAdapter {
//
//        override fun getCurrentContentTitle(player: Player): CharSequence =
//            player.mediaMetadata.albumTitle ?: ""
//
//        override fun createCurrentContentIntent(player: Player): PendingIntent? =
//            pendingIntent
//
//        override fun getCurrentContentText(player: Player): CharSequence =
//            player.mediaMetadata.displayTitle ?: ""
//
//        override fun getCurrentLargeIcon(
//            player: Player,
//            callback: PlayerNotificationManager.BitmapCallback
//        ): Bitmap {
//            return runBlocking(Dispatchers.IO) {
//                val loader = ImageLoader(context)
//                val request = ImageRequest.Builder(context)
//                    .data("https://filmfaker.com/wp-content/uploads/2023/03/WhatsApp-Image-2023-03-17-at-10.47.39-AM-1024x1024.jpeg")
//                    .allowHardware(false)
//                    .build()
//
//                val result = (loader.execute(request) as SuccessResult).drawable
//                val bitmap = (result as BitmapDrawable).bitmap
//                bitmap
//            }
//        }
//
//    }


//
//    fun getMetaDataFromMediaClass(): MediaMetadata {
//        return MediaMetadata.Builder()
//            .setTitle(media.title)
//            .setAlbumTitle(media.title)
//            .setDisplayTitle(media.title)
//            .setArtist(media.subtitle)
//            .setAlbumArtist(media.subtitle)
//            .setArtworkUri(media.imageURL.toUri())
//            .build()
//    }
//
//    fun performPlayMedia() {
//        val metadata = getMetaDataFromMediaClass(media)
//        val mediaItem = MediaItem.Builder()
//            .setUri(media.dataURL)
//            .setMediaId(media.mediaID)
//            .setMediaMetadata(metadata)
//            .build()
//
//        player.apply {
//            setMediaItem(mediaItem)
//            prepare()
//            play()
//        }
//    }



    private val playerListener = object: Player.Listener {
        override fun onEvents(player: Player, events: Player.Events) {
            if (events.contains(Player.EVENT_MEDIA_ITEM_TRANSITION)) {
                player.currentMediaItem?.let { mediaItem ->
//                    nowPlaying.update {
//                        mediaItem
//                    }
                }
            }

            if (events.contains(Player.EVENT_IS_PLAYING_CHANGED)) {
//                isPlaying.update {
//                    player.isPlaying
//                }
            }

            if (events.contains(Player.EVENT_MEDIA_ITEM_TRANSITION) ||
                events.contains(Player.EVENT_PLAY_WHEN_READY_CHANGED) ||
                events.contains(Player.EVENT_PLAYBACK_STATE_CHANGED)) {
                if (player.duration > 0) {
//                    duration.update {
//                        player.duration
//                    }
                }
            }
        }
    }
}