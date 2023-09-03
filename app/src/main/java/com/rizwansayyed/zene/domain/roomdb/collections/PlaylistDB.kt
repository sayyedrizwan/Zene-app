package com.rizwansayyed.zene.domain.roomdb.collections

import androidx.room.Database
import androidx.room.RoomDatabase
import com.rizwansayyed.zene.domain.roomdb.collections.items.PlaylistSongsDao
import com.rizwansayyed.zene.domain.roomdb.collections.items.PlaylistSongsEntity
import com.rizwansayyed.zene.domain.roomdb.collections.playlist.PlaylistDao
import com.rizwansayyed.zene.domain.roomdb.collections.playlist.PlaylistEntity
import com.rizwansayyed.zene.domain.roomdb.offlinesongs.OfflineSongsDao

@Database(entities = [PlaylistEntity::class, PlaylistSongsEntity::class], version = 1)
abstract class PlaylistDB : RoomDatabase() {
    abstract fun playlist(): PlaylistDao

    abstract fun playlistSongs(): PlaylistSongsDao
}