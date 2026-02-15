package com.rizwansayyed.zene.roomdb

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(
    entities = [StoriesNewsEntity::class], version = 1, exportSchema = false
)
abstract class AppStoriesDatabase : RoomDatabase() {
    abstract fun newsDao(): StoriesNewsDao
}
