package com.rizwansayyed.zene.presenter.util

import android.app.Activity
import android.content.Intent
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
import kotlinx.coroutines.withContext
import java.net.URL
import java.text.NumberFormat
import java.util.Locale


object UiUtils {

    object GridSpan {
        const val TOTAL_ITEMS_GRID = 12
        const val TWO_ITEMS_GRID = TOTAL_ITEMS_GRID / 2
        const val THREE_ITEMS_GRID = TOTAL_ITEMS_GRID / 3
    }

    object ContentTypes {
        const val THE_ARTISTS = "songs_artists"
        const val RADIO_NAME = "Live Radio"
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

    fun String.toMoneyFormat(): String {
        val amount = this.toDoubleOrNull() ?: return "137,196"
        return NumberFormat.getNumberInstance().format(amount)
    }

    suspend fun isImagePresent(url: String): Boolean {
        return try {
            val connection = withContext(Dispatchers.IO) {
                URL(url).openConnection()
            }
            val contentType = connection.getHeaderField("Content-Type")
            contentType.startsWith("image/")
        } catch (e: Exception) {
            false
        }
    }

    fun String.convertMoney(): String {
        return try {
            val format = NumberFormat.getNumberInstance(
                Locale("en", "IN")
            )
            format.format(this.toLong())
        } catch (e: Exception) {
            this
        }
    }

    fun String.toCapitalFirst(): String {
        val words = this.split(",").map { it.trim() }
        val capitalizedWords = words.map {
            it.replaceFirstChar {
                if (it.isLowerCase()) it.titlecase(Locale.getDefault()) else it.toString()
            }
        }
        return capitalizedWords.joinToString(", ")
    }

    fun otherPermissionIntent() {
        if (Build.MANUFACTURER.equals("Xiaomi", true)) {
            val intent = Intent("miui.intent.action.APP_PERM_EDITOR")
            intent.setClassName(
                "com.miui.securitycenter",
                "com.miui.permcenter.permissions.PermissionsEditorActivity"
            )
            intent.putExtra("extra_pkgname", context.packageName)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
            context.startActivity(intent)

            context.resources.getString(R.string.enable_lock_screen_to_show).toast()
        }
    }

    fun formatSingleTimeToView(time: String): String {
        var timeMain = ""
        timeMain += if (time.substringBefore(":").trim().length == 1)
            "0${time.substringBefore(":").trim()}"
        else {
            time.substringBefore(":").trim()
        }
        timeMain += " : "
        timeMain += if (time.substringAfter(":").trim().length == 1)
            "0${time.substringAfter(":").trim()}"
        else {
            time.substringAfter(":").trim()
        }

        return timeMain
    }
}