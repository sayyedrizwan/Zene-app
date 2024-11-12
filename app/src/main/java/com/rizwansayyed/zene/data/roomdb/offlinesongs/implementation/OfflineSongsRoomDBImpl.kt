package com.rizwansayyed.zene.data.roomdb.offlinesongs.implementation

import com.rizwansayyed.zene.data.api.model.ZeneMusicDataItems
import com.rizwansayyed.zene.data.roomdb.offlinesongs.OfflineSongsDatabase
import com.rizwansayyed.zene.data.roomdb.offlinesongs.model.OfflineSongsData
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject


class OfflineSongsRoomDBImpl @Inject constructor(
    private val offlineDB: OfflineSongsDatabase
) : OfflineSongsDBInterface {

    override suspend fun save(music: ZeneMusicDataItems) = flow {
        offlineDB.offlineSongsDao().deleteAll(music.id ?: "")
        val ts = System.currentTimeMillis()
        val data = OfflineSongsData(music.id!!, music.name, music.artists, music.thumbnail, ts)
        emit(offlineDB.offlineSongsDao().insert(data))
    }.flowOn(Dispatchers.IO)


    override suspend fun getLists(page: Int) = flow {
        val limit = page * 30
        emit(offlineDB.offlineSongsDao().get(limit))
    }.flowOn(Dispatchers.IO)

    override suspend fun isSaved(id: String) = flow {
        emit(offlineDB.offlineSongsDao().isSaved(id).size)
    }.flowOn(Dispatchers.IO)
}