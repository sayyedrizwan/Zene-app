package com.rizwansayyed.zene.roomdb

import com.rizwansayyed.zene.roomdb.recentplayed.RecentPlayedDao
import com.rizwansayyed.zene.roomdb.recentplayed.RecentPlayedEntity
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class RoomDBImpl @Inject constructor(private val recentPlayedDao: RecentPlayedDao) :
    RoomDBImplInterface {

    override suspend fun recentPlayed() = flow {
        emit(recentPlayedDao.recentPlayedHome())
    }


    override suspend fun insert(recentPlay: RecentPlayedEntity) = flow {
        emit(recentPlayedDao.insert(recentPlay))
    }

    override suspend fun artists() = flow {
        emit(recentPlayedDao.artists())
    }
}