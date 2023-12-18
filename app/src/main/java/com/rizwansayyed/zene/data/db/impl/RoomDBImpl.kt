package com.rizwansayyed.zene.data.db.impl

import com.rizwansayyed.zene.data.db.artistsfeed.ArtistsFeedDao
import com.rizwansayyed.zene.data.db.artistsfeed.ArtistsFeedEntity
import com.rizwansayyed.zene.data.db.artistspin.PinnedArtistsDao
import com.rizwansayyed.zene.data.db.artistspin.PinnedArtistsEntity
import com.rizwansayyed.zene.data.db.offlinedownload.OfflineDownloadedDao
import com.rizwansayyed.zene.data.db.offlinedownload.OfflineDownloadedEntity
import com.rizwansayyed.zene.data.db.recentplay.RecentPlayedDao
import com.rizwansayyed.zene.data.db.recentplay.RecentPlayedEntity
import com.rizwansayyed.zene.data.db.savedplaylist.playlist.SavedPlaylistDao
import com.rizwansayyed.zene.data.db.savedplaylist.playlist.SavedPlaylistEntity
import com.rizwansayyed.zene.data.db.savedplaylist.playlistsongs.PlaylistSongsDao
import com.rizwansayyed.zene.data.db.savedplaylist.playlistsongs.PlaylistSongsEntity
import com.rizwansayyed.zene.utils.Utils.OFFSET_LIMIT
import com.rizwansayyed.zene.utils.Utils.daysOldTimestamp
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject

