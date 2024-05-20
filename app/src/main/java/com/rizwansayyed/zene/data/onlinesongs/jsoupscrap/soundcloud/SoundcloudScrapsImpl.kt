package com.rizwansayyed.zene.data.onlinesongs.jsoupscrap.soundcloud

import android.util.Log
import com.rizwansayyed.zene.data.onlinesongs.cache.writeToCacheFile
import com.rizwansayyed.zene.data.onlinesongs.jsoupscrap.jsoupResponseData
import com.rizwansayyed.zene.data.utils.GeniusURL.GENIUS_BASE_URL
import com.rizwansayyed.zene.data.utils.GeniusURL.geniusMusicSearch
import com.rizwansayyed.zene.data.utils.RentAdvisorSubtitles.RENT_ADVISER_BASE_URL
import com.rizwansayyed.zene.data.utils.RentAdvisorSubtitles.searchOnRentAdviser
import com.rizwansayyed.zene.data.utils.SoundCloudAPI.SOUND_CLOUD_CLIENT_ID
import com.rizwansayyed.zene.data.utils.SoundCloudAPI.SOUND_CLOUD_MAIN_BASE_URL
import com.rizwansayyed.zene.data.utils.moshi
import com.rizwansayyed.zene.di.ApplicationModule.Companion.context
import com.rizwansayyed.zene.domain.MusicPlayerList
import com.rizwansayyed.zene.domain.subtitles.GeniusLyricsWithInfo
import com.rizwansayyed.zene.domain.subtitles.GeniusSearchResponse
import com.rizwansayyed.zene.presenter.ui.musicplayer.utils.Utils.areSongNamesEqual
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import org.jsoup.Jsoup
import java.io.File
import javax.inject.Inject

class SoundcloudScrapsImpl @Inject constructor() : SoundcloudScrapImplInterface {

    override suspend fun getClientId() = flow {
        val response = jsoupResponseData(SOUND_CLOUD_MAIN_BASE_URL)
        val jsoup = Jsoup.parse(response!!)
        val clientId = jsoup.html().substringAfter("\"clientId\":\"").substringBefore("\",\"")
        emit(clientId)
    }.flowOn(Dispatchers.IO)
}