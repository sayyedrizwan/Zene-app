package com.rizwansayyed.zene.di

import com.rizwansayyed.zene.BuildConfig
import com.rizwansayyed.zene.domain.ApiInterface
import com.rizwansayyed.zene.domain.ApiInterfaceImpl
import com.rizwansayyed.zene.utils.Utils.moshi
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
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
    fun apiInterfaceImpl(api: ApiInterface): ApiInterfaceImpl = ApiInterfaceImpl(api)
}