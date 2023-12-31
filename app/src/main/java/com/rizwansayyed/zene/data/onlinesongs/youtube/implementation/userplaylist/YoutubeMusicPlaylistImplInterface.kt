package com.rizwansayyed.zene.data.onlinesongs.youtube.implementation.userplaylist

import com.rizwansayyed.zene.data.db.artistsfeed.ArtistsFeedEntity
import com.rizwansayyed.zene.domain.ArtistsFanData
import com.rizwansayyed.zene.domain.ImportPlaylistInfoData
import com.rizwansayyed.zene.domain.ImportPlaylistTrackInfoData
import com.rizwansayyed.zene.domain.IpJsonResponse
import com.rizwansayyed.zene.domain.MusicData
import com.rizwansayyed.zene.domain.PlaylistItemsData
import com.rizwansayyed.zene.domain.SearchData
import com.rizwansayyed.zene.domain.SongsSuggestionsData
import com.rizwansayyed.zene.domain.yt.MerchandiseItems
import kotlinx.coroutines.flow.Flow

interface YoutubeMusicPlaylistImplInterface {

    suspend fun usersPlaylists(): Flow<MutableList<ImportPlaylistInfoData>>
    suspend fun playlistsTracker(id: String): Flow<MutableList<ImportPlaylistTrackInfoData>>
}