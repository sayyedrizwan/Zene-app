package com.rizwansayyed.zene.utils.share

import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Canvas
import android.os.Build
import android.util.Base64
import android.view.View
import androidx.compose.ui.platform.ComposeView
import androidx.core.content.FileProvider
import androidx.core.content.edit
import androidx.core.graphics.createBitmap
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKey
import com.rizwansayyed.zene.R
import com.rizwansayyed.zene.data.model.MusicDataTypes.AI_MUSIC
import com.rizwansayyed.zene.data.model.MusicDataTypes.ALBUMS
import com.rizwansayyed.zene.data.model.MusicDataTypes.ARTISTS
import com.rizwansayyed.zene.data.model.MusicDataTypes.MOVIES_SHOW
import com.rizwansayyed.zene.data.model.MusicDataTypes.NEWS
import com.rizwansayyed.zene.data.model.MusicDataTypes.NONE
import com.rizwansayyed.zene.data.model.MusicDataTypes.PLAYLISTS
import com.rizwansayyed.zene.data.model.MusicDataTypes.PODCAST
import com.rizwansayyed.zene.data.model.MusicDataTypes.PODCAST_AUDIO
import com.rizwansayyed.zene.data.model.MusicDataTypes.PODCAST_CATEGORIES
import com.rizwansayyed.zene.data.model.MusicDataTypes.RADIO
import com.rizwansayyed.zene.data.model.MusicDataTypes.SONGS
import com.rizwansayyed.zene.data.model.MusicDataTypes.TEXT
import com.rizwansayyed.zene.data.model.MusicDataTypes.VIDEOS
import com.rizwansayyed.zene.data.model.ZeneMusicData
import com.rizwansayyed.zene.datastore.DataStorageManager.userInfo
import com.rizwansayyed.zene.di.ZeneBaseApplication.Companion.context
import com.rizwansayyed.zene.ui.connect_status.ConnectStatusActivity
import com.rizwansayyed.zene.utils.MainUtils.copyTextToClipboard
import com.rizwansayyed.zene.utils.MainUtils.isAppInstalled
import com.rizwansayyed.zene.utils.MainUtils.moshi
import com.rizwansayyed.zene.utils.MainUtils.toast
import com.rizwansayyed.zene.utils.SnackBarManager
import com.rizwansayyed.zene.utils.URLSUtils.ZENE_AI_MUSIC
import com.rizwansayyed.zene.utils.URLSUtils.ZENE_ARTIST
import com.rizwansayyed.zene.utils.URLSUtils.ZENE_M
import com.rizwansayyed.zene.utils.URLSUtils.ZENE_MIX
import com.rizwansayyed.zene.utils.URLSUtils.ZENE_NEWS
import com.rizwansayyed.zene.utils.URLSUtils.ZENE_PODCAST
import com.rizwansayyed.zene.utils.URLSUtils.ZENE_PODCAST_SERIES
import com.rizwansayyed.zene.utils.URLSUtils.ZENE_RADIO
import com.rizwansayyed.zene.utils.URLSUtils.ZENE_SONG
import com.rizwansayyed.zene.utils.URLSUtils.ZENE_URL
import com.rizwansayyed.zene.utils.URLSUtils.ZENE_VIDEO
import com.rizwansayyed.zene.utils.URLSUtils.connectShareURL
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.launch
import java.io.File
import java.io.FileOutputStream
import java.util.UUID


enum class SharingContentType {
    COPY, SHARE_TO, CONNECT, WHATS_APP, INSTAGRAM, SNAPCHAT, FACEBOOK, X, PINTEREST
}

object ShareContentUtils {

    fun shareConnectURL() = CoroutineScope(Dispatchers.IO).launch {
        val userInfo = userInfo.firstOrNull() ?: return@launch
        if ((userInfo.username?.length ?: 0) <= 1) {
            context.resources.getString(R.string.enter_a_valid_username)
            return@launch
        }

        val url = connectShareURL(userInfo.username ?: "")
        Intent().apply {
            action = Intent.ACTION_SEND
            putExtra(Intent.EXTRA_TEXT, url)
            type = "text/plain"
            flags = Intent.FLAG_ACTIVITY_NEW_TASK
            context.startActivity(this)
        }
    }

    fun shareTheData(data: ZeneMusicData?, type: SharingContentType, view: ComposeView) {
        val fileSharingImg = File(context.cacheDir, "sharing_img.jpg")
        fileSharingImg.deleteRecursively()

        try {
            val bitmap = createBitmap(view.width, view.height)
            val canvas = Canvas(bitmap)

            view.measure(
                View.MeasureSpec.makeMeasureSpec(view.width, View.MeasureSpec.EXACTLY),
                View.MeasureSpec.makeMeasureSpec(view.height, View.MeasureSpec.EXACTLY)
            )
            view.layout(0, 0, view.measuredWidth, view.measuredHeight)

            view.draw(canvas)

            val fOut = FileOutputStream(fileSharingImg)
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fOut)
            fOut.flush()
            fOut.close()
        } catch (e: Exception) {
            e.printStackTrace()
        }

