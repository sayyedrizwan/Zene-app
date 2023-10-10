package com.rizwansayyed.zene.domain

import com.rizwansayyed.zene.R
import com.rizwansayyed.zene.di.ApplicationModule.Companion.context

enum class HomeNavigation(val n: String, val img: Int) {
    HOME(context.resources.getString(R.string.home), R.drawable.ic_home_nav),
    FEED(context.resources.getString(R.string.feed), R.drawable.ic_cube_flow),
    SEARCH(context.resources.getString(R.string.search), R.drawable.ic_search),
    MY_MUSIC(context.resources.getString(R.string.my_music), R.drawable.ic_playlist_nav)
}