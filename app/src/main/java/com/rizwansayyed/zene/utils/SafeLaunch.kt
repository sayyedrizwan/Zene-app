package com.rizwansayyed.zene.utils

import android.util.Log
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import kotlin.coroutines.CoroutineContext
import kotlin.coroutines.cancellation.CancellationException

fun CoroutineScope.safeLaunch(
    tag: String = "",
    context: CoroutineContext = Dispatchers.IO,
    block: suspend CoroutineScope.() -> Unit
): Job {
    return this.launch(context) {
        try {
            block()
        } catch (e: CancellationException) {
            Log.d("safeLaunch", e.message ?: "")
            print(e)
        } catch (e: Exception) {
            Log.d("safeLaunch", e.message ?: "")

            print(e)
        }
    }
}
