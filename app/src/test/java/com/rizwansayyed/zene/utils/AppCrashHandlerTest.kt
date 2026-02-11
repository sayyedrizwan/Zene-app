package com.rizwansayyed.zene.utils

import android.app.Application
import android.content.Intent
import androidx.test.core.app.ApplicationProvider
import com.google.firebase.crashlytics.FirebaseCrashlytics
import com.rizwansayyed.zene.di.utils.AppCrashHandler
import io.mockk.every
import io.mockk.mockk
import io.mockk.mockkStatic
import io.mockk.spyk
import io.mockk.unmockkAll
import io.mockk.verify
import junit.framework.TestCase.assertEquals
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner

@RunWith(RobolectricTestRunner::class)
class AppCrashHandlerTest {

    private lateinit var application: Application
    private lateinit var crashHandler: AppCrashHandler

    @Before
    fun setup() {
        application = spyk(ApplicationProvider.getApplicationContext())
        crashHandler = AppCrashHandler(application)

        mockkStatic(FirebaseCrashlytics::class)
        every { FirebaseCrashlytics.getInstance() } returns mockk(relaxed = true)
    }

    @After
    fun tearDown() {
        unmockkAll()
    }

    @Test
    fun init_setsDefaultExceptionHandler() {
        crashHandler.init()
        val handler = Thread.getDefaultUncaughtExceptionHandler()
        assertEquals(crashHandler, handler)
    }

    @Test
    fun uncaughtException_belowThreshold_restartsApp() {

        val throwable = RuntimeException("Test crash")

        crashHandler.uncaughtException(
            Thread.currentThread(), throwable
        )

        verify {
            application.startActivity(
                withArg {
                    assert(it.flags and Intent.FLAG_ACTIVITY_NEW_TASK != 0)
                })
        }
    }

    @Test
    fun uncaughtException_recordsCrashlytics() {
        val crashlytics = mockk<FirebaseCrashlytics>(relaxed = true)
        every { FirebaseCrashlytics.getInstance() } returns crashlytics

        val throwable = RuntimeException("Crash")

        crashHandler.uncaughtException(
            Thread.currentThread(), throwable
        )

        verify {
            crashlytics.recordException(throwable)
        }
    }

    @Test
    fun uncaughtException_aboveThreshold_callsDefaultHandler() {
        val defaultHandler = mockk<Thread.UncaughtExceptionHandler>(relaxed = true)

        Thread.setDefaultUncaughtExceptionHandler(defaultHandler)

        val handler = AppCrashHandler(application)
        repeat(4) {
            handler.uncaughtException(Thread.currentThread(), RuntimeException("Crash $it"))
        }

        verify {
            defaultHandler.uncaughtException(any(), any())
        }
    }
}