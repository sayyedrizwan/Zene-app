package com.rizwansayyed.zene.di.onlinemodule

import com.rizwansayyed.zene.data.onlinesongs.ip.implementation.IpJsonImpl
import com.rizwansayyed.zene.data.onlinesongs.ip.implementation.IpJsonImplInterface
import com.rizwansayyed.zene.data.onlinesongs.radio.implementation.OnlineRadioImpl
import com.rizwansayyed.zene.data.onlinesongs.radio.implementation.OnlineRadioImplInterface
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
abstract class IpJsonImplementation {

    @Binds
    @Singleton
    abstract fun ipJsonImpl(ipJsonImpl: IpJsonImpl): IpJsonImplInterface

}