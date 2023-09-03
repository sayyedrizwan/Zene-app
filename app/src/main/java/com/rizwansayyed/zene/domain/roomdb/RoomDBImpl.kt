package com.rizwansayyed.zene.domain.roomdb

import com.rizwansayyed.zene.BaseApplication.Companion.dataStoreManager
import com.rizwansayyed.zene.domain.ApiInterface
import com.rizwansayyed.zene.domain.IPApiInterface
import com.rizwansayyed.zene.domain.roomdb.collections.playlist.PlaylistDao
import com.rizwansayyed.zene.domain.roomdb.offlinesongs.OfflineSongsDao
import com.rizwansayyed.zene.domain.roomdb.offlinesongs.OfflineSongsEntity
import com.rizwansayyed.zene.domain.roomdb.offlinesongs.OfflineStatusTypes
import com.rizwansayyed.zene.presenter.model.TopArtistsSongs
import com.rizwansayyed.zene.domain.roomdb.recentplayed.RecentPlayedDao
import com.rizwansayyed.zene.domain.roomdb.recentplayed.RecentPlayedEntity
import com.rizwansayyed.zene.domain.roomdb.songsdetails.SongDetailsDao
import com.rizwansayyed.zene.domain.roomdb.songsdetails.SongDetailsEntity
import com.rizwansayyed.zene.presenter.model.TopArtistsSongsWithData
import com.rizwansayyed.zene.presenter.jsoup.ArtistsDataJsoup
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOf
import javax.inject.Inject

