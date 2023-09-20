package com.rizwansayyed.zene.data.db.recentplay

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [RecentPlayedEntity::class], version = 2)
abstract class RecentPlayerDB : RoomDatabase() {
    abstract fun recentPlayedDao(): RecentPlayedDao
}