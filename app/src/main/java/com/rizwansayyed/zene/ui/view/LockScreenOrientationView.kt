package com.rizwansayyed.zene.ui.view

import android.app.Activity
import android.content.Context
import android.content.ContextWrapper
import android.os.Build
import android.view.WindowInsets
import android.view.WindowInsetsController
import androidx.activity.compose.LocalActivity
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.core.view.WindowCompat


@Composable
fun LockScreenOrientation(orientation: Int, doFullScreen: Boolean = true) {
    val context = LocalActivity.current
    DisposableEffect(orientation) {
        val activity = context?.findActivity() ?: return@DisposableEffect onDispose {}
        val originalOrientation = activity.requestedOrientation
        activity.requestedOrientation = orientation
        if (doFullScreen) context.setFullScreen(true)
        onDispose {
            activity.requestedOrientation = originalOrientation
            if (doFullScreen) context.setFullScreen(false)
        }
    }
}

fun Activity.setFullScreen(enabled: Boolean) {
    WindowCompat.setDecorFitsSystemWindows(window, !enabled)
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
        window.insetsController?.let { controller ->
            if (enabled) {
                controller.hide(WindowInsets.Type.systemBars())
                controller.systemBarsBehavior =
                    WindowInsetsController.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE
            } else {
                controller.show(WindowInsets.Type.systemBars())
            }
        }
    }
}

private fun Context.findActivity(): Activity? = when (this) {
    is Activity -> this
    is ContextWrapper -> baseContext.findActivity()
    else -> null
}