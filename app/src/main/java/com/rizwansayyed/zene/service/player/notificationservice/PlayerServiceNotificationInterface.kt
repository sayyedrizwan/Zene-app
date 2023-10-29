package com.rizwansayyed.zene.service.player.notificationservice

import com.rizwansayyed.zene.service.PlayerService

interface PlayerServiceNotificationInterface {

    fun buildNotificationChannel()

    fun buildNotification(playerService: PlayerService)
}