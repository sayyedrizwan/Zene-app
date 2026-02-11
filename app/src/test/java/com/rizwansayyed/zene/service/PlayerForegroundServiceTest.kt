package com.rizwansayyed.zene.service

import android.content.Intent
import com.rizwansayyed.zene.data.model.MusicDataTypes
import com.rizwansayyed.zene.data.model.ZeneMusicData
import com.rizwansayyed.zene.datastore.DataStorageManager
import com.rizwansayyed.zene.service.player.ExoPlaybackService
import com.rizwansayyed.zene.service.player.PlayerForegroundService
import com.rizwansayyed.zene.service.player.utils.SleepTimerEnum
import io.mockk.Runs
import io.mockk.every
import io.mockk.just
import io.mockk.mockk
import io.mockk.mockkConstructor
import io.mockk.mockkStatic
import io.mockk.spyk
import io.mockk.verify
import junit.framework.TestCase.assertEquals
import junit.framework.TestCase.assertNotNull
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.advanceTimeBy
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.Robolectric
import org.robolectric.RobolectricTestRunner
import kotlin.time.Duration.Companion.minutes

@RunWith(RobolectricTestRunner::class)
class PlayerForegroundServiceTest {

    private lateinit var service: PlayerForegroundService

    @Before
    fun setup() {
        service = Robolectric.buildService(PlayerForegroundService::class.java).create().get()

        service.zeneAPI = mockk(relaxed = true)
    }

    @Test
    fun getPlayerS_returnsServiceInstance() {
        PlayerForegroundService.playerService = service
        val result = PlayerForegroundService.getPlayerS()
        assertNotNull(result)
    }

    @Test
    fun onStartCommand_stopAction_stopsService() {
        val spyService = spyk(service)
        val intent = Intent().apply {
            action = "ACTION_STOP_SERVICE"
        }
        spyService.onStartCommand(intent, 0, 0)

        verify {
            spyService.clearAll(1)
        }
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun songEnded_autoPause_enabled_pausesPlayer() = runTest {
        mockkConstructor(ExoPlaybackService::class)

        every {
            anyConstructed<ExoPlaybackService>().pause()
        } just Runs

        every {
            anyConstructed<ExoPlaybackService>().play()
        } just Runs

        service = Robolectric.buildService(
            PlayerForegroundService::class.java
        ).create().get()

        service.zeneAPI = mockk(relaxed = true)
    }

    @Test
    fun toNextSong_movesToNextSong() = runTest {

        val spyService = spyk(service)

        val s1 = ZeneMusicData("a", "1", "n", "", "", MusicDataTypes.SONGS.name)
        val s2 = ZeneMusicData("a", "2", "n", "", "", MusicDataTypes.SONGS.name)

        spyService.currentPlayingSong = s1
        spyService.songsLists = arrayOf(s1, s2)

        every {
            spyService.playSongs(false, any())
        }

        spyService.toNextSong()

        advanceUntilIdle()

        assertEquals(s1.id, spyService.currentPlayingSong?.id)
    }

    @Test
    fun sleepTimer_endOfTrack_doesNotPause() {

        val spyService = spyk(service)

        every { spyService.pause() } just Runs

        spyService.sleepTimer(SleepTimerEnum.END_OF_TRACK)

        verify(exactly = 0) {
            spyService.pause()
        }
    }

}
