package com.rizwansayyed.zene.di.service

import com.rizwansayyed.zene.data.onlinesongs.ip.implementation.IpJsonImpl
import com.rizwansayyed.zene.data.onlinesongs.ip.implementation.IpJsonImplInterface
import com.rizwansayyed.zene.data.onlinesongs.radio.implementation.OnlineRadioImpl
import com.rizwansayyed.zene.data.onlinesongs.radio.implementation.OnlineRadioImplInterface
import com.rizwansayyed.zene.service.player.notificationservice.PlayerServiceNotification
import com.rizwansayyed.zene.service.player.notificationservice.PlayerServiceNotificationInterface
import com.rizwansayyed.zene.service.player.playeractions.PlayerServiceAction
import com.rizwansayyed.zene.service.player.playeractions.PlayerServiceActionInterface
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
abstract class PlayServiceNotificationModule {

    @Binds
    @Singleton
    abstract fun playServiceNotification(playerNotification: PlayerServiceNotification): PlayerServiceNotificationInterface

}