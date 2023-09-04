package com.rizwansayyed.zene.domain.roomdb

import com.rizwansayyed.zene.domain.roomdb.collections.items.PlaylistSongsEntity
import com.rizwansayyed.zene.domain.roomdb.collections.playlist.PlaylistEntity
import com.rizwansayyed.zene.domain.roomdb.offlinesongs.OfflineSongsEntity
import com.rizwansayyed.zene.domain.roomdb.offlinesongs.OfflineStatusTypes
import com.rizwansayyed.zene.presenter.model.TopArtistsSongs
import com.rizwansayyed.zene.domain.roomdb.recentplayed.RecentPlayedEntity
import com.rizwansayyed.zene.domain.roomdb.songsdetails.SongDetailsEntity
import com.rizwansayyed.zene.presenter.model.TopArtistsSongsWithData
import kotlinx.coroutines.flow.Flow

interface RoomDBImplInterface {

    suspend fun recentPlayed(): Flow<Flow<List<RecentPlayedEntity>>>

    suspend fun insert(recentPlay: RecentPlayedEntity): Flow<Unit>
    suspend fun songsSuggestionsUsingSongsHistory(): Flow<ArrayList<TopArtistsSongs>>

    suspend fun songSuggestionsForYouUsingHistory(): Flow<ArrayList<TopArtistsSongs>>

    suspend fun artistsSuggestionsForYouUsingHistory(): Flow<ArrayList<TopArtistsSongs>>

    suspend fun topArtistsSuggestions(): Flow<List<RecentPlayedEntity>>

    suspend fun topArtistsSongs(): Flow<ArrayList<TopArtistsSongsWithData>>
    suspend fun allSongsForYouSongs(): Flow<ArrayList<TopArtistsSongs>>
    suspend fun insert(songDetails: SongDetailsEntity): Flow<Unit>
    suspend fun recentPlayedHome(name: String): Flow<List<SongDetailsEntity>>
    suspend fun removeSongDetails(songID: String): Flow<Unit>
    suspend fun insert(song: OfflineSongsEntity): Flow<Unit>
    suspend fun offlineSongs(): Flow<List<OfflineSongsEntity>>

    suspend fun allOfflineSongs(): Flow<Flow<List<OfflineSongsEntity>>>
    suspend fun musicOfflineSongs(pid: String): Flow<Flow<List<OfflineSongsEntity>>>
    suspend fun updateStatus(status: OfflineStatusTypes, pid: String): Flow<Int>
    suspend fun countOfflineSongs(pid: String): Flow<Int>
    suspend fun getRecentData(pid: String): Flow<RecentPlayedEntity?>
    suspend fun insertWhole(recentPlay: RecentPlayedEntity): Flow<Unit>
    suspend fun deleteOfflineSong(pId: String): Flow<Int>
    suspend fun allPlaylist(): Flow<Flow<List<PlaylistEntity>>>
    suspend fun playlists(name: String): Flow<List<PlaylistEntity>>
    suspend fun playlists(p: PlaylistEntity): Flow<Unit>
    suspend fun playlistItem(p: PlaylistSongsEntity): Flow<Unit>
    suspend fun latest4playlistsItem(id: Int): Flow<List<PlaylistSongsEntity>>
    suspend fun playlistSongs(pID: Int): Flow<Flow<List<PlaylistSongsEntity>>>
    suspend fun playlistsWithId(id: Int): Flow<List<PlaylistEntity>>
    suspend fun isSongsAlreadyAvailable(pid: String): Flow<Int>
}