package com.rizwansayyed.zene.data.roomdb.implementation

import com.rizwansayyed.zene.data.roomdb.model.UpdateData
import kotlinx.coroutines.flow.Flow

interface UpdatesRoomDBInterface {
    suspend fun insertDB(i: UpdateData): Flow<Unit>
}