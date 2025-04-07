package com.rizwansayyed.zene.service.party

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.rizwansayyed.zene.data.implementation.ZeneAPIImplementation
import com.rizwansayyed.zene.service.notification.clearCallNotification
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.cancel
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class PartyServiceReceiver : BroadcastReceiver() {

    @Inject
    lateinit var zeneAPI: ZeneAPIImplementation

    override fun onReceive(c: Context?, i: Intent?) {
        c ?: return
        i ?: return
        val email = i.getStringExtra(Intent.EXTRA_EMAIL) ?: ""
        val profilePhoto = i.getStringExtra(Intent.EXTRA_USER) ?: ""
        val name = i.getStringExtra(Intent.EXTRA_PACKAGE_NAME) ?: ""

        CoroutineScope(Dispatchers.IO).launch {
            zeneAPI.declinePartyCall(email).catch { }.collectLatest { }

            if (isActive) cancel()
        }

        clearCallNotification(email)
    }
}