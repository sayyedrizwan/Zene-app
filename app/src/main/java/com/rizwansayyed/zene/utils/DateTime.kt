package com.rizwansayyed.zene.utils

import java.util.Calendar

object DateTime {

    fun getPastTimestamp(days: Int = -10): Long {
        return Calendar.getInstance().apply {
            add(Calendar.DAY_OF_MONTH, days)
        }.timeInMillis
    }

    fun Long.isOlderNeedCache(): Long {

        val currentTimeMillis = System.currentTimeMillis()
        val timeDifferenceMillis = currentTimeMillis - this

        val hours = timeDifferenceMillis / (1000 * 60 * 60)


        return hours

    }
}