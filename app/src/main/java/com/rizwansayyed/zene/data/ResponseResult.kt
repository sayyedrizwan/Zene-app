package com.rizwansayyed.zene.data

sealed class ResponseResult<out T> {
    data class Success<out T>(val data: T) : ResponseResult<T>()
    data class Error(val throwable: Throwable? = null) : ResponseResult<Nothing>()
    data object Loading : ResponseResult<Nothing>()
    data object Empty : ResponseResult<Nothing>()
}