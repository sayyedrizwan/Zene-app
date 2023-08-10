package com.rizwansayyed.zene.presenter

sealed class APIResponse<T>(data: T) {
    class Success<T>(data: T) : APIResponse<T>(data)
    object Loading
    object Empty
    class Error(messages: String)
}