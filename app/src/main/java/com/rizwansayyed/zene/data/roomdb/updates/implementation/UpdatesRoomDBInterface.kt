package com.rizwansayyed.zene.data.roomdb.updates.implementation

import com.rizwansayyed.zene.data.roomdb.updates.model.UpdateData
import kotlinx.coroutines.flow.Flow

interface UpdatesRoomDBInterface {
    suspend fun insertDB(address: String, type: Int): Flow<Unit>
    suspend fun getLists(address: String, page: Int): Flow<List<UpdateData>>
    suspend fun remove(u: UpdateData): Flow<Unit>
    suspend fun removeAll(address: String): Flow<Unit>
}