        val sharingText = when (data?.type()) {
            NONE -> ""
            SONGS -> String.format(
                context.resources.getString(R.string.enjoy_the_song_on_zene), data.name
            )

            RADIO -> String.format(
                context.resources.getString(R.string.enjoy_free_radio_on_zene), data.name
            )

            VIDEOS -> context.resources.getString(R.string.enjoy_free_video_on_zene)
            PLAYLISTS, ALBUMS -> String.format(
                context.resources.getString(R.string.enjoy_free_mix_playlist_album_on_zene),
                data.name
            )

            ARTISTS -> String.format(
                context.resources.getString(R.string.listen_to_on_zene), data.name
            )

            PODCAST -> String.format(
                context.resources.getString(R.string.enjoy_free_podcast_series_on_zene), data.name
            )

            PODCAST_AUDIO -> String.format(
                context.resources.getString(R.string.enjoy_free_podcast_on_zene), data.name
            )

            PODCAST_CATEGORIES -> ""
            NEWS -> String.format(
                context.resources.getString(R.string.enjoy_free_news_on_zene), data.name
            )

            MOVIES_SHOW -> String.format(
                context.resources.getString(R.string.about_movie_on_zene), data.name
            )

            AI_MUSIC -> String.format(
                context.resources.getString(R.string.enjoy_free_ai_music_on_zene), data.name
            )

            TEXT -> ""
            null -> ""
        }

        val url = generateShareUrl(data)

        when (type) {
            SharingContentType.COPY -> {
                SnackBarManager.showMessage(context.getString(R.string.copied_link))
                copyTextToClipboard(url)
            }

            SharingContentType.SHARE_TO -> Intent().apply {
                action = Intent.ACTION_SEND
                putExtra(Intent.EXTRA_TEXT, url)
                setType("text/plain")
                flags = Intent.FLAG_ACTIVITY_NEW_TASK
                context.startActivity(this)
            }

            SharingContentType.CONNECT -> Intent(context, ConnectStatusActivity::class.java).apply {
                flags = Intent.FLAG_ACTIVITY_NEW_TASK
                val json = moshi.adapter(ZeneMusicData::class.java).toJson(data)
                putExtra(Intent.ACTION_SEND, json)
                putExtra(Intent.ACTION_ATTACH_DATA, fileSharingImg.absolutePath)
                context.startActivity(this)
            }

            SharingContentType.WHATS_APP -> startIntentImageSharing(
                "com.whatsapp", "$sharingText \n$url", fileSharingImg
            )

            SharingContentType.INSTAGRAM -> startIntentImageSharing(
                "com.instagram.android", "$sharingText \n$url", fileSharingImg
            )

            SharingContentType.SNAPCHAT -> startIntentImageSharing(
                "com.snapchat.android", "$sharingText \n$url", fileSharingImg
            )

            SharingContentType.FACEBOOK -> {
                if (isAppInstalled("com.facebook.lite")) startIntentImageSharing(
                    "com.facebook.lite", "$sharingText \n$url", fileSharingImg
                )
                else startIntentImageSharing(
                    "com.facebook.katana", "$sharingText \n$url", fileSharingImg
                )
            }

            SharingContentType.X -> startIntentImageSharing(
                "com.twitter.android", "$sharingText \n$url", fileSharingImg
            )

            SharingContentType.PINTEREST -> startIntentImageSharing(
                "com.pinterest", "$sharingText \n$url", fileSharingImg
            )
        }
    }

    fun generateShareUrl(data: ZeneMusicData?): String {
        val id = data?.id

        return when (data?.type()) {
            NONE -> ZENE_URL
            SONGS -> "$ZENE_URL$ZENE_SONG${id}"
            RADIO -> "$ZENE_URL$ZENE_RADIO${id}"
            VIDEOS -> "$ZENE_URL$ZENE_VIDEO${id}"
            PLAYLISTS, ALBUMS -> "$ZENE_URL$ZENE_MIX${id}"
            ARTISTS -> "$ZENE_URL$ZENE_ARTIST${id}"
            PODCAST -> "$ZENE_URL$ZENE_PODCAST_SERIES${id}"
            PODCAST_AUDIO -> "$ZENE_URL$ZENE_PODCAST${data.id?.replace("/", "_")}"
            PODCAST_CATEGORIES -> ZENE_URL
            NEWS -> "$ZENE_URL$ZENE_NEWS" + Base64.encodeToString(
                data.id?.toByteArray(Charsets.UTF_8), Base64.NO_WRAP
            ).replace("=", "")

            MOVIES_SHOW -> "$ZENE_URL$ZENE_M${data.id}"
            AI_MUSIC -> "$ZENE_URL$ZENE_AI_MUSIC${data.id}"
            TEXT -> ZENE_URL
            null -> ZENE_URL
        }
    }

    private fun startIntentImageSharing(n: String, text: String, image: File) {
        val uri = FileProvider.getUriForFile(context, "${context.packageName}.provider", image)
        val intent = Intent(Intent.ACTION_SEND)
        intent.setType("text/plain")
        intent.setPackage(n)
        intent.putExtra(Intent.EXTRA_TEXT, text)
        intent.putExtra(Intent.EXTRA_TITLE, text)

        if (image.exists()) {
            intent.putExtra(Intent.EXTRA_STREAM, uri)
            intent.setType("image/jpeg")
        }
        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK

        try {
            context.startActivity(intent)
        } catch (_: Exception) {
            context.resources.getString(R.string.error_sharing_).toast()
        }
    }

    fun getUniqueDeviceId(): String {
        val masterKeyAlias =
            MasterKey.Builder(context).setKeyScheme(MasterKey.KeyScheme.AES256_GCM).build()

        val sharedPreferences = EncryptedSharedPreferences.create(
            context,
            "secret_device_prefs",
            masterKeyAlias,
            EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
            EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
        )

        var uniqueID = sharedPreferences.getString("unique_device_id", "")

        if (uniqueID == null) {
            val deviceModel = Build.MODEL
            uniqueID = "$deviceModel-${UUID.randomUUID()}"
            sharedPreferences.edit { putString("unique_device_id", uniqueID) }
        }

        return uniqueID
    }
}