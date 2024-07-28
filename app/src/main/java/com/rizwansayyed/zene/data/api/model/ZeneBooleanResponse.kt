package com.rizwansayyed.zene.data.api.model

data class ZeneBooleanResponse(val status: String) {
    fun isSuccess(): Boolean {
        return status.lowercase() == "success"
    }
}