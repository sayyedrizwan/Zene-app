package com.rizwansayyed.zene.domain.roomdb.songsdetails

import android.provider.MediaStore.Audio.Artists
import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert
import com.rizwansayyed.zene.domain.roomdb.recentplayed.RecentPlayedEntity
import com.rizwansayyed.zene.utils.Utils.DB.RECENT_PLAYED_DB
import com.rizwansayyed.zene.utils.Utils.DB.SONG_DETAILS_DB
import kotlinx.coroutines.flow.Flow

@Dao
interface SongDetailsDao {

    @Query("SELECT * FROM $SONG_DETAILS_DB WHERE name = :name AND artists = :artists LIMIT 6")
    suspend fun recentPlayedHome(name: String, artists: String): List<SongDetailsEntity>

    @Query("DELETE FROM $SONG_DETAILS_DB WHERE songID = :id")
    suspend fun removeSongDetails(id: String)


    @Upsert
    suspend fun insert(data: SongDetailsEntity)
}