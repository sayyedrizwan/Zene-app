package com.rizwansayyed.zene.data.roomdb.offlinesongs.implementation

import com.rizwansayyed.zene.data.api.model.ZeneMusicDataItems
import com.rizwansayyed.zene.data.roomdb.offlinesongs.model.OfflineSongsData
import com.rizwansayyed.zene.data.roomdb.updates.model.UpdateData
import kotlinx.coroutines.flow.Flow

interface OfflineSongsDBInterface {
    suspend fun save(music: ZeneMusicDataItems): Flow<Unit>
    suspend fun getLists(page: Int): Flow<List<OfflineSongsData>>
    suspend fun isSaved(id: String): Flow<Int>
    suspend fun delete(id: String): Flow<Unit>
}