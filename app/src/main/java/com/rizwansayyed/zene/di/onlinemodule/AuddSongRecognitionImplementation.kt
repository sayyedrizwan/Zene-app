package com.rizwansayyed.zene.di.onlinemodule

import com.rizwansayyed.zene.data.onlinesongs.auddrecognition.implementation.AuddSongRecognitionImpl
import com.rizwansayyed.zene.data.onlinesongs.auddrecognition.implementation.AuddSongRecognitionInterface
import com.rizwansayyed.zene.data.onlinesongs.news.implementation.GoogleNewsImpl
import com.rizwansayyed.zene.data.onlinesongs.news.implementation.GoogleNewsInterface
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
abstract class AuddSongRecognitionImplementation {

    @Binds
    @Singleton
    abstract fun auddSongRecognitionImpl(auddSongRecognitionImpl: AuddSongRecognitionImpl): AuddSongRecognitionInterface

}