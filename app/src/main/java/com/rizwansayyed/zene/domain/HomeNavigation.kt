package com.rizwansayyed.zene.domain

import com.rizwansayyed.zene.R
import com.rizwansayyed.zene.di.ApplicationModule.Companion.context

enum class HomeNavigation(val n: String, val img: Int, val doShow: Boolean) {
    HOME(context.resources.getString(R.string.home), R.drawable.ic_home_nav, true),
    ALL_RADIO(context.resources.getString(R.string.radio), R.drawable.ic_home_nav, false),
    SETTINGS(context.resources.getString(R.string.settings), R.drawable.ic_setting, false),
    FEED(context.resources.getString(R.string.feed), R.drawable.ic_cube_flow, true),
    SEARCH(context.resources.getString(R.string.search), R.drawable.ic_search, true),
    MY_MUSIC(context.resources.getString(R.string.my_music), R.drawable.ic_playlist_nav, true)
}