class RoomDBImpl @Inject constructor(
    private val recentPlayedDao: RecentPlayedDao,
    private val playlist: PlaylistDao,
    private val apiInterface: ApiInterface,
    private val ipInterface: IPApiInterface,
    private val songDetailsDao: SongDetailsDao,
    private val offlineSongsDao: OfflineSongsDao,
    private val artistsDataJsoup: ArtistsDataJsoup
) : RoomDBImplInterface {

    override suspend fun recentPlayed() = flow {
        emit(recentPlayedDao.recentPlayedHome())
    }

    override suspend fun insert(recentPlay: RecentPlayedEntity) = flow {
        val response = recentPlayedDao.getRecentData(recentPlay.pid)
        if (response != null) {
            response.playTimes += 1
            response.timestamp = System.currentTimeMillis()
            emit(recentPlayedDao.insert(response))
        } else
            emit(recentPlayedDao.insert(recentPlay))
    }

    override suspend fun insertWhole(recentPlay: RecentPlayedEntity) = flow {
        emit(recentPlayedDao.insert(recentPlay))
    }

    override suspend fun getRecentData(pid: String) = flow {
        emit(recentPlayedDao.getRecentData(pid))
    }

    override suspend fun recentPlayedHome(name: String) = flow {
        emit(songDetailsDao.recentPlayedHome(name))
    }

    override suspend fun removeSongDetails(songID: String) = flow {
        emit(songDetailsDao.removeSongDetails(songID))
    }

    override suspend fun insert(songDetails: SongDetailsEntity) = flow {
        emit(songDetailsDao.insert(songDetails))
    }

    override suspend fun songsSuggestionsUsingSongsHistory() = flow {
        var topSongs = recentPlayedDao.topListenSongs()
        if (topSongs.size < 5) {
            topSongs = recentPlayedDao.topListenSongsAll()
        }
        val list = ArrayList<TopArtistsSongs>(300)

        if (topSongs.isEmpty()) {
            emit(list)
            return@flow
        }
        val ip = ipInterface.ip()
        dataStoreManager.ipData = flowOf(ip)

        topSongs.forEach {
            apiInterface.songSuggestions(ip.query ?: "", it.pid).forEach { songs ->
                if (!list.any { l -> l.name == songs.name }) {
                    list.add(songs)
                }
            }
        }
        list.shuffle()
        emit(list)
    }

    override suspend fun songSuggestionsForYouUsingHistory() = flow {
        var topSongs = recentPlayedDao.topListenSongs()
        if (topSongs.size < 5) {
            topSongs = recentPlayedDao.topListenSongsAll()
        }

        val list = ArrayList<TopArtistsSongs>(300)

        if (topSongs.isEmpty()) {
            emit(list)
            return@flow
        }
        val ip = ipInterface.ip()
        dataStoreManager.ipData = flowOf(ip)

        topSongs.forEach {
            apiInterface.songSuggestionsForYou(ip.query ?: "", it.pid).forEach { songs ->
                if (!list.any { l -> l.name == songs.name }) {
                    list.add(songs)
                }
            }
        }
        list.shuffle()
        emit(list)
    }


    override suspend fun topArtistsSuggestions() = flow {
        var topArtists = recentPlayedDao.artistsUnique(7)
        if (topArtists.size < 7) {
            topArtists = recentPlayedDao.artistsUnique(13)
        }
        if (topArtists.size < 7) {
            topArtists = recentPlayedDao.artistsUniqueAll(13)
        }
        emit(topArtists)
    }

    override suspend fun artistsSuggestionsForYouUsingHistory() = flow {
        var topSongs = recentPlayedDao.artistsUnique(7)
        if (topSongs.size < 7) {
            topSongs = recentPlayedDao.artistsUnique(13)
        }
        if (topSongs.size < 7) {
            topSongs = recentPlayedDao.artistsUniqueAll(13)
        }
        val list = ArrayList<TopArtistsSongs>(150)
        if (topSongs.isEmpty()) {
            emit(list)
            return@flow
        }

        topSongs.forEach {
            val artists =
                it.artists.trim().substringBefore("&").substringBefore(",").replace(" ", "+").trim()

            val artistURL = try {
                artistsDataJsoup.searchArtists(artists).first()
            } catch (e: Exception) {
                null
            }

            if (artistURL != null) {
                val data = try {
                    artistsDataJsoup.artistsSimilar(artistURL).first()
                } catch (e: Exception) {
                    null
                }

                data?.forEach { a ->
                    if (!list.any { l -> l.name == a.name }) {
                        list.add(a)
                    }
                }
            }
        }
        list.shuffle()
        emit(list)
    }


    override suspend fun topArtistsSongs() = flow {
        var topArtists = recentPlayedDao.artistsUnique(7)
        if (topArtists.size < 7) {
            topArtists = recentPlayedDao.artistsUnique(13)
        }

        if (topArtists.size < 7) {
            topArtists = recentPlayedDao.artistsUniqueAll(13)
        }

        val ip = ipInterface.ip()
        dataStoreManager.ipData = flowOf(ip)

        val list = ArrayList<TopArtistsSongsWithData>(20)
        if (topArtists.isEmpty()) {
            emit(list)
            return@flow
        }

        topArtists.forEach {
            val listsChild = ArrayList<TopArtistsSongs>(200)
            val artists =
                it.artists.trim().substringBefore("&").substringBefore(",").lowercase().trim()

            apiInterface.searchSongs(ip.query ?: "", artists).apply { shuffled() }
                .forEach { songs ->
                    listsChild.add(TopArtistsSongs(songs.name, songs.img, songs.artist))
                }

            val a = it.artists.trim().substringBefore("&").substringBefore(",")
            list.add(TopArtistsSongsWithData(a, listsChild))
        }
        emit(list)
    }


    override suspend fun allSongsForYouSongs() = flow {
        var topArtists = recentPlayedDao.artistsUnique(20)
        if (topArtists.size < 10) {
            topArtists = recentPlayedDao.artistsUniqueAll(20)
        }

        var topSongs = recentPlayedDao.top20ListenSongs()
        if (topSongs.size < 10) {
            topSongs = recentPlayedDao.top20ListenSongsAll()
        }
        val list = ArrayList<TopArtistsSongs>(3000)

        val ip = ipInterface.ip()
        dataStoreManager.ipData = flowOf(ip)

        topArtists.forEach {
            val artists =
                it.artists.trim().substringBefore("&").substringBefore(",").lowercase().trim()

            apiInterface.searchSongs(ip.country ?: "", artists).forEach { songs ->
                list.add(songs)
            }
        }

        topSongs.forEach {
            apiInterface.songSuggestions(ip.query ?: "", it.pid).forEach { songs ->
                if (!list.any { l -> l.name == songs.name }) {
                    list.add(songs)
                }
            }
        }
        emit(list)
    }


    override suspend fun insert(song: OfflineSongsEntity) = flow {
        emit(offlineSongsDao.insert(song))
    }


    override suspend fun offlineSongs() = flow {
        emit(offlineSongsDao.offlineSongs())
    }

    override suspend fun countOfflineSongs(pid: String) = flow {
        emit(offlineSongsDao.countOfflineSongs(pid))
    }


    override suspend fun musicOfflineSongs(pid: String) = flow {
        emit(offlineSongsDao.musicOfflineSongs(pid))
    }


    override suspend fun allOfflineSongs() = flow {
        emit(offlineSongsDao.allOfflineSongs())
    }

    override suspend fun updateStatus(status: OfflineStatusTypes, pid: String) = flow {
        emit(offlineSongsDao.updateStatus(status, pid))
    }

    override suspend fun deleteOfflineSong(pId: String) = flow {
        emit(offlineSongsDao.delete(pId))
    }


    override suspend fun allPlaylist() = flow {
        emit(playlist.playlists())
    }


}