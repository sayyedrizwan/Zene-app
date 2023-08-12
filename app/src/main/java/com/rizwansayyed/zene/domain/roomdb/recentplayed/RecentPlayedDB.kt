package com.rizwansayyed.zene.domain.roomdb.recentplayed

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [RecentPlayedEntity::class], version = 2)
abstract class RecentPlayedDB : RoomDatabase() {
    abstract fun recentPlayer(): RecentPlayedDao
}