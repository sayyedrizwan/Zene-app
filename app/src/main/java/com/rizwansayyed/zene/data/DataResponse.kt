package com.rizwansayyed.zene.data

open class DataResponse<out T> {
    data object Empty : DataResponse<Nothing>()
    data object Loading : DataResponse<Nothing>()
    data class Error(val throwable: Throwable) : DataResponse<Nothing>()
    data class Success<T>(val item: T) : DataResponse<T>()
}