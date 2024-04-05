package com.rizwansayyed.zene.service.fcm

import com.google.firebase.Firebase
import com.google.firebase.messaging.messaging
import com.rizwansayyed.zene.utils.Utils.FCMTopics.allUsers

class RegisterOnFCM {

    init {
        registerAllUsers()
    }

    private fun registerAllUsers() {
        Firebase.messaging.subscribeToTopic(allUsers).addOnCompleteListener {  }
    }
}