package com.rizwansayyed.zene.roomdb.recentplayed

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [RecentPlayedEntity::class], version = 1)
abstract class RecentPlayedDB : RoomDatabase() {
    abstract fun recentPlayer(): RecentPlayedDao
}