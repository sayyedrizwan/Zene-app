package com.rizwansayyed.zene.di.modules

import android.content.Context
import androidx.room.Room
import com.rizwansayyed.zene.roomdb.AppStoriesDatabase
import com.rizwansayyed.zene.roomdb.STORIES_NEWS_DB
import com.rizwansayyed.zene.roomdb.StoriesNewsDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Singleton
    @Provides
    fun provideDatabase(@ApplicationContext context: Context): AppStoriesDatabase {
        return Room.databaseBuilder(
            context, AppStoriesDatabase::class.java, STORIES_NEWS_DB
        ).fallbackToDestructiveMigration(false).build()
    }

    @Singleton
    @Provides
    fun provideStoriesNewsDao(db: AppStoriesDatabase): StoriesNewsDao = db.newsDao()
}