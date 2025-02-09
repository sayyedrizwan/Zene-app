package com.rizwansayyed.zene.ui.connect_status

interface ConnectStatusListener {
    fun addedNewStatus()
}

object ConnectStatusCallbackManager {
    private var callback: ConnectStatusListener? = null

    fun setCallback(listener: ConnectStatusListener) {
        callback = listener
    }

    fun triggerEvent() {
        callback?.addedNewStatus()
    }
}