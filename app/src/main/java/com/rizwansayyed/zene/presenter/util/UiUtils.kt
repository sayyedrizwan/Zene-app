package com.rizwansayyed.zene.presenter.util

import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BlurMaskFilter
import android.graphics.Paint
import android.os.Build
import android.view.WindowManager
import android.widget.Toast
import androidx.compose.ui.graphics.Canvas
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

    fun generateCanvas(screenWidth: Int, screenHeight: Int, middleImage: Bitmap): Bitmap {
        // Create a blank bitmap with the screen size
        val canvasBitmap = Bitmap.createBitmap(screenWidth, screenHeight, Bitmap.Config.ARGB_8888)
        val canvas = android.graphics.Canvas(canvasBitmap)

        // Draw a blurred background on the canvas
        drawBlurredBackground(canvas, screenWidth, screenHeight)

        // Calculate the position for the middle image
        val middleImageX = (screenWidth - middleImage.width) / 2
        val middleImageY = (screenHeight - middleImage.height) / 2

        // Draw the middle image on the canvas
        canvas.drawBitmap(middleImage, middleImageX.toFloat(), middleImageY.toFloat(), Paint())
        return canvasBitmap
    }

    private fun drawBlurredBackground(canvas: android.graphics.Canvas, width: Int, height: Int) {
        // Create a paint object with a blur mask filter
        val paint = Paint()
        paint.maskFilter = BlurMaskFilter(25f, BlurMaskFilter.Blur.NORMAL)

        // Draw a rectangle with a blurred background
        canvas.drawRect(0f, 0f, width.toFloat(), height.toFloat(), paint)
    }
}