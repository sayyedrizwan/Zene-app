package com.rizwansayyed.zene.data.db.impl

import com.rizwansayyed.zene.data.db.offlinedownload.OfflineDownloadedDao
import com.rizwansayyed.zene.data.db.offlinedownload.OfflineDownloadedEntity
import com.rizwansayyed.zene.data.db.recentplay.RecentPlayedDao
import com.rizwansayyed.zene.data.db.recentplay.RecentPlayedEntity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject

class RoomDBImpl @Inject constructor(
    private val recentPlayed: RecentPlayedDao,
    private val offlineDownloaded: OfflineDownloadedDao
) : RoomDBInterface {

    override suspend fun recentSixPlayed() = flow {
        emit(recentPlayed.recentList())
    }.flowOn(Dispatchers.IO)


    override suspend fun insert(v: RecentPlayedEntity) = flow {
        emit(recentPlayed.insert(v))
    }.flowOn(Dispatchers.IO)


    override suspend fun offlineDownloadedSongs() = flow {
        emit(offlineDownloaded.recentList())
    }.flowOn(Dispatchers.IO)


    override suspend fun insert(v: OfflineDownloadedEntity) = flow {
        emit(offlineDownloaded.insert(v))
    }.flowOn(Dispatchers.IO)

}