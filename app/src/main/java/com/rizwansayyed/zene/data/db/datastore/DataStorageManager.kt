package com.rizwansayyed.zene.data.db.datastore

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.preferencesDataStore
import com.rizwansayyed.zene.data.db.datastore.DataStorageUtil.DATA_STORE_DB
import com.rizwansayyed.zene.data.db.datastore.DataStorageUtil.IP_JSON
import com.rizwansayyed.zene.data.utils.moshi
import com.rizwansayyed.zene.di.ApplicationModule.Companion.context
import com.rizwansayyed.zene.domain.IpJsonResponse
import kotlinx.coroutines.cancel
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.isActive
import kotlinx.coroutines.runBlocking

object DataStorageManager {
    private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(DATA_STORE_DB)

    var userIpDetails: IpJsonResponse
        get() = runBlocking {
            context.dataStore.data.map {
                moshi.adapter(IpJsonResponse::class.java).fromJson(it[IP_JSON] ?: "{}")
            }.first()!!
        }
        set(v) = runBlocking {
            val moshi = moshi.adapter(IpJsonResponse::class.java).toJson(v)
            context.dataStore.edit { it[IP_JSON] = moshi }
        }
}