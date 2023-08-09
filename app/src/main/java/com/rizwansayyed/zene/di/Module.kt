package com.rizwansayyed.zene.di

import com.rizwansayyed.zene.BuildConfig
import com.rizwansayyed.zene.api.ApiInterface
import com.rizwansayyed.zene.api.ApiInterfaceImpl
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
        .baseUrl(BuildConfig.DOMAIN_KEY)
        .addConverterFactory(MoshiConverterFactory.create())
        .build()
        .create(ApiInterface::class.java)


    @Provides
    @Singleton
    fun apiInterfaceImpl(api: ApiInterface): ApiInterfaceImpl = ApiInterfaceImpl(api)
}