package com.rizwansayyed.zene.data.roomdb.updates

import androidx.room.Database
import androidx.room.RoomDatabase
import com.rizwansayyed.zene.data.roomdb.updates.model.UpdateData


@Database(entities = [UpdateData::class], version = 1)
abstract class UpdatesDatabase : RoomDatabase() {
    abstract fun updatesDao(): UpdatesDao
}