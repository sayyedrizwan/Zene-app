package com.rizwansayyed.zene.data.onlinesongs.instagram.stories

import android.util.Log
import com.rizwansayyed.zene.data.db.datastore.DataStorageManager
import com.rizwansayyed.zene.data.onlinesongs.cache.responseCache
import com.rizwansayyed.zene.data.onlinesongs.cache.returnFromCache2Hours
import com.rizwansayyed.zene.data.onlinesongs.cache.writeToCacheFile
import com.rizwansayyed.zene.data.onlinesongs.instagram.SaveFromInstagramService
import com.rizwansayyed.zene.data.onlinesongs.jsoupscrap.jsoupResponseData
import com.rizwansayyed.zene.data.onlinesongs.lastfm.LastFMService
import com.rizwansayyed.zene.data.onlinesongs.youtube.implementation.YoutubeAPIImplInterface
import com.rizwansayyed.zene.data.utils.CacheFiles
import com.rizwansayyed.zene.data.utils.CacheFiles.recentMostPlayedSongs
import com.rizwansayyed.zene.data.utils.LastFM.LAST_FM_BASE_URL
import com.rizwansayyed.zene.data.utils.LastFM.artistsEventInfo
import com.rizwansayyed.zene.data.utils.LastFM.artistsTopSongsInfo
import com.rizwansayyed.zene.data.utils.LastFM.artistsWikiInfo
import com.rizwansayyed.zene.data.utils.LastFM.searchLastFMImageURLPath
import com.rizwansayyed.zene.data.utils.YoutubeAPI
import com.rizwansayyed.zene.data.utils.config.RemoteConfigInterface
import com.rizwansayyed.zene.data.utils.moshi
import com.rizwansayyed.zene.di.ApplicationModule.Companion.context
import com.rizwansayyed.zene.domain.ArtistsArtists
import com.rizwansayyed.zene.domain.ArtistsEvents
import com.rizwansayyed.zene.domain.MusicData
import com.rizwansayyed.zene.domain.MusicDataWithArtists
import com.rizwansayyed.zene.domain.MusicDataWithArtistsCache
import com.rizwansayyed.zene.domain.MusicType
import com.rizwansayyed.zene.domain.lastfm.ArtistsSearchResponse
import com.rizwansayyed.zene.domain.lastfm.LastFMArtist
import com.rizwansayyed.zene.domain.toCache
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flatMapMerge
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import org.jsoup.Jsoup
import java.io.File
import javax.inject.Inject

class SaveFromStoriesImpl @Inject constructor(
    private val saveFromAPI: SaveFromInstagramService
) : SaveFromStoriesImplInterface {

    override suspend fun storiesList(username: String) = flow {
        val id = saveFromAPI.userInfoByUsername(username).result?.user?.pk
        Log.d("TAG", "storiesList: get data $id ")
        val stories = saveFromAPI.userStories(id)
        Log.d("TAG", "storiesList: get data ${stories.result}")
        emit(stories.result)
    }.flowOn(Dispatchers.IO)
}