package com.rizwansayyed.zene.di.implementation

import com.rizwansayyed.zene.data.api.ImgBBAPIImpl
import com.rizwansayyed.zene.data.api.ZeneAPIImpl
import com.rizwansayyed.zene.data.api.imgbb.ImgBBAPIInterface
import com.rizwansayyed.zene.data.api.zene.ZeneAPIInterface
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent

@Module
@InstallIn(ViewModelComponent::class)
abstract class ImgBBAPIModule {

    @Binds
    abstract fun imgBBAPIImplementation(impl: ImgBBAPIImpl): ImgBBAPIInterface
}