package com.rizwansayyed.zene.data.onlinesongs.jsoupscrap.social

import android.util.Log
import com.rizwansayyed.zene.data.onlinesongs.jsoupscrap.jsoupResponseData
import com.rizwansayyed.zene.data.utils.ScrapURL.songKickArtistsCalendar
import com.rizwansayyed.zene.data.utils.ScrapURL.songKickArtistsCalendarInfo
import com.rizwansayyed.zene.data.utils.ScrapURL.songKickArtistsSearch
import com.rizwansayyed.zene.domain.ArtistsEvents
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import org.jsoup.Jsoup
import javax.inject.Inject

class SocialMediaScrapsImpl @Inject constructor() : SocialMediaScrapsImplInterface {

    override suspend fun getAllArtistsData(a: String) = flow {

        emit("")
    }.flowOn(Dispatchers.IO)

}