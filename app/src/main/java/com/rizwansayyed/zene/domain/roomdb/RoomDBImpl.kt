package com.rizwansayyed.zene.domain.roomdb

import android.util.Log
import com.rizwansayyed.zene.BaseApplication.Companion.dataStoreManager
import com.rizwansayyed.zene.domain.ApiInterface
import com.rizwansayyed.zene.domain.IPApiInterface
import com.rizwansayyed.zene.presenter.model.TopArtistsSongs
import com.rizwansayyed.zene.domain.roomdb.recentplayed.RecentPlayedDao
import com.rizwansayyed.zene.domain.roomdb.recentplayed.RecentPlayedEntity
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
        val topSongs = recentPlayedDao.topListenSongs()
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
        val topSongs = recentPlayedDao.topListenSongs()
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
        emit(topArtists)
    }

    override suspend fun artistsSuggestionsForYouUsingHistory() = flow {
        var topSongs = recentPlayedDao.artistsUnique(7)
        if (topSongs.size < 7) {
            topSongs = recentPlayedDao.artistsUnique(13)
        }
        val list = ArrayList<TopArtistsSongs>(100)
        if (topSongs.isEmpty()) {
            emit(list)
            return@flow
        }

        topSongs.forEach {
            val artists =
                it.artists.trim().substringBefore("&").substringBefore(",").replace(" ", "+")
            apiInterface.similarArtists(artists).forEach { songs ->
                if (!list.any { l -> l.name == songs.name }) {
                    list.add(songs)
                }
            }
        }
        list.shuffle()
        emit(list)
    }
}