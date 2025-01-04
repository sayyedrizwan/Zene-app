package com.rizwansayyed.zene.data.implementation

import com.google.firebase.messaging.FirebaseMessaging
import com.rizwansayyed.zene.data.ZeneAPIService
import com.rizwansayyed.zene.data.model.UserInfoResponse
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class ZeneAPIImplementation @Inject constructor(
    private val zeneAPI: ZeneAPIService
) : ZeneAPIInterface {

    override suspend fun updateUser(email: String, name: String, photo: String) = flow {
        val fcm = FirebaseMessaging.getInstance().token.await() ?: ""
        val info = UserInfoResponse(email, name, photo, fcm, "", "", "")
        emit(zeneAPI.updateUser(info))
    }

}