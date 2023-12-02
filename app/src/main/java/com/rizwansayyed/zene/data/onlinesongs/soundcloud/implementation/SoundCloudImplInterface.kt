package com.rizwansayyed.zene.data.onlinesongs.soundcloud.implementation

import com.rizwansayyed.zene.domain.soundcloud.SoundCloudProfileInfo
import com.rizwansayyed.zene.domain.soundcloud.SoundCloudProfileResponse
import com.rizwansayyed.zene.domain.soundcloud.SoundCloudSearchResponse
import kotlinx.coroutines.flow.Flow

interface SoundCloudImplInterface {

    suspend fun artistsProfileDetails(q: String): Flow<SoundCloudProfileInfo>
    suspend fun artistsSmallInfo(q: String): Flow<SoundCloudSearchResponse.Collection?>
}