package com.rizwansayyed.zene.di.implementation

import com.rizwansayyed.zene.data.api.ZeneAPIImpl
import com.rizwansayyed.zene.data.api.zene.ZeneAPIInterface
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent

@Module
@InstallIn(ViewModelComponent::class)
abstract class ZeneAPIModule {

    @Binds
    abstract fun zeneAPIImplementation(impl: ZeneAPIImpl): ZeneAPIInterface
}