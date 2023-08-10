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

object Utils {

    object URL {
        const val ALBUMS_WITH_HEADERS = "albumsWithHeadersExp"
    }


    val moshi: Moshi = Moshi.Builder().add(KotlinJsonAdapterFactory()).build()

    fun String.showToast() = CoroutineScope(Dispatchers.Main).launch {
        Toast.makeText(context, this@showToast, Toast.LENGTH_LONG).show()
        if (isActive) cancel()
    }

}