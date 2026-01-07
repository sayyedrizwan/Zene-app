package com.rizwansayyed.zene.ui.main.ent.discoverview

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Whatshot
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.rizwansayyed.zene.ui.theme.MainColor
import com.rizwansayyed.zene.ui.view.ImageIcon
import com.rizwansayyed.zene.ui.view.TextViewSemiBold

@Composable
fun EntCelebStoriesView() {
//    val stories = listOf(
//        StoryItem(
//            name = "Taylor Swift", imageUrl = CelebrityImages.TAYLOR_SWIFT
//        ), StoryItem(
//            name = "Zendaya", imageUrl = CelebrityImages.ZENDAYA, isLive = true
//        ), StoryItem(
//            name = "Harry Styles", imageUrl = CelebrityImages.HARRY_STYLES
//        )
//    )
//
//    LazyRow(
//        modifier = Modifier
//            .fillMaxWidth()
//            .padding(16.dp),
//        horizontalArrangement = Arrangement.spacedBy(12.dp)
//    ) {
//
//        item { AddStory() }
//
//        items(stories) { story ->
//            StoryBubble(story)
//        }
//    }
}
//
//@Composable
//fun StoryBubble(story: StoryItem) {
//    Column(horizontalAlignment = Alignment.CenterHorizontally) {
//
//        Box {
//            NetworkImage(
//                url = story.imageUrl, modifier = Modifier
//                    .size(70.dp)
//                    .border(
//                        2.dp, Brush.linearGradient(
//                            listOf(Color.Magenta, Color.Yellow)
//                        ), CircleShape
//                    )
//                    .padding(3.dp)
//                    .clip(CircleShape)
//            )
//
//            if (story.isLive) {
//                Text(
//                    text = "LIVE",
//                    color = Color.White,
//                    fontSize = 10.sp,
//                    modifier = Modifier
//                        .align(Alignment.BottomCenter)
//                        .background(Color.Red, RoundedCornerShape(6.dp))
//                        .padding(horizontal = 6.dp, vertical = 2.dp)
//                )
//            }
//        }
//
//        Spacer(Modifier.height(6.dp))
//
//        Text(
//            story.name,
//            color = Color.White,
//            fontSize = 12.sp,
//            maxLines = 1,
//            overflow = TextOverflow.Ellipsis
//        )
//    }
//}
//
//@Composable
//fun AddStory() {
//    Column(horizontalAlignment = Alignment.CenterHorizontally) {
//        Box(
//            modifier = Modifier
//                .size(70.dp)
//                .border(1.dp, Color.Gray, CircleShape),
//            contentAlignment = Alignment.Center
//        ) {
//            Icon(
//                imageVector = Icons.Default.Add, contentDescription = null, tint = Color.White
//            )
//        }
//        Spacer(modifier = Modifier.height(6.dp))
//        Text(
//            text = "Your Story", color = Color.Gray, fontSize = 12.sp
//        )
//    }
//}
//
