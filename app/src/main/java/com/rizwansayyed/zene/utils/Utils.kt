package com.rizwansayyed.zene.utils

import android.widget.Toast
import com.rizwansayyed.zene.BaseApplication.Companion.context
import com.squareup.moshi.Moshi
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.cancel
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch

object Utils {

    object URL {
        const val ALBUMS_WITH_HEADERS = "albumsWithHeaders"
    }

    val moshi: Moshi = Moshi.Builder().build()


    fun Any.showToast(v: String) = CoroutineScope(Dispatchers.Main).launch {
        Toast.makeText(context, v, Toast.LENGTH_LONG).show()
        if (isActive) cancel()
    }

}