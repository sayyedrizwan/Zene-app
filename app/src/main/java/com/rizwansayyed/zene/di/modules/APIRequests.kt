package com.rizwansayyed.zene.di.modules

import com.rizwansayyed.zene.data.ZeneAPIService
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
    fun provideAnalyticsService(): ZeneAPIService {
        val okHttpClient = OkHttpClient.Builder().readTimeout(60, TimeUnit.SECONDS)
            .connectTimeout(60, TimeUnit.SECONDS).build()

        return Retrofit.Builder().baseUrl(ZENE_BASE_URL_API)
            .addConverterFactory(MoshiConverterFactory.create()).build()
            .create(ZeneAPIService::class.java)
    }
}