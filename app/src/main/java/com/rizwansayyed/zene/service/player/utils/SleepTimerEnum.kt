package com.rizwansayyed.zene.service.player.utils

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import com.rizwansayyed.zene.R
import com.rizwansayyed.zene.di.ZeneBaseApplication.Companion.context
import com.rizwansayyed.zene.service.notification.NotificationUtils
import com.rizwansayyed.zene.service.notification.NotificationUtils.Companion.SLEEP_TIMER_NOTIFICATION
import com.rizwansayyed.zene.service.notification.NotificationUtils.Companion.SLEEP_TIMER_NOTIFICATION_DESC

enum class SleepTimerEnum(val time: Int) {
    FIVE(5), TEN(10), FIFTEEN(15), THIRTY(30), FORTY_FIVE(45), SIXTY_MINUTES(60),
    END_OF_TRACK(-1), TURN_OFF(0)
}

var sleepTimerSelected by mutableStateOf(SleepTimerEnum.TURN_OFF)

fun sleepTimerNotification() {
    val title = context.resources.getString(R.string.timer_completed)
    val body = context.resources.getString(R.string.timer_completed_desc)
    NotificationUtils(title, body).apply {
        channel(SLEEP_TIMER_NOTIFICATION, SLEEP_TIMER_NOTIFICATION_DESC)
        generate()
    }
}