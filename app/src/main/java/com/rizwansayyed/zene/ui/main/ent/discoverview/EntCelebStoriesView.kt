package com.rizwansayyed.zene.ui.main.ent.discoverview

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.bumptech.glide.integration.compose.GlideImage
import com.rizwansayyed.zene.R
import com.rizwansayyed.zene.data.ResponseResult
import com.rizwansayyed.zene.data.model.StoriesListsResponseItems
import com.rizwansayyed.zene.ui.main.ent.utils.dynamicNameColor
import com.rizwansayyed.zene.ui.view.ImageIcon
import com.rizwansayyed.zene.ui.view.TextViewBold
import com.rizwansayyed.zene.ui.view.TextViewLight
import com.rizwansayyed.zene.ui.view.TextViewSemiBold
import com.rizwansayyed.zene.utils.share.MediaContentUtils.startMedia
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
                StoryBubble(story) {
                    dialogInfo = story
                }
            }
        }
    }

    if (dialogInfo != null) StoryDialogInfo(dialogInfo, viewModel) {
        dialogInfo = null
    }
}

@OptIn(ExperimentalGlideComposeApi::class)
@Composable
fun StoryBubble(story: StoriesListsResponseItems, click: () -> Unit) {
    if (story.info?.name != null) Column(
        Modifier
            .width(95.dp)
            .clickable {
                click()
            }, horizontalAlignment = Alignment.CenterHorizontally
    ) {
        GlideImage(
            model = story.info.thumbnail,
            contentDescription = story.info.name,
            modifier = Modifier
                .size(90.dp)
                .clip(CircleShape)
                .border(
                    3.dp, if (story.isSeen == true) Brush.linearGradient(listOf(Color.Gray, Color.DarkGray))
                    else dynamicNameColor(story.info.name), CircleShape
                ),
            contentScale = ContentScale.Crop
        )

        Spacer(Modifier.height(5.dp))
        TextViewSemiBold(story.info.name, 15, line = 1)
    }
}

@Composable
fun StoryDialogInfo(
    info: StoriesListsResponseItems?, viewModel: EntertainmentViewModel, dismiss: () -> Unit
) {
    Dialog(dismiss, DialogProperties(usePlatformDefaultWidth = false)) {
        LazyColumn(
            Modifier
                .fillMaxSize()
                .background(Color.Black)
        ) {
            item {
                StoriesDialogHeader(info, dismiss)
            }

            item {
                Spacer(Modifier.height(30.dp))
            }

            items(info?.news ?: emptyList()) {
                EntBuzzNewsViewItem(it)
            }
        }

        LaunchedEffect(Unit) {
            info?.let { viewModel.viewedArtistsStories(it) }
        }
    }
}

@OptIn(ExperimentalGlideComposeApi::class)
@Composable
fun StoriesDialogHeader(info: StoriesListsResponseItems?, dismiss: () -> Unit) {
    Row(
        Modifier
            .fillMaxWidth()
            .padding(horizontal = 10.dp, vertical = 19.dp)
            .clickable {
                dismiss()
                startMedia(info?.info)
            }, verticalAlignment = Alignment.CenterVertically
    ) {
        GlideImage(
            info?.info?.thumbnail,
            info?.info?.name,
            Modifier
                .size(100.dp)
                .clip(RoundedCornerShape(12.dp)),
            contentScale = ContentScale.Crop
        )

        Spacer(Modifier.width(10.dp))

        Column(Modifier.weight(1f), Arrangement.Center) {
            TextViewBold(info?.info?.name.orEmpty(), 22)
            Spacer(Modifier.height(7.dp))
            Row(
                Modifier
                    .border(BorderStroke(1.dp, Color.White), RoundedCornerShape(12.dp))
                    .padding(horizontal = 13.dp, vertical = 5.dp),
                Arrangement.Center,
                Alignment.CenterVertically
            ) {
                TextViewLight(stringResource(R.string.view), 16)
                Spacer(Modifier.width(4.dp))
                Box(Modifier.rotate(-90f)) {
                    ImageIcon(R.drawable.ic_arrow_down, 17)
                }
            }
        }
    }
}