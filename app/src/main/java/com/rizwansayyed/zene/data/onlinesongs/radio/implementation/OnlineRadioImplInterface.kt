package com.rizwansayyed.zene.data.onlinesongs.radio.implementation

import com.rizwansayyed.zene.domain.OnlineRadioResponse
import com.rizwansayyed.zene.domain.OnlineRadioResponseItem
import kotlinx.coroutines.flow.Flow

interface OnlineRadioImplInterface {

    suspend fun onlineRadioSearch(all: Boolean = true): Flow<OnlineRadioResponse>

    suspend fun favouriteRadioLists(uuid: String): Flow<List<OnlineRadioResponseItem>>
    suspend fun searchOnlineRadio(q: String): Flow<OnlineRadioResponse>
}