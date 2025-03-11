package com.rizwansayyed.zene.utils


interface HomeNavigationListener {
    fun navigate(path: String)
}

object NavigationUtils {
    const val NAV_GO_BACK = "go_back"
    const val NAV_MAIN_PAGE = "main_page"
    const val NAV_LIKED_PLAYLIST = "nav_liked_playlist"
    const val NAV_SETTINGS_PAGE = "settings_page"
    const val NAV_PODCAST_PAGE = "podcast/"
    const val NAV_PLAYLIST_PAGE = "playlist/"
    const val NAV_MY_PLAYLIST_PAGE = "my-playlist/"
    const val NAV_ARTIST_PAGE = "artist/"
    const val NAV_CONNECT_PROFILE_PAGE = "connect_profile/"

    private var callback: HomeNavigationListener? = null

    fun setNavigationCallback(listener: HomeNavigationListener) {
        callback = listener
    }

    fun triggerHomeNav(path: String) {
        callback?.navigate(path)
    }
}