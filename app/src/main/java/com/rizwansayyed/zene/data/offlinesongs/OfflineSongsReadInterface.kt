package com.rizwansayyed.zene.data.offlinesongs

import android.database.Cursor
import android.net.Uri
import android.provider.MediaStore
import com.rizwansayyed.zene.domain.OfflineSongsDetailsResult

interface OfflineSongsReadInterface {
    val internalStorageUri: Uri get() = MediaStore.Audio.Media.INTERNAL_CONTENT_URI
    val externalStorageUri: Uri get() = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI
    val sortOrder: String get() = "${MediaStore.Audio.Media.DATE_ADDED} DESC"

    val songProjection: Array<String>
        get() = arrayOf(
            MediaStore.Audio.Media._ID,
            MediaStore.Audio.Media.TITLE,
            MediaStore.Audio.Media.ARTIST,
            MediaStore.Audio.Media.ALBUM,
            MediaStore.Audio.Media.DURATION,
            MediaStore.Audio.Media.DATA
        )

    suspend fun readAllSongs(): MutableList<OfflineSongsDetailsResult>

    suspend fun songsFromCursor(cursor: Cursor, songs: MutableList<OfflineSongsDetailsResult>)
}