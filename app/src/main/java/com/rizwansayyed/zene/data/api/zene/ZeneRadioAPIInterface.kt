package com.rizwansayyed.zene.data.api.zene

import com.rizwansayyed.zene.data.api.model.IpJsonResponse
import com.rizwansayyed.zene.data.api.model.MoodLists
import com.rizwansayyed.zene.data.api.model.StatusResponse
import com.rizwansayyed.zene.data.api.model.ZeneArtistsData
import com.rizwansayyed.zene.data.api.model.ZeneArtistsDataResponse
import com.rizwansayyed.zene.data.api.model.ZeneArtistsInfoResponse
import com.rizwansayyed.zene.data.api.model.ZeneArtistsPostsResponse
import com.rizwansayyed.zene.data.api.model.ZeneBooleanResponse
import com.rizwansayyed.zene.data.api.model.ZeneLyricsData
import com.rizwansayyed.zene.data.api.model.ZeneMoodPlaylistData
import com.rizwansayyed.zene.data.api.model.ZeneMusicDataItems
import com.rizwansayyed.zene.data.api.model.ZeneMusicDataResponse
import com.rizwansayyed.zene.data.api.model.ZeneMusicHistoryResponse
import com.rizwansayyed.zene.data.api.model.ZeneMusicImportPlaylistsDataResponse
import com.rizwansayyed.zene.data.api.model.ZenePlaylistAlbumsData
import com.rizwansayyed.zene.data.api.model.ZeneSavedPlaylistsResponse
import com.rizwansayyed.zene.data.api.model.ZeneSearchData
import com.rizwansayyed.zene.data.api.model.ZeneSponsorsResponse
import com.rizwansayyed.zene.data.api.model.ZeneUpdateAvailabilityResponse
import com.rizwansayyed.zene.data.api.model.ZeneUsersResponse
import com.rizwansayyed.zene.data.api.model.ZeneVideosMusicData
import kotlinx.coroutines.flow.Flow
import java.io.File

interface ZeneRadioAPIInterface {

    suspend fun topRadio(): Flow<List<MoodLists>>
    suspend fun radioLanguages(): Flow<ZeneMusicDataResponse>
    suspend fun radioCountries(): Flow<ZeneMusicDataResponse>
    suspend fun radiosYouMayLike(): Flow<ZeneMusicDataResponse>
    suspend fun radiosViaLanguages(language: String): Flow<ZeneMusicDataResponse>
}