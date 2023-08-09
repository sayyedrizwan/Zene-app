package com.rizwansayyed.zene.datastore

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.preferencesDataStore
import com.rizwansayyed.zene.BaseApplication.Companion.context
import com.rizwansayyed.zene.api.model.ablumsheader.AlbumsHeadersReponse
import com.rizwansayyed.zene.datastore.DataStoreUtil.DATA_STORE_KEY.DB_STORE
import com.rizwansayyed.zene.datastore.DataStoreUtil.album_header_data
import com.rizwansayyed.zene.datastore.DataStoreUtil.album_header_timestamp
import com.rizwansayyed.zene.utils.DateTime.getPastTimestamp
import com.rizwansayyed.zene.utils.Utils.moshi
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.cancel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch

class DataStoreManager {

    companion object {
        val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = DB_STORE)
    }


    var albumHeaderData: Flow<AlbumsHeadersReponse?>
        get() = context.dataStore.data.map {
            moshi.adapter(AlbumsHeadersReponse::class.java).fromJson(it[album_header_data] ?: "{}")
        }
        set(value) {
            SetDataStoreValueClass(album_header_data, AlbumsHeadersReponse::class.java, value)
        }

    var albumHeaderTimestamp: Flow<Long>
        get() = context.dataStore.data.map { it[album_header_timestamp] ?: getPastTimestamp() }
        set(value) {
            SetDataStoreValue(album_header_timestamp, value)
        }
}