package com.rizwansayyed.zene.data.db.impl

import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.PagingSource
import androidx.paging.PagingState
import androidx.paging.RemoteMediator
import com.rizwansayyed.zene.data.db.savedplaylist.playlist.SavedPlaylistDB
import com.rizwansayyed.zene.data.db.savedplaylist.playlist.SavedPlaylistDao
import com.rizwansayyed.zene.data.db.savedplaylist.playlist.SavedPlaylistEntity
import com.rizwansayyed.zene.data.utils.PAGINATION_PAGE_SIZE
import javax.inject.Inject

class PlaylistPagingMediator @Inject constructor(
    private val database: SavedPlaylistDao
) : PagingSource<Int, SavedPlaylistEntity>() {
    override fun getRefreshKey(state: PagingState<Int, SavedPlaylistEntity>) =
        ((state.anchorPosition ?: 0) - state.config.initialLoadSize / 2).coerceAtLeast(0)

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, SavedPlaylistEntity> {
        return try {
            val page = (params.key ?: 0) * PAGINATION_PAGE_SIZE
            val response = database.pagingPlaylist(page)

            LoadResult.Page(
                data = response,
                prevKey = if (page == 0) null else page - 1,
                nextKey = if (response.isEmpty()) null else page + 1
            )
        } catch (e: Exception) {
            LoadResult.Error(e)
        }
    }


}