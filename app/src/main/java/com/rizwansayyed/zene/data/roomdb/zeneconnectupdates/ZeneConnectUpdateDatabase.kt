package com.rizwansayyed.zene.data.roomdb.zeneconnectupdates

import androidx.room.Database
import androidx.room.RoomDatabase
import com.rizwansayyed.zene.data.roomdb.zeneconnectupdates.model.ZeneConnectUpdatesModel


@Database(entities = [ZeneConnectUpdatesModel::class], version = 1)
abstract class ZeneConnectUpdateDatabase : RoomDatabase() {
    abstract fun dao(): ZeneConnectUpdatesDao
}