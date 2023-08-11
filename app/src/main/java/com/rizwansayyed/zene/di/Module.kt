package com.rizwansayyed.zene.di

import android.content.Context
import androidx.room.Room
import com.rizwansayyed.zene.BuildConfig
import com.rizwansayyed.zene.domain.ApiInterface
import com.rizwansayyed.zene.domain.ApiInterfaceImpl
import com.rizwansayyed.zene.domain.IPApiInterface
import com.rizwansayyed.zene.roomdb.RoomDBImpl
import com.rizwansayyed.zene.roomdb.recentplayed.RecentPlayedDB
import com.rizwansayyed.zene.roomdb.recentplayed.RecentPlayedDao
import com.rizwansayyed.zene.utils.Utils
import com.rizwansayyed.zene.utils.Utils.DB.RECENT_PLAYED_DB
import com.rizwansayyed.zene.utils.Utils.URL.IP_JSON_BASE_URL
import com.rizwansayyed.zene.utils.Utils.moshi
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
object Module {

    @Provides
    @Singleton
    fun retrofitAPIService(): ApiInterface = Retrofit.Builder()
        .addConverterFactory(MoshiConverterFactory.create(moshi))
        .baseUrl(BuildConfig.DOMAIN_KEY)
        .build()
        .create(ApiInterface::class.java)

    @Provides
    @Singleton
    fun retrofitIPService(): IPApiInterface = Retrofit.Builder()
        .addConverterFactory(MoshiConverterFactory.create(moshi))
        .baseUrl(IP_JSON_BASE_URL)
        .build()
        .create(IPApiInterface::class.java)


    @Provides
    @Singleton
    fun apiInterfaceImpl(api: ApiInterface, ip: IPApiInterface): ApiInterfaceImpl =
        ApiInterfaceImpl(api, ip)

    @Provides
    @Singleton
    fun recentPlayedDB(@ApplicationContext context: Context): RecentPlayedDao =
        Room.databaseBuilder(context, RecentPlayedDB::class.java, RECENT_PLAYED_DB).build()
            .recentPlayer()

    @Provides
    @Singleton
    fun roomDBImpl(dao: RecentPlayedDao, api: ApiInterface, ip: IPApiInterface): RoomDBImpl =
        RoomDBImpl(dao, api, ip)
}