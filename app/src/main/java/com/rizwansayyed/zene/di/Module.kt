package com.rizwansayyed.zene.di

import android.content.Context
import androidx.room.Room
import com.rizwansayyed.zene.BuildConfig
import com.rizwansayyed.zene.domain.ApiInterface
import com.rizwansayyed.zene.domain.ApiInterfaceImpl
import com.rizwansayyed.zene.domain.IPApiInterface
import com.rizwansayyed.zene.domain.roomdb.RoomDBImpl
import com.rizwansayyed.zene.domain.roomdb.recentplayed.RecentPlayedDB
import com.rizwansayyed.zene.domain.roomdb.recentplayed.RecentPlayedDao
import com.rizwansayyed.zene.domain.roomdb.songsdetails.SongDetailsDB
import com.rizwansayyed.zene.domain.roomdb.songsdetails.SongDetailsDao
import com.rizwansayyed.zene.service.musicplayer.MediaPlayerObjects
import com.rizwansayyed.zene.utils.Utils.DB.RECENT_PLAYED_DB
import com.rizwansayyed.zene.utils.Utils.DB.SONG_DETAILS_DB
import com.rizwansayyed.zene.utils.Utils.URL.IP_JSON_BASE_URL
import com.rizwansayyed.zene.utils.Utils.moshi
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
object Module {

    @Provides
    @Singleton
    fun retrofitAPIService(): ApiInterface {

        val httpClient = OkHttpClient.Builder()
            .connectTimeout(60, TimeUnit.SECONDS)
            .writeTimeout(60, TimeUnit.SECONDS)
            .readTimeout(2, TimeUnit.MINUTES)

        return Retrofit.Builder()
            .addConverterFactory(MoshiConverterFactory.create(moshi))
            .baseUrl(BuildConfig.DOMAIN_KEY)
            .client(httpClient.build())
            .build()
            .create(ApiInterface::class.java)
    }

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
    fun songDetailsDB(@ApplicationContext context: Context): SongDetailsDao =
        Room.databaseBuilder(context, SongDetailsDB::class.java, SONG_DETAILS_DB).build()
            .songDetails()

    @Provides
    @Singleton
    fun roomDBImpl(
        dao: RecentPlayedDao, api: ApiInterface, ip: IPApiInterface, song: SongDetailsDao
    ): RoomDBImpl = RoomDBImpl(dao, api, ip, song)


    @Provides
    @Singleton
    fun mediaPlayerObjects(@ApplicationContext context: Context) = MediaPlayerObjects(context)
}