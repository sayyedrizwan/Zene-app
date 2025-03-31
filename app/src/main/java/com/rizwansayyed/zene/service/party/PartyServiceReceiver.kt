package com.rizwansayyed.zene.service.party

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.rizwansayyed.zene.service.notification.clearCallNotification

class PartyServiceReceiver : BroadcastReceiver() {
    override fun onReceive(c: Context?, i: Intent?) {
        c ?: return
        i ?: return
        val email = i.getStringExtra(Intent.EXTRA_EMAIL) ?: ""
        val profilePhoto = i.getStringExtra(Intent.EXTRA_USER) ?: ""
        val name = i.getStringExtra(Intent.EXTRA_PACKAGE_NAME) ?: ""

        clearCallNotification(email)
    }
}