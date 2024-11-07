package com.rizwansayyed.zene.data.roomdb.implementation


import com.rizwansayyed.zene.data.roomdb.UpdatesDatabase
import com.rizwansayyed.zene.data.roomdb.model.UpdateData
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject


class UpdatesRoomDBImpl @Inject constructor(private val updateDB: UpdatesDatabase) :
    UpdatesRoomDBInterface {

    override suspend fun insertDB(i: UpdateData) = flow {
        val info = updateDB.updatesDao().insertAll()
        emit(info)
    }.flowOn(Dispatchers.IO)
}