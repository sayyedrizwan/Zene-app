package com.rizwansayyed.zene.roomdb

import com.rizwansayyed.zene.datastore.DataStorageManager.userInfo
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONObject
import java.util.concurrent.TimeUnit
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class StoriesNewsRepository @Inject constructor(private val dao: StoriesNewsDao) {

    fun getArtistsInfo(ids: List<String>) = flow {
        emit(dao.getAllArtists(ids))
    }.flowOn(Dispatchers.IO)

    fun updateArtists(item: StoriesNewsEntity) = flow {
        emit(dao.insertArtist(item))
    }.flowOn(Dispatchers.IO)
}

