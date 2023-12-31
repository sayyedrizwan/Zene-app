package com.rizwansayyed.zene.data.onlinesongs.applemusic.implementation

import com.rizwansayyed.zene.domain.ImportPlaylistTrackInfoData
import com.rizwansayyed.zene.domain.MusicData
import com.rizwansayyed.zene.domain.applemusic.AppleMusicPlaylistItemResponse
import com.rizwansayyed.zene.domain.applemusic.AppleMusicPlaylistTracksResponse
import kotlinx.coroutines.flow.Flow

interface AppleMusicAPIImplInterface {

    suspend fun playlistsTracks(id: String): Flow<MutableList<ImportPlaylistTrackInfoData>>
    suspend fun userPlaylists(): Flow<AppleMusicPlaylistItemResponse?>
}