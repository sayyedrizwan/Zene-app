package com.rizwansayyed.zene.di

import com.rizwansayyed.zene.data.api.ip.IpAPIService
import com.rizwansayyed.zene.data.api.zene.ZeneAPIService
import com.rizwansayyed.zene.utils.Utils.URLS.BASE_URL
import com.rizwansayyed.zene.utils.Utils.URLS.BASE_URL_IP
import com.rizwansayyed.zene.utils.Utils.moshi
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityComponent
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RetrofitAPI {

    @Provides
    fun zeneAPIService(): ZeneAPIService {
        return Retrofit.Builder().baseUrl(BASE_URL)
            .addConverterFactory(MoshiConverterFactory.create(moshi)).build()
            .create(ZeneAPIService::class.java)
    }

    @Provides
    fun ipAPIService(): IpAPIService {
        return Retrofit.Builder().baseUrl(BASE_URL_IP)
            .addConverterFactory(MoshiConverterFactory.create(moshi)).build()
            .create(IpAPIService::class.java)
    }

}