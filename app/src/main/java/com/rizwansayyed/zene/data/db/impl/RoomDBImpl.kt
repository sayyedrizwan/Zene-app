package com.rizwansayyed.zene.data.db.impl

import com.rizwansayyed.zene.data.db.recentplay.RecentPlayedDao
import com.rizwansayyed.zene.data.db.recentplay.RecentPlayedEntity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject

class RoomDBImpl @Inject constructor(private val recentPlayed: RecentPlayedDao) : RoomDBInterface {

    override suspend fun recentSixPlayed() = flow {
        emit(recentPlayed.recentList())
    }.flowOn(Dispatchers.IO)


    override suspend fun insert(v: RecentPlayedEntity) = flow {
        emit(recentPlayed.insert(v))
    }.flowOn(Dispatchers.IO)

}