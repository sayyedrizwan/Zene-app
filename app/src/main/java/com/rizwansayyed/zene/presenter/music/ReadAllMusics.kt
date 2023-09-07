package com.rizwansayyed.zene.presenter.music

import android.provider.MediaStore
import android.util.Log
import androidx.core.database.getStringOrNull
import com.rizwansayyed.zene.BaseApplication.Companion.context
import com.rizwansayyed.zene.utils.Utils.showToast


class ReadAllMusics {

    fun allMusic() {
        val uri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI
        val projection = arrayOf(
            MediaStore.Audio.AudioColumns.DATA,
            MediaStore.Audio.AudioColumns.TITLE, MediaStore.Audio.AudioColumns.ALBUM,
            MediaStore.Audio.ArtistColumns.ARTIST
        )

        val c = context.contentResolver.query(
            uri, projection, null, null, null
        )
        if (c != null) {
            while (c.moveToNext()) {
                val path = c.getStringOrNull(c.getColumnIndex(MediaStore.Audio.AudioColumns.DATA))
                val name = c.getStringOrNull(c.getColumnIndex(MediaStore.Audio.AudioColumns.TITLE))
                val album = c.getStringOrNull(c.getColumnIndex(MediaStore.Audio.AudioColumns.ALBUM))
                val artist = c.getStringOrNull(c.getColumnIndex(MediaStore.Audio.AudioColumns.ARTIST))
                Log.d("TA", " Album data :$album = $path = $name = $artist")
            }
            c.close()
        }

    }
}