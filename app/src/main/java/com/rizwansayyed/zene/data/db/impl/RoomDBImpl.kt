package com.rizwansayyed.zene.data.db.impl

import com.rizwansayyed.zene.data.db.recentplay.RecentPlayedDao
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject

class RoomDBImpl @Inject constructor(private val recentPlayed: RecentPlayedDao) : RoomDBInterface {

    override suspend fun recentSixPlayed() = flow {
        emit(recentPlayed.recentList())
    }.flowOn(Dispatchers.IO)

}