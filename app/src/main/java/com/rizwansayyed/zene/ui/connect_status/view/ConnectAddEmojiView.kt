package com.rizwansayyed.zene.ui.connect_status.view

import android.view.ViewGroup
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import androidx.emoji2.emojipicker.EmojiPickerView
import com.rizwansayyed.zene.R
import com.rizwansayyed.zene.ui.main.connect.profile.SettingsViewSimpleItems
import com.rizwansayyed.zene.viewmodel.ConnectViewModel

@Composable
fun ConnectEmojiView(viewModel: ConnectViewModel) {
    var showAlert by remember { mutableStateOf(false) }

    Spacer(Modifier.height(30.dp))
    SettingsViewSimpleItems(R.drawable.ic_tongue_wink, R.string.add_a_emoji) {
        showAlert = true
    }

    if (showAlert) Dialog(
        { showAlert = false }, DialogProperties(usePlatformDefaultWidth = false)
    ) {
        AndroidView(
            { ctx ->
                EmojiPickerView(ctx).apply {
                    emojiGridColumns = 9
                    layoutParams = ViewGroup.LayoutParams(
                        ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT
                    )

                    setOnEmojiPickedListener {
                        viewModel.addVibeEmoji(it.emoji.trim())
                    }
                }
            },
            Modifier.fillMaxSize()
        )
    }
}
