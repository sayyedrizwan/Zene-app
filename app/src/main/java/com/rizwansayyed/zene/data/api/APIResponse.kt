package com.rizwansayyed.zene.data.api

sealed class APIResponse<out T> {
    data class Success<out T>(val data: T) : APIResponse<T>()
    data class Error(val error: Throwable) : APIResponse<Nothing>()
    data object Loading : APIResponse<Nothing>()
    data object Empty : APIResponse<Nothing>()
}