package com.rizwansayyed.zene.ui.partycall

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
}