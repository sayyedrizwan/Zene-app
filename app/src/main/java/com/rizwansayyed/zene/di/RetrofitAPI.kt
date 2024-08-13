package com.rizwansayyed.zene.di

import com.rizwansayyed.zene.BuildConfig
import com.rizwansayyed.zene.data.api.ip.IpAPIService
import com.rizwansayyed.zene.data.api.zene.ZeneAPIService
import com.rizwansayyed.zene.utils.Utils.URLS.BASE_URL
import com.rizwansayyed.zene.utils.Utils.URLS.BASE_URL_IP
import com.rizwansayyed.zene.utils.Utils.moshi
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import java.util.concurrent.TimeUnit


@Module
@InstallIn(SingletonComponent::class)
object RetrofitAPI {

    @Provides
    fun zeneAPIService(): ZeneAPIService {
        val okHttpClient = OkHttpClient.Builder().readTimeout(3, TimeUnit.MINUTES)
            .connectTimeout(3, TimeUnit.MINUTES)

//        okHttpClient.addInterceptor { chain ->
//            chain.proceed(
//                chain.request().newBuilder().addHeader("auth", BuildConfig.AUTH_HEADER).build()
//            )
//        }

        return Retrofit.Builder().baseUrl(BASE_URL).client(okHttpClient.build())
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