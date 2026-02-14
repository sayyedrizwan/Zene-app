package com.rizwansayyed.zene.ui.main.ent.discoverview

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.bumptech.glide.integration.compose.GlideImage
import com.rizwansayyed.zene.data.ResponseResult
import com.rizwansayyed.zene.data.model.StoriesListsResponseItems
import com.rizwansayyed.zene.data.model.ZeneMusicData
import com.rizwansayyed.zene.ui.main.ent.utils.dynamicNameColor
import com.rizwansayyed.zene.ui.view.TextViewSemiBold
import com.rizwansayyed.zene.viewmodel.EntertainmentViewModel

@Composable
fun EntCelebStoriesView(viewModel: EntertainmentViewModel) {
    var dialogInfo by remember { mutableStateOf<StoriesListsResponseItems?>(null) }

    when (val v = viewModel.storiesLists) {
        ResponseResult.Empty -> {}
        is ResponseResult.Error -> {}
        ResponseResult.Loading -> {}
        is ResponseResult.Success -> LazyRow(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 15.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            contentPadding = PaddingValues(horizontal = 15.dp)
        ) {
            items(v.data) { story ->
                StoryBubble(story.info) {
                    dialogInfo = story
                }
            }
        }
    }

    if (dialogInfo != null) StoryDialogInfo(dialogInfo) {
        dialogInfo = null
    }
}

@OptIn(ExperimentalGlideComposeApi::class)
@Composable
fun StoryBubble(story: ZeneMusicData?, click: () -> Unit) {
    if (story?.name != null) Column(
        Modifier
            .width(95.dp)
            .clickable {
                click()
            }, horizontalAlignment = Alignment.CenterHorizontally
    ) {
        GlideImage(
            model = story.thumbnail,
            contentDescription = story.name,
            modifier = Modifier
                .size(90.dp)
                .clip(CircleShape)
                .border(3.dp, dynamicNameColor(story.name), CircleShape),
            contentScale = ContentScale.Crop
        )

        Spacer(Modifier.height(5.dp))
        TextViewSemiBold(story.name, 15, line = 1)
    }
}

@Composable
fun StoryDialogInfo(info: StoriesListsResponseItems?, dismiss: () -> Unit) {
    Dialog(
        dismiss, DialogProperties(usePlatformDefaultWidth = false)
    ) {
        LazyColumn(
            Modifier
                .fillMaxSize()
                .background(Color.Black)
        ) {
            item {

            }
            items(info?.news ?: emptyList()) {
                EntBuzzNewsViewItem(it)
            }
        }
    }
}