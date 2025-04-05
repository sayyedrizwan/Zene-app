package com.rizwansayyed.zene.ui.partycall

import android.webkit.WebView
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel

class PartyViewModel : ViewModel() {
    var isSpeaker by mutableStateOf(false)
    var isInPictureInPicture by mutableStateOf(false)

    var profilePhoto by mutableStateOf("")
    var email by mutableStateOf("")
    var name by mutableStateOf("")
    var type by mutableIntStateOf(0)
    var isCallPicked by mutableStateOf(false)
    var hideCallingView by mutableStateOf(false)
    var isVideoOn by mutableStateOf(false)
    var isMicOn by mutableStateOf(true)
    var randomCode by mutableStateOf("")
    var callWebViewMain by mutableStateOf<WebView?>(null)

    fun setInfo(profilePhoto: String, email: String, name: String, type: Int) {
        this.profilePhoto = profilePhoto
        this.email = email
        this.name = name
        this.type = type
    }

    fun setSpeaker() {
        this.isSpeaker = !this.isSpeaker
    }

    fun setPIP(v: Boolean) {
        this.isSpeaker = v
    }

    fun generateAlphabetCodeSet(length: Int = 12) {
        val chars = ('A'..'Z')
        randomCode = (1..length)
            .map { chars.random() }
            .joinToString("")
    }

    fun setCode(v: String) {
        randomCode = v
    }

    fun setCallWebView(v: WebView?) {
        callWebViewMain = v
    }

    fun setCallPicked() {
        isCallPicked = true
    }

    fun hideCallingView() {
        hideCallingView = true
    }

    fun setVideoOnOrOff(v: Boolean) {
        isVideoOn = v
    }

    fun setMicOnOrOff(v: Boolean) {
        isMicOn = v
    }
}