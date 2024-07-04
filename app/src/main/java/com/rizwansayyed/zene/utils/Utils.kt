package com.rizwansayyed.zene.utils

import android.widget.Toast
import com.rizwansayyed.zene.di.BaseApp.Companion.context
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

object Utils {
    val moshi: Moshi = Moshi.Builder().addLast(KotlinJsonAdapterFactory()).build()


    fun Any.toast() = CoroutineScope(Dispatchers.Main).launch {
        Toast.makeText(context, this@toast.toString(), Toast.LENGTH_LONG).show()
    }
}