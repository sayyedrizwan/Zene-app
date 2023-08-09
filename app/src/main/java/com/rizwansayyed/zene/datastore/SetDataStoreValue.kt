package com.rizwansayyed.zene.datastore

import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import com.rizwansayyed.zene.BaseApplication
import com.rizwansayyed.zene.BaseApplication.Companion.context
import com.rizwansayyed.zene.datastore.DataStoreManager.Companion.dataStore
import com.rizwansayyed.zene.utils.Utils
import com.rizwansayyed.zene.utils.Utils.moshi
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.cancel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch

class SetDataStoreValueClass<T>(key: Preferences.Key<String>, `class`: Class<T>, value: Flow<T?>) {
    init {
        CoroutineScope(Dispatchers.IO).launch {
            context.dataStore.edit {
                it[key] = moshi.adapter(`class`).toJson(value.first())
            }
            if (isActive) cancel()
        }
    }
}

class SetDataStoreValue<T>(key: Preferences.Key<T>, value: Flow<T>) {
    init {
        CoroutineScope(Dispatchers.IO).launch {
            context.dataStore.edit { it[key] = value.first() }
            if (isActive) cancel()
        }
    }
}