package com.rizwansayyed.zene.di

import android.content.Context
import androidx.room.Room
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
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
        .addMigrations(MIGRATION_1_2_RECENT_PLAYED).build().recentPlayedDao()

    private val MIGRATION_1_2_RECENT_PLAYED = object : Migration(1, 2) {
        override fun migrate(db: SupportSQLiteDatabase) {
            db.execSQL("ALTER TABLE $recent_played_db RENAME COLUMN img TO thumbnail")
        }
    }

}