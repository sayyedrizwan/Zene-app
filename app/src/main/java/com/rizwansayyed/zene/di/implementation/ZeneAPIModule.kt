package com.rizwansayyed.zene.di.implementation

import com.rizwansayyed.zene.data.implementation.ZeneAPIImplementation
import com.rizwansayyed.zene.data.implementation.ZeneAPIInterface
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class ZeneAPIModule {

    @Singleton
    @Binds
    abstract fun zeneAPIImplementation(zeneAPIImpl: ZeneAPIImplementation): ZeneAPIInterface
}