package com.rizwansayyed.zene.utils

import android.app.Application
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.test.core.app.ApplicationProvider
import com.google.common.truth.Truth.assertThat
import com.rizwansayyed.zene.utils.MainUtils.removeSpecialChars
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.Shadows.shadowOf
import org.robolectric.util.ReflectionHelpers

@RunWith(RobolectricTestRunner::class)
class MainUtilsTest {
    private lateinit var context: Context
    private lateinit var application: Application

    @Before
    fun setup() {
        context = ApplicationProvider.getApplicationContext()
        application = context as Application
    }

    @Test
    fun formatDurationsForVideo_returnsMMSS() {
        val result = MainUtils.formatDurationsForVideo(125f)
        assertThat(result).isEqualTo("02:05")
    }

    @Test
    fun formatDurationsForVideo_returnsHHMMSS() {
        val result = MainUtils.formatDurationsForVideo(3665f)
        assertThat(result).isEqualTo("01:01:05")
    }

    @Test
    fun formatMSDurationsForVideo_returnsCorrectTime() {
        val result = MainUtils.formatMSDurationsForVideo(125000f)
        assertThat(result).isEqualTo("02:05")
    }

    @Test
    fun removeSpecialChars_removesSymbolsAndSpaces() {
        val input = "He@llo W#orld!! 123"
        val result = input.removeSpecialChars()

        assertThat(result).isEqualTo("HelloWorld123")
    }

    @Test
    fun copyTextToClipboard_setsClipboardValue() {
        MainUtils.copyTextToClipboard("Clipboard Test")
        val clipboard =
            context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
        val text =
            clipboard.primaryClip?.getItemAt(0)?.text.toString()

        assertThat(text).isEqualTo("Clipboard Test")
    }

    @Test
    fun openFeedbackMail_startsEmailIntent() {
        MainUtils.openFeedbackMail()
        val shadowApp = shadowOf(application)
        val intent = shadowApp.nextStartedActivity

        assertThat(intent).isNotNull()
        assertThat(intent.action)
            .isEqualTo(Intent.ACTION_SENDTO)

        assertThat(intent.dataString)
            .contains("mailto:")
    }

    @Test
    fun openAppOnPlayStore_startsPlayStoreIntent() {
        MainUtils.openAppOnPlayStore()
        val shadowApp = shadowOf(application)
        val intent = shadowApp.nextStartedActivity

        assertThat(intent).isNotNull()
        assertThat(intent.action)
            .isEqualTo(Intent.ACTION_VIEW)
    }

    @Test
    fun getDeviceInfo_returnsManufacturerModelVersion() {
        ReflectionHelpers.setStaticField(
            Build::class.java, "MANUFACTURER", "Samsung"
        )

        ReflectionHelpers.setStaticField(
            Build::class.java, "MODEL", "S23"
        )

        ReflectionHelpers.setStaticField(
            Build.VERSION::class.java, "RELEASE", "14"
        )

        val result = MainUtils.getDeviceInfo()

        assertThat(result).contains("Samsung")
        assertThat(result).contains("S23")
        assertThat(result).contains("14")
    }

    @Test
    fun timeDifferenceInMinutes_returnsCorrectValue() {
        val past = System.currentTimeMillis() - (5 * 60 * 1000)
        val diff = MainUtils.timeDifferenceInMinutes(past)

        assertThat(diff).isAtLeast(5)
    }

    @Test
    fun timeDifferenceInSeconds_returnsCorrectValue() {
        val past = System.currentTimeMillis() - (10 * 1000)
        val diff = MainUtils.timeDifferenceInSeconds(past)

        assertThat(diff).isAtLeast(10)
    }
}
