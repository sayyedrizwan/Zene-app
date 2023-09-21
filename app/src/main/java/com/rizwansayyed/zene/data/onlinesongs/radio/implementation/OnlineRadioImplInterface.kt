package com.rizwansayyed.zene.data.onlinesongs.radio.implementation

import com.rizwansayyed.zene.domain.OnlineRadioResponse
import kotlinx.coroutines.flow.Flow

interface OnlineRadioImplInterface {

    suspend fun onlineRadioSearch(): Flow<OnlineRadioResponse>

}