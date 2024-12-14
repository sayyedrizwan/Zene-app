package com.rizwansayyed.zene.data.roomdb.zeneconnect

import androidx.room.Database
import androidx.room.RoomDatabase
import com.rizwansayyed.zene.data.roomdb.updates.model.UpdateData
import com.rizwansayyed.zene.data.roomdb.zeneconnect.model.ZeneConnectContactsModel


@Database(entities = [ZeneConnectContactsModel::class], version = 1)
abstract class ZeneConnectContactDatabase : RoomDatabase() {
    abstract fun contactsDao(): ZeneConnectContactsDao
}