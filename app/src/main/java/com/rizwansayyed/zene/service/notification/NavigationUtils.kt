package com.rizwansayyed.zene.service.notification

import com.rizwansayyed.zene.data.model.ZeneMusicData


interface HomeNavigationListener {
    fun navigate(path: String)
    fun longPress(value: ZeneMusicData?)
}

object NavigationUtils {
    const val NAV_GO_BACK = "go_back"
    const val NAV_MAIN_PAGE = "main_page"
    const val NAV_SETTINGS_PAGE = "settings_page"
    const val NAV_PODCAST_PAGE = "podcast/"
    const val NAV_PLAYLIST_PAGE = "playlist/"
    const val NAV_MY_PLAYLIST_PAGE = "my-playlist/"
    const val NAV_ARTIST_PAGE = "artist/"
    const val NAV_MOVIES_PAGE = "movies-show/"
    const val NAV_CONNECT_PROFILE_PAGE = "connect_profile/"

    const val MY_PLAYLIST_ID = "zene_p_"
    const val LIKED_SONGS_ZENE_ID = "_liked_songs_on_zene"

    private var callback: HomeNavigationListener? = null

    fun setNavigationCallback(listener: HomeNavigationListener) {
        callback = listener
    }

    fun triggerHomeNav(path: String) {
        callback?.navigate(replaceSecondSlash(path.replace("//", "/")))
    }

    fun triggerInfoSheet(v: ZeneMusicData?) {
        callback?.longPress(v)
    }

   private fun replaceSecondSlash(input: String): String {
        var slashCount = 0
        return input.map { char ->
            if (char == '/') {
                slashCount++
                if (slashCount == 2) '_' else char
            } else {
                char
            }
        }.joinToString("")
    }
}