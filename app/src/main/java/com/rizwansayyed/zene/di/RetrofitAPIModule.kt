package com.rizwansayyed.zene.di

import com.rizwansayyed.zene.data.onlinesongs.instagram.InstagramInfoService
import com.rizwansayyed.zene.data.onlinesongs.ip.IpJsonService
import com.rizwansayyed.zene.data.onlinesongs.radio.OnlineRadioService
import com.rizwansayyed.zene.data.utils.InstagramAPI.INSTAGRAM_BASE_URL
import com.rizwansayyed.zene.data.utils.IpJsonAPI.IP_BASE_URL
import com.rizwansayyed.zene.data.utils.USER_AGENT
import com.rizwansayyed.zene.data.utils.moshi
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import java.io.IOException
import java.util.concurrent.TimeUnit


@Module
@InstallIn(SingletonComponent::class)
object RetrofitAPIModule {

    private var okHttpClient = OkHttpClient.Builder()
        .connectTimeout(1, TimeUnit.MINUTES)
        .readTimeout(30, TimeUnit.SECONDS)
        .writeTimeout(30, TimeUnit.SECONDS)
        .build()

    @Provides
    fun retrofitOnlineRadioService(): OnlineRadioService = Retrofit.Builder()
        .baseUrl("https://demo.com").client(okHttpClient)
        .addConverterFactory(MoshiConverterFactory.create(moshi))
        .build()
        .create(OnlineRadioService::class.java)


    @Provides
    fun retrofitIpJsonService(): IpJsonService = Retrofit.Builder()
        .baseUrl(IP_BASE_URL).client(okHttpClient)
        .addConverterFactory(MoshiConverterFactory.create(moshi))
        .build()
        .create(IpJsonService::class.java)


    @Provides
    fun retrofitInstagramService(): InstagramInfoService {
        val builder = OkHttpClient.Builder()
        builder.addInterceptor(Interceptor { chain: Interceptor.Chain ->
            val chains = chain.request().newBuilder()
                .addHeader("authority", "www.instagram.com")
                .addHeader("user-agent", USER_AGENT)
            chain.proceed(chains.build())
        })
        return Retrofit.Builder()
            .baseUrl(INSTAGRAM_BASE_URL).client(okHttpClient)
            .addConverterFactory(MoshiConverterFactory.create(moshi))
            .client(builder.build())
            .build()
            .create(InstagramInfoService::class.java)
    }
}