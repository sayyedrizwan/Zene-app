package com.rizwansayyed.zene.di.service

import com.rizwansayyed.zene.data.onlinesongs.news.implementation.GoogleNewsImpl
import com.rizwansayyed.zene.data.onlinesongs.news.implementation.GoogleNewsInterface
import com.rizwansayyed.zene.service.implementation.recentplay.RecentPlayingSongImpl
import com.rizwansayyed.zene.service.implementation.recentplay.RecentPlayingSongInterface
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
abstract class RecentPlayingSongImplementation {

    @Binds
    @Singleton
    abstract fun recentPlayingSongImpl(recentPlayingSongImpl: RecentPlayingSongImpl): RecentPlayingSongInterface

}