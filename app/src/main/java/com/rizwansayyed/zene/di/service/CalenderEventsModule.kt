package com.rizwansayyed.zene.di.service

import com.rizwansayyed.zene.data.utils.calender.CalenderEvents
import com.rizwansayyed.zene.data.utils.calender.CalenderEventsInterface
import com.rizwansayyed.zene.service.player.playeractions.PlayerServiceAction
import com.rizwansayyed.zene.service.player.playeractions.PlayerServiceActionInterface
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
abstract class CalenderEventsModule {

    @Binds
    @Singleton
    abstract fun calenderEvents(calenderEvents: CalenderEvents): CalenderEventsInterface

}