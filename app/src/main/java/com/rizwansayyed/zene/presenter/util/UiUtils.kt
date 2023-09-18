package com.rizwansayyed.zene.presenter.util

import android.app.Activity
import android.graphics.Color
import android.os.Build
import android.view.View
import android.view.WindowManager

object UiUtils {

    fun Activity.transparentStatusAndNavigation(
        systemUiScrim: Int = Color.parseColor("#40000000") // 25% black
    ) {
        var systemUiVisibility = 0
        var navigationBarColor = systemUiScrim
        val winParams = window.attributes


        systemUiVisibility = systemUiVisibility or View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            systemUiVisibility = systemUiVisibility or View.SYSTEM_UI_FLAG_LIGHT_NAVIGATION_BAR
            navigationBarColor = Color.TRANSPARENT
        }
        systemUiVisibility = systemUiVisibility or
                View.SYSTEM_UI_FLAG_LAYOUT_STABLE or
                View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN or
                View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
        window.decorView.systemUiVisibility = systemUiVisibility
        winParams.flags = winParams.flags and
                (WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS or
                        WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION).inv()
        window.statusBarColor = Color.TRANSPARENT
        window.navigationBarColor = navigationBarColor

        window.attributes = winParams
    }
}