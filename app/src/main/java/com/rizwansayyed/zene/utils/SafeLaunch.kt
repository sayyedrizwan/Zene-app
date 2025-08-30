package com.rizwansayyed.zene.utils

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import kotlin.coroutines.CoroutineContext
import kotlin.coroutines.cancellation.CancellationException

fun CoroutineScope.safeLaunch(
    context: CoroutineContext = Dispatchers.IO,
    block: suspend CoroutineScope.() -> Unit
): Job {
    return this.launch(context) {
        try {
            block()
        } catch (e: CancellationException) {
            print(e)
        } catch (e: Exception) {
            print(e)
        }
    }
}
