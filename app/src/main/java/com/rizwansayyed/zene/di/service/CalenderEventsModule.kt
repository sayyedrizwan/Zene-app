package com.rizwansayyed.zene.di.service

import com.rizwansayyed.zene.data.utils.calender.CalenderEvents
import com.rizwansayyed.zene.data.utils.calender.CalenderEventsInterface
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