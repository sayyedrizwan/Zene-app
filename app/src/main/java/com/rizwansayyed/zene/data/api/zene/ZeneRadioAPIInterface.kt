package com.rizwansayyed.zene.data.api.zene

import com.rizwansayyed.zene.data.api.model.MoodLists
import com.rizwansayyed.zene.data.api.model.ZeneMusicDataItems
import com.rizwansayyed.zene.data.api.model.ZeneMusicDataResponse
import kotlinx.coroutines.flow.Flow

interface ZeneRadioAPIInterface {

    suspend fun topRadio(): Flow<List<MoodLists>>
    suspend fun radioLanguages(): Flow<ZeneMusicDataResponse>
    suspend fun radioCountries(): Flow<ZeneMusicDataResponse>
    suspend fun radiosYouMayLike(): Flow<ZeneMusicDataResponse>
    suspend fun radiosViaLanguages(language: String): Flow<ZeneMusicDataResponse>
    suspend fun radioViaCountries(country: String, page: Int): Flow<ZeneMusicDataResponse>
    suspend fun radioInfo(id: String): Flow<ZeneMusicDataItems>
}