package com.rizwansayyed.zene.data.onlinesongs.pinterest.implementation

import com.rizwansayyed.zene.data.onlinesongs.pinterest.PinterestAPIService
import com.rizwansayyed.zene.data.utils.PinterestAPI.pinterestSearch
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject

class PinterestAPIImpl @Inject constructor(private val pinterest: PinterestAPIService) :
    PinterestAPIImplInterface {

    override suspend fun search(name: String, artist: String) = flow {
        val lists = mutableListOf<String>()
        artist.split("&", ",").forEach { a ->
            val response = pinterest.searchPosts(pinterestSearch("$name $a").trim())
            response.resource_response?.data?.results?.forEach {
                if (it?.grid_title?.lowercase()
                        ?.contains(name.trim().lowercase().substringBefore("(")) == true ||
                    it?.grid_title?.lowercase()?.contains(artist.trim().lowercase()) == true
                ) if (!lists.any { i -> i == it.images?.orig?.url })
                    it.images?.orig?.url?.let { it1 ->
                        lists.add(it1)
                    }
            }
        }
        emit(lists)
    }.flowOn(Dispatchers.IO)

    override suspend fun search(name: String) = flow {
        val lists = mutableListOf<String>()
        val response = pinterest.searchPosts(pinterestSearch(name).trim())
        response.resource_response?.data?.results?.forEach {
            it?.images?.orig?.url?.let { it1 ->
                lists.add(it1)
            }
        }
        emit(lists)
    }.flowOn(Dispatchers.IO)
}