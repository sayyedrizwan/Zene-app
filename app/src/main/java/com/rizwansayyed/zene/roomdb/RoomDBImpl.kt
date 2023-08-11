package com.rizwansayyed.zene.roomdb

import com.rizwansayyed.zene.BaseApplication
import com.rizwansayyed.zene.BaseApplication.Companion.dataStoreManager
import com.rizwansayyed.zene.domain.ApiInterface
import com.rizwansayyed.zene.domain.ApiInterfaceImpl
import com.rizwansayyed.zene.domain.IPApiInterface
import com.rizwansayyed.zene.presenter.model.TopArtistsSongs
import com.rizwansayyed.zene.roomdb.recentplayed.RecentPlayedDao
import com.rizwansayyed.zene.roomdb.recentplayed.RecentPlayedEntity
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOf
import java.util.Collections
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

    override suspend fun artists() = flow {
        emit(recentPlayedDao.artists())
    }


    override suspend fun songsSuggestionsUsingSongsHistory() = flow {
        val top4Songs = recentPlayedDao.topListenSongs()
        val ip = ipInterface.ip()
        dataStoreManager.ipData = flowOf(ip)
        val list = ArrayList<TopArtistsSongs>(100)

        top4Songs.forEach {
            apiInterface.songSuggestions(ip.query ?: "", it.pid).forEach { songs ->
                if (!list.any { l -> l.name == songs.name }) {
                    list.add(songs)
                }
            }
        }
        list.shuffle()
        emit(list)
    }
}