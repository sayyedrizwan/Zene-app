package com.rizwansayyed.zene.data.db.impl

import androidx.paging.DataSource
import androidx.paging.PagingConfig
import com.rizwansayyed.zene.data.db.artistsfeed.ArtistsFeedEntity
import com.rizwansayyed.zene.data.db.artistspin.PinnedArtistsEntity
import com.rizwansayyed.zene.data.db.offlinedownload.OfflineDownloadedEntity
import com.rizwansayyed.zene.data.db.recentplay.RecentPlayedEntity
import com.rizwansayyed.zene.data.db.savedplaylist.playlist.SavedPlaylistEntity
import com.rizwansayyed.zene.data.db.savedplaylist.playlistsongs.PlaylistSongsEntity
import kotlinx.coroutines.flow.Flow

interface RoomDBInterface {

    suspend fun recentMainPlayed(): Flow<Flow<List<RecentPlayedEntity>>>

    suspend fun insert(v: RecentPlayedEntity): Flow<Unit>

    suspend fun offlineDownloadedSongs(): Flow<Flow<List<OfflineDownloadedEntity>>>

    suspend fun insert(v: OfflineDownloadedEntity): Flow<Unit>

    suspend fun savedPlaylists(): Flow<Flow<List<SavedPlaylistEntity>>>

    suspend fun insert(v: SavedPlaylistEntity): Flow<Unit>

    suspend fun readRecentPlay(set: Int): Flow<List<RecentPlayedEntity>>

    suspend fun offlineSongInfo(songId: String): Flow<OfflineDownloadedEntity?>

    suspend fun addOfflineSongDownload(v: OfflineDownloadedEntity): Flow<Unit>

    suspend fun offlineSongInfoFlow(songId: String): Flow<Flow<OfflineDownloadedEntity?>>

    suspend fun removeSong(songId: String): Flow<Int>

    suspend fun playlistWithName(name: String): Flow<List<SavedPlaylistEntity>>

    suspend fun allCreatedPlaylists(limit: Int): Flow<List<SavedPlaylistEntity>>

    suspend fun playlistSongInfo(songId: String): Flow<Flow<PlaylistSongsEntity?>>

    suspend fun songInfo(songId: String): Flow<PlaylistSongsEntity?>

    suspend fun rmSongs(songId: String): Flow<Int>

    suspend fun insert(v: PlaylistSongsEntity): Flow<Unit>

    suspend fun nonDownloadedSongs(): Flow<List<OfflineDownloadedEntity>>
    suspend fun defaultPlaylistSongsCount(): Flow<Int>
    suspend fun recentPlayedList(offset: Int): Flow<List<RecentPlayedEntity>>

    suspend fun offlineDownloadedSongs(offset: Int): Flow<List<OfflineDownloadedEntity>>
    suspend fun topSongsListenThisWeekOrMonth(): Flow<Pair<Boolean, List<RecentPlayedEntity>>>
    suspend fun albumsList(): Flow<Flow<List<SavedPlaylistEntity>>>
    suspend fun pagingAlbums(offset: Int): Flow<List<SavedPlaylistEntity>>
    suspend fun isAlbums(id: String): Flow<Flow<Int>>

    suspend fun deleteAlbums(id: String): Flow<Unit>
    suspend fun pinnedArtistsList(): Flow<Flow<List<PinnedArtistsEntity>>>
    suspend fun doContainPinnedArtist(name: String): Flow<Int>
    suspend fun insert(v: PinnedArtistsEntity): Flow<Unit>
    suspend fun deletePinnedArtists(v: String): Flow<Int>
    suspend fun pinnedArtists(): Flow<List<PinnedArtistsEntity>>
    suspend fun artistsData(name: String): Flow<PinnedArtistsEntity>
    suspend fun deleteArtistsFeeds(v: String): Flow<Int>
    suspend fun insert(v: ArtistsFeedEntity): Flow<Unit>
    suspend fun artistsThumbnailUpdate(url: String): Flow<Int>
    suspend fun deleteAll(): Flow<Int>
}