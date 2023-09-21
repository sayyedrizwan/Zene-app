package com.rizwansayyed.zene.data.offlinesongs

import android.content.ContentResolver
import android.content.ContentUris
import android.content.Context
import android.database.Cursor
import android.database.MergeCursor
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import com.rizwansayyed.zene.domain.OfflineSongsDetailsResult
import com.rizwansayyed.zene.presenter.util.UiUtils.toast
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.flow
import java.io.File
import javax.inject.Inject

class OfflineSongReadImpl @Inject constructor(
    @ApplicationContext private val context: Context
) : OfflineSongsReadInterface {

    override suspend fun readAllSongs() = flow {
        val songs = mutableListOf<OfflineSongsDetailsResult>()

        val internalStorageCursor = context.contentResolver.query(
            internalStorageUri, songProjection, null, null, sortOrder
        )
        internalStorageCursor?.use { cursor ->
            songsFromCursor(cursor, songs)
        }

        internalStorageCursor?.close()

        val externalStorageCursor = context.contentResolver.query(
            externalStorageUri, songProjection, null, null, sortOrder
        )
        externalStorageCursor?.use { cursor ->
            songsFromCursor(cursor, songs)
        }

        externalStorageCursor?.close()

        songs.sortByDescending { it.date }

        emit(songs)
    }

    override suspend fun songsFromCursor(
        cursor: Cursor, songs: MutableList<OfflineSongsDetailsResult>
    ) {
        val idColumn = cursor.getColumnIndexOrThrow(MediaStore.Audio.Media._ID)
        val titleColumn = cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.TITLE)
        val artistColumn = cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.ARTIST)
        val albumColumn = cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.ALBUM)
        val durationColumn = cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.DURATION)
        val dataColumn = cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.DATA)
        val albumIdColumn = cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.ALBUM_ID)
        val dateColumn = cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.DATE_ADDED)

        while (cursor.moveToNext()) {
            val id = cursor.getLong(idColumn)
            val title = cursor.getString(titleColumn)
            val artist = cursor.getString(artistColumn)
            val album = cursor.getString(albumColumn)
            val duration = cursor.getLong(durationColumn)
            val data = cursor.getString(dataColumn)
            val albumId = cursor.getLong(albumIdColumn)
            val date = cursor.getLong(dateColumn)
            val art: Uri = ContentUris.withAppendedId(
                Uri.parse("content://media/external/audio/albumart"), albumId
            )

            val song =
                OfflineSongsDetailsResult(id, title, artist, album, duration, data, date, art)
            songs.add(song)
        }
    }

}