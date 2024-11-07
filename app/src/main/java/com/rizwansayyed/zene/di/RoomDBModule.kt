package com.rizwansayyed.zene.di

import android.content.Context
import androidx.room.Room
import com.rizwansayyed.zene.BuildConfig
import com.rizwansayyed.zene.data.api.ip.IpAPIService
import com.rizwansayyed.zene.data.api.zene.ZeneAPIService
import com.rizwansayyed.zene.data.roomdb.UpdatesDatabase
import com.rizwansayyed.zene.utils.Utils.RoomDB.UPDATE_ROOM_DB
import com.rizwansayyed.zene.utils.Utils.URLS.BASE_URL
import com.rizwansayyed.zene.utils.Utils.URLS.BASE_URL_IP
import com.rizwansayyed.zene.utils.Utils.moshi
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import java.util.concurrent.TimeUnit


@Module
@InstallIn(SingletonComponent::class)
object RoomDBModule {

    @Provides
    fun earphoneUpdatedDB(@ApplicationContext context: Context): UpdatesDatabase {
        return Room.databaseBuilder(context, UpdatesDatabase::class.java, UPDATE_ROOM_DB).build()
    }

}