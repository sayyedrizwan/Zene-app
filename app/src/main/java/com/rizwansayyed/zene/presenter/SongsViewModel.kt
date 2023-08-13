package com.rizwansayyed.zene.presenter

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rizwansayyed.zene.BaseApplication.Companion.dataStoreManager
import com.rizwansayyed.zene.domain.ApiInterfaceImpl
import com.rizwansayyed.zene.domain.roomdb.RoomDBImpl
import com.rizwansayyed.zene.domain.roomdb.recentplayed.RecentPlayedEntity
import com.rizwansayyed.zene.utils.DateTime.is1DayOlderNeedCache
import com.rizwansayyed.zene.utils.DateTime.is2DayOlderNeedCache
import com.rizwansayyed.zene.utils.DateTime.isOlderNeedCache
import com.rizwansayyed.zene.utils.Utils.showToast
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.launch
import org.jsoup.Jsoup
import javax.inject.Inject
import kotlin.time.Duration.Companion.seconds

@HiltViewModel
class SongsViewModel @Inject constructor(
    private val apiImpl: ApiInterfaceImpl,
    private val roomDBImpl: RoomDBImpl
) : ViewModel() {

    private var footerDataTried = 0

    fun run() {
        footerDataTried = 0
        recentPlaySongs()

        viewModelScope.launch(Dispatchers.IO) {
            albumsWithHeaders()
            topWeekArtists()
            topGlobalSongsThisWeek()
        }

        viewModelScope.launch(Dispatchers.IO) {
            delay(2.seconds)
            topArtists()
            topCountrySong()
            songsSuggestions()
        }

        viewModelScope.launch(Dispatchers.IO) {
            delay(4.seconds)
            songsSuggestionsForYou()
            trendingSongsTop50()
            trendingSongsTopKPop()
            trendingSongsTop50KPop()
            similarArtistsSuggestionsForYou()
            topArtistsSongs()
        }
    }

    private fun albumsWithHeaders() = viewModelScope.launch(Dispatchers.IO) {
        if (!dataStoreManager.albumHeaderTimestamp.first().isOlderNeedCache() &&
            dataStoreManager.albumHeaderData.first() != null &&
            dataStoreManager.albumHeaderData.first()?.isNotEmpty() == true
        ) return@launch

        val channelUrl = try {
            apiImpl.albumsWithHeaders().first().url
        } catch (e: Exception) {
            ""
        }
        if (channelUrl.isEmpty()) return@launch

        apiImpl.albumsWithYTHeaders(channelUrl).catch {}.collectLatest {
            dataStoreManager.albumHeaderTimestamp = flowOf(System.currentTimeMillis())
            it.header?.let { h ->
                dataStoreManager.albumHeaderData = flowOf(h.toTypedArray())
            }
            it.albums?.let { a ->
                dataStoreManager.footerAlbumsData = flowOf(a.toTypedArray())
            }

            if (it.albums?.isEmpty() == true && footerDataTried < 3) {
                footerDataTried += 1
                delay(3.seconds)
                reRunFooter()
            }
        }
    }

    private fun reRunFooter() {
        albumsWithHeaders()
    }

    private fun topWeekArtists() = viewModelScope.launch(Dispatchers.IO) {
        if (!dataStoreManager.topArtistsOfWeekTimestamp.first().is2DayOlderNeedCache() &&
            dataStoreManager.topArtistsOfWeekData.first() != null &&
            dataStoreManager.topArtistsOfWeekData.first()?.isNotEmpty() == true
        ) return@launch

        apiImpl.topArtistOfWeek().catch {}.collectLatest {
            dataStoreManager.topArtistsOfWeekTimestamp = flowOf(System.currentTimeMillis())
            dataStoreManager.topArtistsOfWeekData = flowOf(it.toTypedArray())
        }
    }

    private fun topGlobalSongsThisWeek() = viewModelScope.launch(Dispatchers.IO) {
        if (!dataStoreManager.topGlobalSongsTimestamp.first().is1DayOlderNeedCache() &&
            dataStoreManager.topGlobalSongsData.first() != null &&
            dataStoreManager.topGlobalSongsData.first()?.isNotEmpty() == true
        ) return@launch

        apiImpl.topGlobalSongsThisWeek().catch {}.collectLatest {
            dataStoreManager.topGlobalSongsTimestamp = flowOf(System.currentTimeMillis())
            dataStoreManager.topGlobalSongsData = flowOf(it.toTypedArray())
        }
    }

    var recentPlayedSongs by mutableStateOf<Flow<List<RecentPlayedEntity>>?>(null)
        private set

    private fun recentPlaySongs() = viewModelScope.launch(Dispatchers.IO) {
        roomDBImpl.recentPlayed().catch {}.collectLatest {
            recentPlayedSongs = it
        }
    }

    var topArtistsSuggestions by mutableStateOf<List<RecentPlayedEntity>?>(null)
        private set

    private fun topArtists() = viewModelScope.launch(Dispatchers.IO) {
        roomDBImpl.topArtistsSuggestions().catch {}.collectLatest {
            topArtistsSuggestions = it
        }
    }

    private fun topCountrySong() = viewModelScope.launch(Dispatchers.IO) {
        if (!dataStoreManager.topCountrySongsTimestamp.first().isOlderNeedCache() &&
            dataStoreManager.topCountrySongsData.first() != null &&
            dataStoreManager.topCountrySongsData.first()?.isNotEmpty() == true
        ) return@launch

        apiImpl.topCountrySongs().catch {}.collectLatest {
            dataStoreManager.topCountrySongsTimestamp = flowOf(System.currentTimeMillis())
            dataStoreManager.topCountrySongsData = flowOf(it.toTypedArray())
        }
    }


    private fun trendingSongsTop50() = viewModelScope.launch(Dispatchers.IO) {
        if (!dataStoreManager.trendingSongsTop50Timestamp.first().is2DayOlderNeedCache() &&
            dataStoreManager.trendingSongsTop50Data.first() != null &&
            dataStoreManager.trendingSongsTop50Data.first()?.isNotEmpty() == true
        ) return@launch

        apiImpl.trendingSongsTop50().catch {}.collectLatest {
            dataStoreManager.trendingSongsTop50Timestamp = flowOf(System.currentTimeMillis())
            it.shuffled()
            dataStoreManager.trendingSongsTop50Data = flowOf(it.toTypedArray())
        }
    }


    private fun songsSuggestions() = viewModelScope.launch(Dispatchers.IO) {
        if (!dataStoreManager.songsSuggestionsTimestamp.first().is1DayOlderNeedCache() &&
            dataStoreManager.songsSuggestionsData.first() != null &&
            dataStoreManager.songsSuggestionsData.first()?.isNotEmpty() == true
        ) {
            val s = dataStoreManager.songsSuggestionsData.first()
            s?.shuffle()
            s?.shuffle()
            dataStoreManager.songsSuggestionsData = flowOf(s)
            return@launch
        }

        roomDBImpl.songsSuggestionsUsingSongsHistory().catch {}.collectLatest {
            dataStoreManager.songsSuggestionsTimestamp = flowOf(System.currentTimeMillis())
            dataStoreManager.songsSuggestionsData = flowOf(it.toTypedArray())
        }
    }

    private fun songsSuggestionsForYou() = viewModelScope.launch(Dispatchers.IO) {
        if (!dataStoreManager.songsSuggestionsForYouTimestamp.first().is1DayOlderNeedCache() &&
            dataStoreManager.songsSuggestionsForYouData.first() != null &&
            dataStoreManager.songsSuggestionsForYouData.first()?.isNotEmpty() == true
        ) {
            val s = dataStoreManager.songsSuggestionsForYouData.first()
            s?.shuffle()
            s?.shuffle()
            dataStoreManager.songsSuggestionsForYouData = flowOf(s)
            return@launch
        }

        roomDBImpl.songSuggestionsForYouUsingHistory().catch {}.collectLatest {
            dataStoreManager.songsSuggestionsForYouTimestamp = flowOf(System.currentTimeMillis())
            dataStoreManager.songsSuggestionsForYouData = flowOf(it.toTypedArray())
        }
    }


    private fun trendingSongsTopKPop() = viewModelScope.launch(Dispatchers.IO) {
        if (!dataStoreManager.trendingSongsTopKPopTimestamp.first().is2DayOlderNeedCache() &&
            dataStoreManager.trendingSongsTopKPopData.first() != null &&
            dataStoreManager.trendingSongsTopKPopData.first()?.isNotEmpty() == true
        ) return@launch

        apiImpl.trendingSongsTopKPop().catch {}.collectLatest {
            dataStoreManager.trendingSongsTopKPopTimestamp = flowOf(System.currentTimeMillis())
            dataStoreManager.trendingSongsTopKPopData = flowOf(it.toTypedArray())
        }
    }


    private fun trendingSongsTop50KPop() = viewModelScope.launch(Dispatchers.IO) {
        if (!dataStoreManager.trendingSongsTop50KPopTimestamp.first().is2DayOlderNeedCache() &&
            dataStoreManager.trendingSongsTop50KPopData.first() != null &&
            dataStoreManager.trendingSongsTop50KPopData.first()?.isNotEmpty() == true
        ) {
            val s = dataStoreManager.trendingSongsTop50KPopData.first()
            s?.shuffle()
            s?.shuffle()
            s?.shuffle()
            dataStoreManager.trendingSongsTop50KPopData = flowOf(s)
            return@launch
        }

        apiImpl.trendingSongsTop50KPop().catch {}.collectLatest {
            dataStoreManager.trendingSongsTop50KPopTimestamp = flowOf(System.currentTimeMillis())
            dataStoreManager.trendingSongsTop50KPopData = flowOf(it.toTypedArray())
        }
    }


    private fun similarArtistsSuggestionsForYou() = viewModelScope.launch(Dispatchers.IO) {
        if (!dataStoreManager.artistsSuggestionsTimestamp.first().is2DayOlderNeedCache() &&
            dataStoreManager.artistsSuggestionsData.first() != null &&
            dataStoreManager.artistsSuggestionsData.first()?.isNotEmpty() == true
        ) {
            val s = dataStoreManager.artistsSuggestionsData.first()
            s?.shuffle()
            s?.shuffle()
            dataStoreManager.artistsSuggestionsData = flowOf(s)
            return@launch
        }

        roomDBImpl.artistsSuggestionsForYouUsingHistory().catch {}.collectLatest {
            dataStoreManager.artistsSuggestionsTimestamp = flowOf(System.currentTimeMillis())
            dataStoreManager.artistsSuggestionsData = flowOf(it.toTypedArray())
        }
    }


    private fun topArtistsSongs() = viewModelScope.launch(Dispatchers.IO) {
        if (!dataStoreManager.topArtistsSongsDataTimestamp.first().is2DayOlderNeedCache() &&
            dataStoreManager.topArtistsSongsData.first() != null &&
            dataStoreManager.topArtistsSongsData.first()?.isNotEmpty() == true
        ) return@launch

        roomDBImpl.topArtistsSongs().catch {}.collectLatest {
            dataStoreManager.topArtistsSongsDataTimestamp = flowOf(System.currentTimeMillis())
            dataStoreManager.topArtistsSongsData = flowOf(it.toTypedArray())
        }
    }


    fun insert() = viewModelScope.launch(Dispatchers.IO) {
//        val insert = RecentPlayedEntity(
//            id = null,
//            name = "Last Night",
//            "The Weeknd",
//            6,
//            "J7p4bzqLvCw",
//            "https://lh3.googleusercontent.com/R_cjQK3wwLPEzri1jerx-79zgzGocoKvwGU3NMONaTsaMM0Idd641pfB8r5jgfpn6I8JAoFtf9RBIcI=w544-h544-l90-rj",
//            System.currentTimeMillis(),
//            233
//        )
//        roomDBImpl.insert(insert).catch { }.collect()
//
//        val insert1 = RecentPlayedEntity(
//            id = null,
//            name = "What Jhumka?",
//            "Arijit Singh & Jonita Gandhi",
//            3,
//            "87JIOAX3njM",
//            "https://i.ytimg.com/vi/irF2QC5xP2g/hq720.jpg?sqp=-oaymwEXCKAGEMIDSFryq4qpAwkIARUAAIhCGAE=&rs=AOn4CLAbXgumdDeyMiDoWm1ZqRls3xPlBA",
//            System.currentTimeMillis(),
//            233
//        )
//        roomDBImpl.insert(insert1).catch { }.collect()
//
//        val insert2 = RecentPlayedEntity(
//            id = null,
//            name = "Main Nikla Gaddi Leke",
//            "Udit Narayan & Mithoon",
//            3,
//            "2nK6WBcGPOw",
//            "https://i.ytimg.com/vi/irF2QC5xP2g/hq720.jpg?sqp=-oaymwEXCKAGEMIDSFryq4qpAwkIARUAAIhCGAE=&rs=AOn4CLAbXgumdDeyMiDoWm1ZqRls3xPlBA",
//            System.currentTimeMillis(),
//            233
//        )
//        roomDBImpl.insert(insert2).catch { }.collect()
//
//        val insert3 = RecentPlayedEntity(
//            id = null,
//            name = "Gone Girl",
//            "Badshah & Payal Dev",
//            3,
//            "mvCWGL_r-Zg",
//            "https://i.ytimg.com/vi/irF2QC5xP2g/hq720.jpg?sqp=-oaymwEXCKAGEMIDSFryq4qpAwkIARUAAIhCGAE=&rs=AOn4CLAbXgumdDeyMiDoWm1ZqRls3xPlBA",
//            System.currentTimeMillis(),
//            233
//        )
//        roomDBImpl.insert(insert3).catch { }.collect()
//
//        val insert4 = RecentPlayedEntity(
//            id = null,
//            name = "Mummy Nu Pasand (From \"Jai Mummy Di\")",
//            "Sunanda Sharma",
//            3,
//            "F-4gKsi5jfw",
//            "https://i.ytimg.com/vi/irF2QC5xP2g/hq720.jpg?sqp=-oaymwEXCKAGEMIDSFryq4qpAwkIARUAAIhCGAE=&rs=AOn4CLAbXgumdDeyMiDoWm1ZqRls3xPlBA",
//            System.currentTimeMillis(),
//            233
//        )
//        roomDBImpl.insert(insert4).catch { }.collect()
//
//        val insert5 = RecentPlayedEntity(
//            id = null,
//            name = "I'm the One (feat. Justin Bieber, Lil Wayne, Chance The Rapper & Quavo)",
//            "DJ Khaled",
//            7,
//            "weeI1G46q0o",
//            "https://i.ytimg.com/vi/irF2QC5xP2g/hq720.jpg?sqp=-oaymwEXCKAGEMIDSFryq4qpAwkIARUAAIhCGAE=&rs=AOn4CLAbXgumdDeyMiDoWm1ZqRls3xPlBA",
//            System.currentTimeMillis(),
//            233
//        )
//        roomDBImpl.insert(insert5).catch { }.collect()
//
//        val insert6 = RecentPlayedEntity(
//            id = null,
//            name = "I'm the One (feat. Justin Bieber, Lil Wayne, Chance The Rapper & Quavo)",
//            "DJ Khaled",
//            7,
//            "weeI1G46q0o",
//            "https://i.ytimg.com/vi/irF2QC5xP2g/hq720.jpg?sqp=-oaymwEXCKAGEMIDSFryq4qpAwkIARUAAIhCGAE=&rs=AOn4CLAbXgumdDeyMiDoWm1ZqRls3xPlBA",
//            System.currentTimeMillis(),
//            233
//        )
//        roomDBImpl.insert(insert6).catch { }.collect()
//
//    val insert7 = RecentPlayedEntity(
//            id = null,
//            name = "Beautiful Mistakes ft. Megan Thee Stallion (Official Music Video",
//            "Maroon 5",
//            7,
//            "BSzSn-PRdtI",
//            "https://i.ytimg.com/vi/irF2QC5xP2g/hq720.jpg?sqp=-oaymwEXCKAGEMIDSFryq4qpAwkIARUAAIhCGAE=&rs=AOn4CLAbXgumdDeyMiDoWm1ZqRls3xPlBA",
//            System.currentTimeMillis(),
//            233
//        )
//        roomDBImpl.insert(insert7).catch { }.collect()
//
//    val insert8 = RecentPlayedEntity(
//            id = null,
//            name = "22",
//            "Taylor Swift",
//            7,
//            "AgFeZr5ptV8",
//            "https://i.ytimg.com/vi/irF2QC5xP2g/hq720.jpg?sqp=-oaymwEXCKAGEMIDSFryq4qpAwkIARUAAIhCGAE=&rs=AOn4CLAbXgumdDeyMiDoWm1ZqRls3xPlBA",
//            System.currentTimeMillis(),
//            233
//        )
//        roomDBImpl.insert(insert8).catch { }.collect()
    }
}