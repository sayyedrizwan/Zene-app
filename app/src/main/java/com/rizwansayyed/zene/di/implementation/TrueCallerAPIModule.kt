package com.rizwansayyed.zene.di.implementation

import com.rizwansayyed.zene.data.api.TrueCallerAPIImpl
import com.rizwansayyed.zene.data.api.zene.TrueCallerAPIInterface
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent

@Module
@InstallIn(ViewModelComponent::class)
abstract class TrueCallerAPIModule {

    @Binds
    abstract fun trueCallerAPIImplementation(impl: TrueCallerAPIImpl): TrueCallerAPIInterface
}