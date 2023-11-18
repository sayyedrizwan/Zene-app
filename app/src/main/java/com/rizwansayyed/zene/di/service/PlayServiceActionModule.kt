package com.rizwansayyed.zene.di.service

import com.rizwansayyed.zene.service.player.playeractions.PlayerServiceAction
import com.rizwansayyed.zene.service.player.playeractions.PlayerServiceActionInterface
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
abstract class PlayServiceActionModule {

    @Binds
    @Singleton
    abstract fun playService(playerAction: PlayerServiceAction): PlayerServiceActionInterface

}