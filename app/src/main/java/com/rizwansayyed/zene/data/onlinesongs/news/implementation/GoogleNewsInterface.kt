package com.rizwansayyed.zene.data.onlinesongs.news.implementation

import com.rizwansayyed.zene.domain.news.GoogleNewsResponse
import kotlinx.coroutines.flow.Flow

interface GoogleNewsInterface {
    suspend fun searchNews(artists: String): Flow<GoogleNewsResponse>
}