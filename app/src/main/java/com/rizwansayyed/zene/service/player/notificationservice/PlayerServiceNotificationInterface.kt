package com.rizwansayyed.zene.service.player.notificationservice

import android.app.PendingIntent
import com.rizwansayyed.zene.service.PlayerService

interface PlayerServiceNotificationInterface {

    fun buildNotificationChannel()

    fun buildNotification(playerService: PlayerService)
    fun openMusicPlayerIntent(): PendingIntent
}