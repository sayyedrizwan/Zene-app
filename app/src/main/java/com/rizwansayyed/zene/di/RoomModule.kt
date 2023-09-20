package com.rizwansayyed.zene.di

import android.content.Context
import androidx.room.Room
import com.rizwansayyed.zene.data.db.recentplay.RecentPlayedDao
import com.rizwansayyed.zene.data.db.recentplay.RecentPlayerDB
import com.rizwansayyed.zene.data.db.utils.DbName.recent_played_db
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
object RoomModule {

    @Provides
    @Singleton
    fun recentPlayedDao(
        @ApplicationContext context: Context
    ): RecentPlayedDao = Room.databaseBuilder(context, RecentPlayerDB::class.java, recent_played_db)
        .build().recentPlayedDao()


}