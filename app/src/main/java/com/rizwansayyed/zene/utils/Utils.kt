package com.rizwansayyed.zene.utils

import android.content.Context
import android.net.ConnectivityManager
import android.os.Build
import com.rizwansayyed.zene.di.ApplicationModule.Companion.context
import kotlin.random.Random


object Utils {
    fun isInternetConnected(): Boolean {
        val cm = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        return cm.activeNetwork != null && cm.getNetworkCapabilities(cm.activeNetwork) != null
    }


    fun <T> weightedRandomChoice(items: List<T>, weights: List<Int> = listOf(3, 3, 1, 1, 1)): T? {
        require(items.size == weights.size) { "Items and weights lists must have the same size" }

        val totalWeight = weights.sum()
        val threshold = Random.nextInt(totalWeight)

        var cumulativeWeight = 0
        for (i in items.indices) {
            cumulativeWeight += weights[i]
            if (threshold < cumulativeWeight) {
                return items[i]
            }
        }

        return null
    }

}