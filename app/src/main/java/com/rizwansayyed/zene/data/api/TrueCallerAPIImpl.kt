package com.rizwansayyed.zene.data.api

import com.rizwansayyed.zene.data.api.truecaller.TrueCallerAPIService
import com.rizwansayyed.zene.data.api.zene.TrueCallerAPIInterface
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject

class TrueCallerAPIImpl @Inject constructor(
    private val api: TrueCallerAPIService
) : TrueCallerAPIInterface {

    override suspend fun userInfo(code: String, codeVerifier: String) = flow {
        val token = api.token(code, codeVerifier)
        emit(api.userInfo("${token.token_type} ${token.access_token}"))
    }.flowOn(Dispatchers.IO)
}