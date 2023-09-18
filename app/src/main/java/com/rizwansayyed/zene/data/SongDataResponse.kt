package com.rizwansayyed.zene.data

sealed class SongDataResponse<out T> {
    data object Empty : SongDataResponse<Nothing>()
    data object Loading : SongDataResponse<Nothing>()
    data class Error(val throwable: Throwable) : SongDataResponse<Nothing>()
    data class Success<T>(val item: T) : SongDataResponse<T>()
}