package com.rizwansayyed.zene.datastore

import android.content.Context
import androidx.test.core.app.ApplicationProvider
import com.google.common.truth.Truth.assertThat
import com.rizwansayyed.zene.data.model.UserInfoResponse
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner


@OptIn(ExperimentalCoroutinesApi::class)
@RunWith(RobolectricTestRunner::class)
class DataStorageManagerImportantTest {

    private lateinit var context: Context

    @Before
    fun setup() {
        context = ApplicationProvider.getApplicationContext()
    }

    @Test
    fun userInfo_saveAndRead_success() = runTest {
        val fakeUser = UserInfoResponse(
            email = "rizwan@gmail.com",
            name = "Rizwan User",
            photo = "photo_url",
            username = "rizuser",
            emailSubscribe = true,
            phoneNumber = "9999999999",
            status = "active",
            authToken = "token123",
            isError = false,
            logout = false
        )

        DataStorageManager.userInfo = flowOf(fakeUser)

        val stored = DataStorageManager.userInfo.first()

        assertThat(stored?.email).isEqualTo("rizwan@gmail.com")
        assertThat(stored?.name).isEqualTo("Rizwan User")
        assertThat(stored?.username).isEqualTo("rizuser")
        assertThat(stored?.isLoggedIn()).isTrue()
    }

    @Test
    fun isPremium_defaultFalse_thenTrueAfterSave() = runTest {
        val default = DataStorageManager.isPremiumDB.first()

        assertThat(default).isFalse()

        DataStorageManager.isPremiumDB = flowOf(true)

        val updated = DataStorageManager.isPremiumDB.first()

        assertThat(updated).isTrue()
    }

    @Test
    fun searchHistory_saveAndRead_success() = runTest {
        val history = arrayOf("song1", "song2", "song3")

        DataStorageManager.searchHistoryDB = flowOf(history)

        val stored = DataStorageManager.searchHistoryDB.first()

        assertThat(stored?.size).isEqualTo(3)
        assertThat(stored?.get(0)).isEqualTo("song1")
    }

    @Test
    fun pauseHistorySettings_saveAndRead() = runTest {
        DataStorageManager.pauseHistorySettings = flowOf(true)
        val value = DataStorageManager.pauseHistorySettings.first()
        assertThat(value).isTrue()
    }

    @Test
    fun lockChatSettings_saveAndRead() = runTest {
        val userId = "user_123"

        DataStorageManager.lockChatSettings(id = userId, v = true).firstOrNull()

        val value = DataStorageManager.lockChatSettings(userId).first()

        assertThat(value).isTrue()
    }
}
