package com.rizwansayyed.zene.ui.viewmodel

sealed class APIResponse<T>(data: T) {
    class Success<T>(data: T) : APIResponse<T>(data)
    object Loading
    object Empty
    class Error(messages: String)
}