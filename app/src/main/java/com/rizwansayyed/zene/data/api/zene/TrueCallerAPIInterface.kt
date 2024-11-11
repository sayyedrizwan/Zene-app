package com.rizwansayyed.zene.data.api.zene

import com.rizwansayyed.zene.data.api.model.TrueCallerUserInfoResponse
import kotlinx.coroutines.flow.Flow

interface TrueCallerAPIInterface {

    suspend fun userInfo(code: String, codeVerifier: String): Flow<TrueCallerUserInfoResponse>
}