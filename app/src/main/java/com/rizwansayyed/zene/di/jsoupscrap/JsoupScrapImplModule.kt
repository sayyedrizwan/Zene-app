package com.rizwansayyed.zene.di.jsoupscrap

import com.rizwansayyed.zene.data.onlinesongs.jsoupscrap.JsoupScrapImpl
import com.rizwansayyed.zene.data.onlinesongs.jsoupscrap.JsoupScrapInterface
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
abstract class JsoupScrapImplementation {

    @Binds
    @Singleton
    abstract fun jsoupScrapImpl(jsoupScrapImpl: JsoupScrapImpl): JsoupScrapInterface

}
