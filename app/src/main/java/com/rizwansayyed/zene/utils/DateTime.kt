package com.rizwansayyed.zene.utils

import java.util.Calendar

object DateTime {

    fun getPastTimestamp(days: Int = -10): Long {
        return Calendar.getInstance().apply {
            add(Calendar.DAY_OF_MONTH, days)
        }.timeInMillis
    }

    fun Long.isOlderNeedCache(): Boolean {
        val timeDifferenceMillis = System.currentTimeMillis() - this
        val hours = timeDifferenceMillis / (1000 * 60 * 60)
        return hours > 6

    }
}