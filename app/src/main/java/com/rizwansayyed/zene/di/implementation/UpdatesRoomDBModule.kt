package com.rizwansayyed.zene.di.implementation

import com.rizwansayyed.zene.data.api.ZeneRadioAPIImpl
import com.rizwansayyed.zene.data.api.zene.ZeneRadioAPIInterface
import com.rizwansayyed.zene.data.roomdb.implementation.UpdatesRoomDBImpl
import com.rizwansayyed.zene.data.roomdb.implementation.UpdatesRoomDBInterface
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent

@Module
@InstallIn(ViewModelComponent::class)
abstract class UpdatesRoomDBModule {

    @Binds
    abstract fun updatesRoomDBImplementation(impl: UpdatesRoomDBImpl): UpdatesRoomDBInterface
}