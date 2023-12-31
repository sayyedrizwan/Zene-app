package com.rizwansayyed.zene.data.onlinesongs.applemusic.implementation

import com.rizwansayyed.zene.data.db.datastore.DataStorageManager.appleMusicToken
import com.rizwansayyed.zene.data.onlinesongs.applemusic.AppleMusicAPIService
import com.rizwansayyed.zene.domain.ImportPlaylistTrackInfoData
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject


class AppleMusicAPIImpl @Inject constructor(
    private val appleMusic: AppleMusicAPIService
) : AppleMusicAPIImplInterface {

    override suspend fun userPlaylists() = flow {
        val token = appleMusicToken.first()
        emit(appleMusic.appleMusicPlaylists(token?.authorization!!, token.cookie))
    }.flowOn(Dispatchers.IO)


    override suspend fun playlistsTracks(id: String) = flow {
        val list = mutableListOf<ImportPlaylistTrackInfoData>()
        val token = appleMusicToken.first()
        val playlists =
            appleMusic.appleMusicPlaylistsTracks(token?.authorization!!, token.cookie, id)

        playlists.data?.forEach { t ->
            t?.relationships?.tracks?.data?.forEach { tracks ->
                val l = ImportPlaylistTrackInfoData(
                    tracks?.attributes?.artwork?.url?.replace("{w}", "592")?.replace("{h}", "592"),
                    tracks?.attributes?.artistName,
                    tracks?.attributes?.name
                )
                list.add(l)
            }
        }

        emit(list)
    }.flowOn(Dispatchers.IO)


}