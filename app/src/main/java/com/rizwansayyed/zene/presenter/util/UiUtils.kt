package com.rizwansayyed.zene.presenter.util

import android.app.Activity
import android.os.Build
import android.view.WindowManager
import android.widget.Toast
import com.rizwansayyed.zene.R
import com.rizwansayyed.zene.di.ApplicationModule.Companion.context
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.cancel
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import java.text.NumberFormat
import kotlin.random.Random


object UiUtils {

    object ContentTypes {
        const val THE_ARTISTS = "songs_artists"
    }

    private const val FLAG_NO_LIMIT = WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS

    fun Activity.transparentStatusAndNavigation() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R)
            window.setDecorFitsSystemWindows(false)
        else
            window.setFlags(FLAG_NO_LIMIT, FLAG_NO_LIMIT)
    }

    fun Any.toast() = CoroutineScope(Dispatchers.Main).launch {
        Toast.makeText(context, this@toast.toString(), Toast.LENGTH_LONG).show()
        if (isActive) cancel()
    }

    fun generateRadioName(): String {
        return "${context.resources.getString(R.string.radio)} ${(4..19).random()}"
    }

    fun String.toMoneyFormat(): String {
        val amount = this.toDoubleOrNull() ?: return "137,196"
        return NumberFormat.getNumberInstance().format(amount)
    }

}