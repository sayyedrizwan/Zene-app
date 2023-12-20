package com.rizwansayyed.zene.data.onlinesongs.instagram.stories

import com.rizwansayyed.zene.domain.ArtistsEvents
import com.rizwansayyed.zene.domain.MusicData
import com.rizwansayyed.zene.domain.MusicDataWithArtists
import com.rizwansayyed.zene.domain.instagram.SaveFromInstagramStoriesResponse
import com.rizwansayyed.zene.domain.lastfm.ArtistsSearchResponse
import com.rizwansayyed.zene.domain.lastfm.LastFMArtist
import kotlinx.coroutines.flow.Flow

interface SaveFromStoriesImplInterface {

    suspend fun storiesList(username: String): Flow<List<SaveFromInstagramStoriesResponse.Result?>?>
}