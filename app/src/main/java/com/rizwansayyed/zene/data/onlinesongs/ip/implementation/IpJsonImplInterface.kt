package com.rizwansayyed.zene.data.onlinesongs.ip.implementation

import com.rizwansayyed.zene.domain.IpJsonResponse
import com.rizwansayyed.zene.domain.OnlineRadioResponse
import kotlinx.coroutines.flow.Flow

interface IpJsonImplInterface {
    suspend fun ip(): Flow<IpJsonResponse>
}