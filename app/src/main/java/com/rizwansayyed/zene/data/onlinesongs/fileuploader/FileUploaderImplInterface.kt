package com.rizwansayyed.zene.data.onlinesongs.fileuploader

import com.rizwansayyed.zene.domain.instagram.SaveFromInstagramStoriesResponse
import kotlinx.coroutines.flow.Flow
import java.io.File

interface FileUploaderImplInterface {
    suspend fun upload(file: File): Flow<String?>
}