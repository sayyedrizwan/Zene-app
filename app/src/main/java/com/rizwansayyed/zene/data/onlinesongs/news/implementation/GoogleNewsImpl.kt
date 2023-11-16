package com.rizwansayyed.zene.data.onlinesongs.news.implementation

import com.rizwansayyed.zene.data.onlinesongs.news.GoogleNewsService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject

class GoogleNewsImpl @Inject constructor(
    private val googleNews: GoogleNewsService
) : GoogleNewsInterface {

    override suspend fun searchNews(artists: String) = flow {
        emit(googleNews.searchNews(artists))
    }.flowOn(Dispatchers.IO)


}