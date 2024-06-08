package com.rizwansayyed.zene.utils

import android.os.Bundle
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.analytics.logEvent
import com.rizwansayyed.zene.di.ApplicationModule.Companion.context
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.cancel
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch

object FirebaseEvents {

    enum class FirebaseEvent {
        OPEN_APP_ADS,
        OPEN_APP,
        OPEN_HOME,
        OPEN_SEARCH,
        OPEN_SETTINGS,
        OPEN_FEED,
        OPEN_MY_MUSIC,
        OPEN_A_ALBUM,
        OPEN_A_ARTISTS,
        OPEN_IMPORT_SPOTIFY,
        OPEN_IMPORT_YOUTUBE_MUSIC,
        OPEN_IMPORT_APPLE_MUSIC,
        STAND_BY_MODE,
        MUSIC_ON_LOCK_SCREEN,
        ALBUM_URL_SHARE,
        ALBUM_SAVE_SHARE,
        DOWNLOAD_OFFLINE_ALBUMS_SONG,
        PLAY_ALL_ALBUMS_SONG,
        OPEN_ARTISTS_EVENTS,
        TAP_ARTISTS_IMAGE_TO_SET_AS_WALLPAPER,
        PIN_ARTISTS,
        UNPINNED_ARTISTS,
        OPEN_ARTISTS_NEWS,
        OPEN_ARTISTS_WEBSITE,
        OPEN_ARTISTS_FACEBOOK,
        OPEN_ARTISTS_TWITTER,
        OPEN_ARTISTS_INSTAGRAM,
        OPEN_ARTISTS_YOUTUBE,
        OPEN_ARTISTS_SNAPCHAT,
        OPEN_ARTISTS_TIKTOK,
        OPEN_ARTISTS_STORE,
        PLAY_SONGS_LIST,
        PLAY_RADIO,
        OPENED_WALLPAPER_SET,
        SET_CUSTOM_IMAGE_AS_WALLPAPER_HOME,
        SET_CUSTOM_IMAGE_AS_WALLPAPER_LOCK,
        SET_IMAGE_AS_WALLPAPER_HOME,
        SET_IMAGE_AS_WALLPAPER_LOCK,
        ADDING_MORE_IMAGE_ON_WALLPAPER,
        SONG_MENU_SHEET,
        RADIO_MENU_SHEET,
        SONG_CHANGE_FROM_MUSIC_PLAYER,
        OPEN_MUSIC_PLAYER,
        OPEN_RINGTONE_VIEW,
        OPEN_RINGTONE_EDIT_VIEW,
        SETTING_MUSIC_AS_RINGTONE,
        LOADED_AD,
        CLICK_AD,
        PLAYING_SONG_ALARM,
        REBOOT_DEVICE,
        RECEIVED_FCM_NOTIFICATION,
        STARTED_PLAYER_SERVICE,
        DOWNLOADING_SONG_URL,
        CHANGE_SONG_TRACK_ITEM,
        OPEN_INSTAGRAM_ACCOUNT,
        SHARE_APP_VIA_DIALOG,
        RATING_CLICK_FEEDBACK,
        RATING_CLICK_APP_STORE,
        PARTY_SERVICE_RUNNING,
        PARTY_SONG_CHANGE,
        PARTY_SONG_PLAY,
        PARTY_SONG_PAUSE,
    }


    fun registerEvent(t: FirebaseEvent) = CoroutineScope(Dispatchers.IO).launch {
        val firebaseAnalytics = FirebaseAnalytics.getInstance(context)

        firebaseAnalytics.logEvent(t.name.lowercase()) {}
        if (isActive) cancel()
    }

}