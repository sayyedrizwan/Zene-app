package com.rizwansayyed.zene.data.roomdb.implementation


import android.util.Log
import com.rizwansayyed.zene.data.roomdb.UpdatesDatabase
import com.rizwansayyed.zene.data.roomdb.model.UpdateData
import com.rizwansayyed.zene.ui.earphonetracker.utils.LocationManagerResponse
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.firstOrNull
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
}