package com.rizwansayyed.zene.data.db.recentplay

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [RecentPlayedEntity::class], version = 3)
abstract class RecentPlayerDB : RoomDatabase() {
    abstract fun dao(): RecentPlayedDao
}