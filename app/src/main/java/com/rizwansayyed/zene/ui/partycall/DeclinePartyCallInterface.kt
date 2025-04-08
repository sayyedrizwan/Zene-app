package com.rizwansayyed.zene.ui.partycall

import com.rizwansayyed.zene.data.model.ZeneMusicData

interface DeclinePartyCallInterface {
    fun declineCall(email: String?)
    fun changeUpdate(v: ZeneMusicData)
}