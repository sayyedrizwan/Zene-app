package com.rizwansayyed.zene.ui.login


object GlobalEmailEventProvider {
    private var listeners: EmailEventListener? = null

    fun registerListener(listener: EmailEventListener) {
        this.listeners = listener
    }

    fun unregisterListener() {
        this.listeners = null
    }

    fun sendEvent(data: String) {
        listeners?.onEvent(data)
    }
}

interface EmailEventListener {
    fun onEvent(data: String)
}