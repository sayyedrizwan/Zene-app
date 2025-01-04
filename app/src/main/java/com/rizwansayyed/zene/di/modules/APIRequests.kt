package com.rizwansayyed.zene.di.modules

import com.rizwansayyed.zene.data.IPAPIService
import com.rizwansayyed.zene.data.ZeneAPIService
import com.rizwansayyed.zene.utils.MainUtils.moshi
import com.rizwansayyed.zene.utils.URLSUtils.IP_BASE_URL
import com.rizwansayyed.zene.utils.URLSUtils.ZENE_BASE_URL_API
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
object APIRequests {

    @Singleton
    @Provides
    fun zeneAPIService(): ZeneAPIService {
        val okHttpClient = OkHttpClient.Builder().readTimeout(60, TimeUnit.SECONDS)
            .connectTimeout(60, TimeUnit.SECONDS).build()

        return Retrofit.Builder().baseUrl(ZENE_BASE_URL_API).client(okHttpClient)
            .addConverterFactory(MoshiConverterFactory.create(moshi)).build()
            .create(ZeneAPIService::class.java)
    }

    @Singleton
    @Provides
    fun ipAPIService(): IPAPIService {
        val okHttpClient = OkHttpClient.Builder().readTimeout(60, TimeUnit.SECONDS)
            .connectTimeout(60, TimeUnit.SECONDS).build()

        return Retrofit.Builder().baseUrl(IP_BASE_URL).client(okHttpClient)
            .addConverterFactory(MoshiConverterFactory.create(moshi)).build()
            .create(IPAPIService::class.java)
    }
}