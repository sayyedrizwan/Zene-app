package com.rizwansayyed.zene.api

import com.rizwansayyed.zene.ApiInterfaceImplInterface
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject

class ApiInterfaceImpl @Inject constructor(private val apiInterface: ApiInterface) :
    ApiInterfaceImplInterface {

    override suspend fun albumsWithHeaders() = flow {
        emit(apiInterface.albumsWithHeaders())
    }.flowOn(Dispatchers.IO)
}