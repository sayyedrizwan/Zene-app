package com.rizwansayyed.zene.utils

import java.util.Calendar

object DateTime {

    fun getPastTimestamp(days: Int = -10): Long {
        return Calendar.getInstance().apply {
            add(Calendar.DAY_OF_MONTH, days)
        }.timeInMillis
    }

    fun Long.isMoreThan25Minute(): Boolean {
        val timeDifferenceMillis = System.currentTimeMillis() - this
        val minutes = timeDifferenceMillis / (1000 * 60)
        return minutes > 25
    }

    fun Long.isOlderNeedCache(): Boolean {
        val timeDifferenceMillis = System.currentTimeMillis() - this
        val hours = timeDifferenceMillis / (1000 * 60 * 60)
        return hours > 6
    }

    fun Long.is2DayOlderNeedCache(): Boolean {
        val timeDifferenceMillis = System.currentTimeMillis() - this
        val hours = timeDifferenceMillis / (1000 * 60 * 60)
        return hours > 48
    }

    fun Long.is30DayOlderNeedCache(): Boolean {
        val timeDifferenceMillis = System.currentTimeMillis() - this
        val hours = timeDifferenceMillis / (1000 * 60 * 60)
        return hours > 740
    }

    fun Long.is5DayOlderNeedCache(): Boolean {
        val timeDifferenceMillis = System.currentTimeMillis() - this
        val hours = timeDifferenceMillis / (1000 * 60 * 60)
        return hours > 120
    }

    fun Long.is1DayOlderNeedCache(): Boolean {
        val timeDifferenceMillis = System.currentTimeMillis() - this
        val hours = timeDifferenceMillis / (1000 * 60 * 60)
        return hours > 25
    }
}