package com.rizwansayyed.zene.data.offlinesongs

import android.content.ContentResolver
import android.database.Cursor
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import androidx.annotation.RequiresApi
import com.rizwansayyed.zene.domain.OfflineSongsDetailsResult
import kotlinx.coroutines.flow.Flow

interface OfflineSongsReadInterface {
    val internalStorageUri: Uri get() = MediaStore.Audio.Media.INTERNAL_CONTENT_URI
    val externalStorageUri: Uri get() = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI
    fun sortOrder(l: Int, o: Int) = "${MediaStore.Audio.Media.DATE_ADDED} DESC LIMIT $l OFFSET $o"

    @RequiresApi(Build.VERSION_CODES.O)
    fun sortOrderSDKO(l: Int, o: Int) = Bundle().apply {
        putInt(ContentResolver.QUERY_ARG_LIMIT, l)
        putInt(ContentResolver.QUERY_ARG_OFFSET, o)
        putStringArray(
            ContentResolver.QUERY_ARG_SORT_COLUMNS, arrayOf(MediaStore.Files.FileColumns.DATE_ADDED)
        )
        putInt(
            ContentResolver.QUERY_ARG_SORT_DIRECTION, ContentResolver.QUERY_SORT_DIRECTION_DESCENDING
        )
    }

    val songProjection: Array<String>
        get() = arrayOf(
            MediaStore.Audio.Media._ID,
            MediaStore.Audio.Media.TITLE,
            MediaStore.Audio.Media.ARTIST,
            MediaStore.Audio.Media.ALBUM,
            MediaStore.Audio.Media.DURATION,
            MediaStore.Audio.Media.DATA,
            MediaStore.Audio.Media.ALBUM_ID
        )

    suspend fun songsFromCursor(cursor: Cursor, songs: MutableList<OfflineSongsDetailsResult>)
    suspend fun readAllSongs(offset: Int, limit: Int): Flow<MutableList<OfflineSongsDetailsResult>>
}