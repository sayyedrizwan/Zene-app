package com.rizwansayyed.zene.presenter.util

import android.app.Activity
import android.graphics.Color
import android.os.Build
import android.view.View
import android.view.Window
import android.view.WindowManager
import androidx.compose.foundation.background
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier


object UiUtils {

    private const val FLAG_NO_LIMIT = WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS

    fun Activity.transparentStatusAndNavigation() {
        window.statusBarColor = resources.getColor(android.R.color.transparent, null)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R)
            window.setDecorFitsSystemWindows(false)
        else
            window.setFlags(FLAG_NO_LIMIT, FLAG_NO_LIMIT)

    }
}