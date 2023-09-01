package com.rizwansayyed.zene.utils

import android.widget.Toast
import com.rizwansayyed.zene.BaseApplication.Companion.context
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.cancel
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch

object Algorithims {

    object URL {
        const val ALBUMS_WITH_HEADERS = "albumsWithHeaders"
    }

    fun randomIds(): String {
        return (111..9999999999).random().toString()
    }

}