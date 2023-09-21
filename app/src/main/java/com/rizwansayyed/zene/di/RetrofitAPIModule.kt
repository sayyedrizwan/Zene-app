package com.rizwansayyed.zene.di

import com.rizwansayyed.zene.data.onlinesongs.ip.IpJsonService
import com.rizwansayyed.zene.data.onlinesongs.radio.OnlineRadioService
import com.rizwansayyed.zene.data.utils.IpJsonAPI.IP_BASE_URL
import com.rizwansayyed.zene.data.utils.RadioOnlineAPI.RADIO_BASE_URL
import com.rizwansayyed.zene.data.utils.moshi
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory


@Module
@InstallIn(SingletonComponent::class)
object RetrofitAPIModule {

    @Provides
    fun retrofitOnlineRadioService(): OnlineRadioService = Retrofit.Builder()
        .baseUrl(RADIO_BASE_URL)
        .addConverterFactory(MoshiConverterFactory.create(moshi))
        .build()
        .create(OnlineRadioService::class.java)


    @Provides
    fun retrofitIpJsonService(): IpJsonService = Retrofit.Builder()
        .baseUrl(IP_BASE_URL)
        .addConverterFactory(MoshiConverterFactory.create(moshi))
        .build()
        .create(IpJsonService::class.java)
}