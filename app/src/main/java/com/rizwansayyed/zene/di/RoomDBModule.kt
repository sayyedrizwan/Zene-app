package com.rizwansayyed.zene.di

import android.content.Context
import androidx.room.Room
import com.rizwansayyed.zene.data.roomdb.offlinesongs.OfflineSongsDatabase
import com.rizwansayyed.zene.data.roomdb.updates.UpdatesDatabase
import com.rizwansayyed.zene.utils.Utils.RoomDB.OFFLINE_SONGS_ROOM_DB
import com.rizwansayyed.zene.utils.Utils.RoomDB.UPDATE_ROOM_DB
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent


@Module
@InstallIn(SingletonComponent::class)
object RoomDBModule {

    @Provides
    fun earphoneUpdatedDB(@ApplicationContext context: Context): UpdatesDatabase {
        return Room.databaseBuilder(context, UpdatesDatabase::class.java, UPDATE_ROOM_DB).build()
    }

    @Provides
    fun offlineSongsDB(@ApplicationContext context: Context): OfflineSongsDatabase {
        return Room.databaseBuilder(context, OfflineSongsDatabase::class.java, OFFLINE_SONGS_ROOM_DB ).build()
    }

}