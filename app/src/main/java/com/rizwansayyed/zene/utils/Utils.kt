package com.rizwansayyed.zene.utils

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import com.rizwansayyed.zene.di.ApplicationModule.Companion.context
import com.rizwansayyed.zene.presenter.MainActivity
import com.rizwansayyed.zene.presenter.util.UiUtils.toast
import kotlin.random.Random
import kotlin.system.exitProcess


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

    fun artistsListToString(list: MutableList<String>): String {
        val artistName = buildString {
            for (i in 0 until list.size) {
                if (i > 0) {
                    if (i == list.size.minus(1)) {
                        append(" & ")
                    } else {
                        append(", ")
                    }
                }
                append(list[i])
            }
        }

        return artistName
    }

    fun restartTheApp(context: Activity) {
        "11111".toast()
        val intent = context.packageManager.getLaunchIntentForPackage(context.packageName)
        context.finishAffinity()
        context.startActivity(intent)
        exitProcess(0)
        "runnqqq111".toast()
    }

}