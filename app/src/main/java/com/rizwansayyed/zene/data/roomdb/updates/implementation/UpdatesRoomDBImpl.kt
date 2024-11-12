package com.rizwansayyed.zene.data.roomdb.updates.implementation

import com.rizwansayyed.zene.data.roomdb.updates.UpdatesDatabase
import com.rizwansayyed.zene.data.roomdb.updates.model.UpdateData
import com.rizwansayyed.zene.ui.earphonetracker.utils.LocationManagerResponse
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject


class UpdatesRoomDBImpl @Inject constructor(
    private val updateDB: UpdatesDatabase, private val location: LocationManagerResponse
) : UpdatesRoomDBInterface {

    override suspend fun insertDB(address: String, type: Int) = flow {
        val l = location.locationUpdates()
        val records =
            UpdateData(null, address, l.lat, l.lon, l.address, System.currentTimeMillis(), type)
        emit(updateDB.updatesDao().insert(records))
    }.flowOn(Dispatchers.IO)


    override suspend fun getLists(address: String, page: Int) = flow {
        val limit = page * 30
        emit(updateDB.updatesDao().get(address, limit))
    }.flowOn(Dispatchers.IO)


    override suspend fun removeAll(address: String) = flow {
        emit(updateDB.updatesDao().deleteAll(address))
    }.flowOn(Dispatchers.IO)

    override suspend fun remove(u: UpdateData) = flow {
        emit(updateDB.updatesDao().delete(u))
    }.flowOn(Dispatchers.IO)
}