class RoomDBImpl @Inject constructor(
    private val recentPlayed: RecentPlayedDao,
    private val offlineDownloaded: OfflineDownloadedDao,
    private val savedPlaylistDao: SavedPlaylistDao,
    private val playlistSongsDao: PlaylistSongsDao,
    private val pinnedArtists: PinnedArtistsDao,
    private val artistsFeedDao: ArtistsFeedDao
) : RoomDBInterface {

    override suspend fun recentMainPlayed() = flow {
        emit(recentPlayed.recentListLive(OFFSET_LIMIT))
    }.flowOn(Dispatchers.IO)

    override suspend fun recentPlayedList(offset: Int) = flow {
        emit(recentPlayed.recentList(offset))
    }.flowOn(Dispatchers.IO)

    override suspend fun readRecentPlay(set: Int) = flow {
        emit(recentPlayed.read(set))
    }.flowOn(Dispatchers.IO)


    override suspend fun insert(v: RecentPlayedEntity) = flow {
        emit(recentPlayed.insert(v))
    }.flowOn(Dispatchers.IO)


    override suspend fun insert(v: OfflineDownloadedEntity) = flow {
        emit(offlineDownloaded.insertOrUpdate(v))
    }.flowOn(Dispatchers.IO)

    override suspend fun savedPlaylists() = flow {
        emit(savedPlaylistDao.list())
    }.flowOn(Dispatchers.IO)

    override suspend fun albumsList() = flow {
        emit(savedPlaylistDao.albumsList())
    }.flowOn(Dispatchers.IO)

    override suspend fun isAlbums(id: String) = flow {
        emit(savedPlaylistDao.isAlbums(id))
    }.flowOn(Dispatchers.IO)

    override suspend fun deleteAlbums(id: String) = flow {
        emit(savedPlaylistDao.deleteAlbums(id))
    }.flowOn(Dispatchers.IO)

    override suspend fun pagingAlbums(offset: Int) = flow {
        emit(savedPlaylistDao.pagingAlbums(offset))
    }.flowOn(Dispatchers.IO)

    override suspend fun offlineSongInfo(songId: String) = flow {
        emit(offlineDownloaded.songDetails(songId))
    }.flowOn(Dispatchers.IO)

    override suspend fun offlineSongInfoFlow(songId: String) = flow {
        emit(offlineDownloaded.songDetailsFlow(songId))
    }.flowOn(Dispatchers.IO)

    override suspend fun insert(v: SavedPlaylistEntity) = flow {
        emit(savedPlaylistDao.insert(v))
    }.flowOn(Dispatchers.IO)


    override suspend fun addOfflineSongDownload(v: OfflineDownloadedEntity) = flow {
        emit(offlineDownloaded.insertOrUpdate(v))
    }.flowOn(Dispatchers.IO)


    override suspend fun removeSong(songId: String) = flow {
        emit(offlineDownloaded.removeSong(songId))
    }.flowOn(Dispatchers.IO)


    override suspend fun playlistWithName(name: String) = flow {
        emit(savedPlaylistDao.searchWithName(name))
    }.flowOn(Dispatchers.IO)


    override suspend fun allCreatedPlaylists(limit: Int) = flow {
        emit(savedPlaylistDao.pagingCreatedPlaylist(limit))
    }.flowOn(Dispatchers.IO)

    override suspend fun playlistSongInfo(songId: String) = flow {
        emit(playlistSongsDao.info(songId))
    }.flowOn(Dispatchers.IO)

    override suspend fun defaultPlaylistSongsCount() = flow {
        emit(playlistSongsDao.defaultPlaylistSongsCount())
    }.flowOn(Dispatchers.IO)


    override suspend fun songInfo(songId: String) = flow {
        emit(playlistSongsDao.songInfo(songId))
    }.flowOn(Dispatchers.IO)


    override suspend fun rmSongs(songId: String) = flow {
        emit(playlistSongsDao.rmSongs(songId))
    }.flowOn(Dispatchers.IO)


    override suspend fun insert(v: PlaylistSongsEntity) = flow {
        emit(playlistSongsDao.insert(v))
    }.flowOn(Dispatchers.IO)


    override suspend fun nonDownloadedSongs() = flow {
        emit(offlineDownloaded.nonDownloadedSongs())
    }.flowOn(Dispatchers.IO)


    override suspend fun offlineDownloadedSongs() = flow {
        emit(offlineDownloaded.recentList())
    }.flowOn(Dispatchers.IO)

    override suspend fun offlineDownloadedSongs(offset: Int) = flow {
        emit(offlineDownloaded.recentList(offset))
    }.flowOn(Dispatchers.IO)


    override suspend fun topSongsListenThisWeekOrMonth() = flow {
        var isThisWeek = true
        var songs = recentPlayed.readWithTimestamp(10, daysOldTimestamp(8))
        if (songs.size <= 2) {
            isThisWeek = false
            songs = recentPlayed.readWithTimestamp(10, daysOldTimestamp(31))
        }
        emit(Pair(isThisWeek, songs))
    }.flowOn(Dispatchers.IO)


    override suspend fun pinnedArtistsList() = flow {
        emit(pinnedArtists.flowList())
    }.flowOn(Dispatchers.IO)

    override suspend fun pinnedArtists() = flow {
        emit(pinnedArtists.list())
    }.flowOn(Dispatchers.IO)

    override suspend fun doContainPinnedArtist(name: String) = flow {
        emit(pinnedArtists.doContain(name))
    }.flowOn(Dispatchers.IO)

    override suspend fun artistsData(name: String) = flow {
        emit(pinnedArtists.artistsData(name))
    }.flowOn(Dispatchers.IO)

    override suspend fun artistsThumbnailUpdate(url: String) = flow {
        emit(pinnedArtists.artistsThumbnailUpdate(url))
    }.flowOn(Dispatchers.IO)

    override suspend fun insert(v: PinnedArtistsEntity) = flow {
        emit(pinnedArtists.insertOrUpdate(v))
    }.flowOn(Dispatchers.IO)

    override suspend fun deletePinnedArtists(v: String) = flow {
        emit(pinnedArtists.delete(v))
    }.flowOn(Dispatchers.IO)

    override suspend fun deleteArtistsFeeds(v: String) = flow {
        emit(artistsFeedDao.delete(v))
    }.flowOn(Dispatchers.IO)

    override suspend fun insert(v: ArtistsFeedEntity) = flow {
        emit(artistsFeedDao.insertOrUpdate(v))
    }.flowOn(Dispatchers.IO)

}