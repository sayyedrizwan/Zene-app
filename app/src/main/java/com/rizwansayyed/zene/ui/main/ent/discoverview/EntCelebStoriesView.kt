package com.rizwansayyed.zene.ui.main.ent.discoverview

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.bumptech.glide.integration.compose.GlideImage
import com.rizwansayyed.zene.ui.main.ent.utils.dynamicNameColor
import com.rizwansayyed.zene.ui.view.TextViewSemiBold
import com.rizwansayyed.zene.viewmodel.EntertainmentViewModel

data class StoryItem(val name: String, val imageUrl: String)

@Composable
fun EntCelebStoriesView(viewModel: EntertainmentViewModel) {
    val stories = listOf(
        StoryItem(
            name = "Taylor Swift",
            imageUrl = "https://t3.gstatic.com/licensed-image?q=tbn:ANd9GcTLlpqNaFEPKlR2nrG7dqgnpooimf6ug2tG5cIEaOcwcq-k61KfyFBKAq1ob1tSrbNX"
        ), StoryItem(
            name = "Zendaya",
            imageUrl = "https://image.tmdb.org/t/p/w500/3WdOloHpjtjL96uVOhFRRCcYSwq.jpg"
        ), StoryItem(
            name = "Harry Styles",
            imageUrl = "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcT6LxhXHP9Gjt9K7_w83wmgO8SQNYodMAuookWPtFCYZ3Q5XhP-7Oo0zLJGkc5J6VUZiebE&s=10"
        ), StoryItem(
            name = "Taylor Swift",
            imageUrl = "https://t3.gstatic.com/licensed-image?q=tbn:ANd9GcTLlpqNaFEPKlR2nrG7dqgnpooimf6ug2tG5cIEaOcwcq-k61KfyFBKAq1ob1tSrbNX"
        ), StoryItem(
            name = "Zendaya",
            imageUrl = "https://image.tmdb.org/t/p/w500/3WdOloHpjtjL96uVOhFRRCcYSwq.jpg"
        ), StoryItem(
            name = "Harry Styles",
            imageUrl = "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcT6LxhXHP9Gjt9K7_w83wmgO8SQNYodMAuookWPtFCYZ3Q5XhP-7Oo0zLJGkc5J6VUZiebE&s=10"
        ), StoryItem(
            name = "Taylor Swift",
            imageUrl = "https://t3.gstatic.com/licensed-image?q=tbn:ANd9GcTLlpqNaFEPKlR2nrG7dqgnpooimf6ug2tG5cIEaOcwcq-k61KfyFBKAq1ob1tSrbNX"
        ), StoryItem(
            name = "Zendaya",
            imageUrl = "https://image.tmdb.org/t/p/w500/3WdOloHpjtjL96uVOhFRRCcYSwq.jpg"
        ), StoryItem(
            name = "Harry Styles",
            imageUrl = "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcT6LxhXHP9Gjt9K7_w83wmgO8SQNYodMAuookWPtFCYZ3Q5XhP-7Oo0zLJGkc5J6VUZiebE&s=10"
        ), StoryItem(
            name = "Taylor Swift",
            imageUrl = "https://t3.gstatic.com/licensed-image?q=tbn:ANd9GcTLlpqNaFEPKlR2nrG7dqgnpooimf6ug2tG5cIEaOcwcq-k61KfyFBKAq1ob1tSrbNX"
        ), StoryItem(
            name = "Zendaya",
            imageUrl = "https://image.tmdb.org/t/p/w500/3WdOloHpjtjL96uVOhFRRCcYSwq.jpg"
        ), StoryItem(
            name = "Harry Styles",
            imageUrl = "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcT6LxhXHP9Gjt9K7_w83wmgO8SQNYodMAuookWPtFCYZ3Q5XhP-7Oo0zLJGkc5J6VUZiebE&s=10"
        )
    )

    LazyRow(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 15.dp),
        horizontalArrangement = Arrangement.spacedBy(12.dp),
        contentPadding = PaddingValues(horizontal = 15.dp)
    ) {
        items(stories, key = { it.name } ) { story ->
            StoryBubble(story)
        }
    }
}

@OptIn(ExperimentalGlideComposeApi::class)
@Composable
fun StoryBubble(story: StoryItem) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        GlideImage(
            model = story.imageUrl,
            contentDescription = story.name,
            modifier = Modifier
                .size(90.dp)
                .clip(CircleShape)
                .border(3.dp, dynamicNameColor(story.name), CircleShape),
            contentScale = ContentScale.Crop
        )

        Spacer(Modifier.height(5.dp))
        TextViewSemiBold(story.name, 15)
    }
}