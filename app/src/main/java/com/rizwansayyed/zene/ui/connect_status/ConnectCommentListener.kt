package com.rizwansayyed.zene.ui.connect_status

interface ConnectCommentListener {
    fun addedNewComment()
}

object ConnectCommentListenerManager {
    private var callback: ConnectCommentListener? = null

    fun setCallback(listener: ConnectCommentListener) {
        callback = listener
    }

    fun triggerEvent() {
        callback?.addedNewComment()
    }
}