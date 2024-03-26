package com.rizwansayyed.zene.di.onlinemodule

import com.rizwansayyed.zene.data.onlinesongs.fileuploader.FileUploaderImpl
import com.rizwansayyed.zene.data.onlinesongs.fileuploader.FileUploaderImplInterface
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
abstract class FileUploaderImplementation {

    @Binds
    @Singleton
    abstract fun fileUploaderImpl(fileUploaderImpl: FileUploaderImpl): FileUploaderImplInterface

}