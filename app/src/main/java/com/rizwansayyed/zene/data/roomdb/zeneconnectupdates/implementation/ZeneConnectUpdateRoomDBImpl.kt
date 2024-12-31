package com.rizwansayyed.zene.data.roomdb.zeneconnectupdates.implementation

import com.rizwansayyed.zene.data.api.zene.ZeneAPIService
import com.rizwansayyed.zene.data.roomdb.zeneconnectupdates.ZeneConnectUpdateDatabase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject

class ZeneConnectUpdateRoomDBImpl @Inject constructor(
    private val zeneAPI: ZeneAPIService,
    private val zeneUpdateDB: ZeneConnectUpdateDatabase
) : ZeneConnectUpdateRoomDBInterface {

    override suspend fun getAll() = flow {
        emit(zeneUpdateDB.dao().getList())
    }.flowOn(Dispatchers.IO)

}