package com.rizwansayyed.zene.ui.artists.artistviewmodel

import android.content.Context
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class ArtistsDataJsoup @Inject constructor(@ApplicationContext private val context: Context) {

    fun artistsBio() = flow {

        emit("")
    }
}