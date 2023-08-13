package com.rizwansayyed.zene.domain.roomdb

import android.util.Log
import com.rizwansayyed.zene.BaseApplication.Companion.dataStoreManager
import com.rizwansayyed.zene.domain.ApiInterface
import com.rizwansayyed.zene.domain.IPApiInterface
import com.rizwansayyed.zene.presenter.model.TopArtistsSongs
import com.rizwansayyed.zene.domain.roomdb.recentplayed.RecentPlayedDao
import com.rizwansayyed.zene.domain.roomdb.recentplayed.RecentPlayedEntity
import com.rizwansayyed.zene.presenter.model.TopArtistsSongsWithData
import com.rizwansayyed.zene.utils.Utils.showToast
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOf
import javax.inject.Inject

class RoomDBImpl @Inject constructor(
    private val recentPlayedDao: RecentPlayedDao,
    private val apiInterface: ApiInterface,
    private val ipInterface: IPApiInterface
) : RoomDBImplInterface {

    override suspend fun recentPlayed() = flow {
        emit(recentPlayedDao.recentPlayedHome())
    }


    override suspend fun insert(recentPlay: RecentPlayedEntity) = flow {
        emit(recentPlayedDao.insert(recentPlay))
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
        val list = ArrayList<TopArtistsSongs>(100)
        if (topSongs.isEmpty()) {
            emit(list)
            return@flow
        }

        topSongs.forEach {
            val artists =
                it.artists.trim().substringBefore("&").substringBefore(",").replace(" ", "+").trim()
            apiInterface.similarArtists(artists).forEach { songs ->
                if (!list.any { l -> l.name == songs.name }) {
                    list.add(songs)
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

}