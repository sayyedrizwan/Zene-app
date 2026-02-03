package com.rizwansayyed.zene.ui.main.ent.nav

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.bumptech.glide.integration.compose.GlideImage
import com.rizwansayyed.zene.R
import com.rizwansayyed.zene.data.ResponseResult
import com.rizwansayyed.zene.data.model.LoveBuzzFullInfoResponse
import com.rizwansayyed.zene.ui.main.ent.EntertainmentViewModel
import com.rizwansayyed.zene.ui.theme.MainColor
import com.rizwansayyed.zene.ui.view.ButtonArrowBack
import com.rizwansayyed.zene.ui.view.TextViewBold
import com.rizwansayyed.zene.ui.view.TextViewNormal
import com.rizwansayyed.zene.ui.view.TextViewSemiBold

@OptIn(ExperimentalGlideComposeApi::class)
@Composable
fun LoveBuzzNavView(id: String) {
    val viewModel: EntertainmentViewModel = hiltViewModel()
    Box(
        Modifier
            .background(Color.Black)
            .fillMaxSize()
    ) {
        when (val v = viewModel.loveBuzzFullInfo) {
            ResponseResult.Empty -> {}
            is ResponseResult.Error -> {}
            ResponseResult.Loading -> {}
            is ResponseResult.Success -> LazyColumn(modifier = Modifier.fillMaxSize()) {
                item {
                    GlideImage(
                        v.data.mainImage,
                        v.data.title,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(350.dp),
                        contentScale = ContentScale.Crop
                    )
                }
                item { Spacer(Modifier.height(20.dp)) }
                item { LoveBuzzHeader(v.data) }
                item {
                    Column(Modifier.padding(horizontal = 8.dp, vertical = 5.dp)) {
                        TextViewNormal(v.data.summary.orEmpty(), 15)
                    }
                }
                item { Spacer(Modifier.height(30.dp)) }
                item { LoveBuzzStats(v.data) }
                item { Spacer(Modifier.height(35.dp)) }

                itemsIndexed(v.data.timeline ?: emptyList()) { index, item ->
                    LoveBuzzTimeline(
                        item,
                        isLast = index == (v.data.timeline ?: emptyList()).lastIndex,
                        isActive = index == 0
                    )
                }

                item { Spacer(Modifier.height(35.dp)) }
                item { LoveBuzzComparison(v.data.comparison) }
                item { Spacer(Modifier.height(12.dp)) }
                item { LoveBuzzChildren(v.data.children) }
                item { Spacer(Modifier.height(12.dp)) }
                item { LoveBuzzExFiles(v.data.otherRelationship) }
                item { Spacer(Modifier.height(12.dp)) }
                item { LoveBuzzGallery(v.data.photoGallery) }
                item { Spacer(Modifier.height(40.dp)) }
            }
        }

        ButtonArrowBack(Modifier.align(Alignment.TopStart))
    }

    LaunchedEffect(Unit) {
        if (viewModel.loveBuzzFullInfo !is ResponseResult.Success) {
            viewModel.loveBuzzFullInfo(id)
        }
    }
}

@OptIn(ExperimentalGlideComposeApi::class)
@Composable
private fun LoveBuzzHeader(data: LoveBuzzFullInfoResponse) {
    Row(
        Modifier
            .padding(horizontal = 8.dp)
            .fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(Modifier.weight(1f)) {
            if (data.relationshipStatus != null) Surface(
                color = MainColor, shape = RoundedCornerShape(50)
            ) {
                Box(Modifier.padding(horizontal = 12.dp, vertical = 5.dp)) {
                    TextViewSemiBold(data.relationshipStatus.uppercase(), 13)
                }
            }
            Spacer(Modifier.height(8.dp))
            TextViewBold(data.title.orEmpty(), 25)
        }

        Row(Modifier.padding(12.dp), horizontalArrangement = Arrangement.SpaceBetween) {
            Icon(Icons.Default.Share, null, tint = Color.White)
        }
    }
}

@Composable
private fun LoveBuzzStats(data: LoveBuzzFullInfoResponse) {
    Box(
        Modifier
            .padding(horizontal = 8.dp)
            .fillMaxWidth()
            .border(0.5.dp, Color.White, RoundedCornerShape(20.dp))
            .background(Color.Black)
            .padding(10.dp)
    ) {
        Row(Modifier.fillMaxSize(), verticalAlignment = Alignment.CenterVertically) {
            if (data.relationshipDuration?.relationshipDuration != null) {
                StatItem(
                    data.relationshipDuration.relationshipDuration.toString(),
                    "${data.relationshipDuration.placeholder} ${stringResource(R.string.in_relationship)}",
                    modifier = Modifier.weight(1f)
                )
            }

            if (data.relationshipDuration?.relationshipDuration != null && data.children?.isNotEmpty() == true) Box(
                Modifier
                    .width(1.dp)
                    .height(40.dp)
                    .background(Color.White.copy(alpha = 0.1f))
            )

            if (data.children?.isNotEmpty() == true) {
                StatItem(
                    "${data.children.size}", stringResource(R.string.children),
                    modifier = Modifier.weight(1f)
                )
            }
        }
    }
}


@Composable
fun StatItem(value: String, label: String, modifier: Modifier = Modifier) {
    Column(modifier, horizontalAlignment = Alignment.CenterHorizontally) {
        TextViewBold(value, 24)
        Spacer(modifier = Modifier.height(4.dp))
        TextViewNormal(label, 12, Color.White.copy(0.8f))
    }
}


@Composable
private fun LoveBuzzTimeline(
    item: LoveBuzzFullInfoResponse.Timeline, isLast: Boolean, isActive: Boolean
) {
    Row(
        Modifier
            .fillMaxWidth()
            .height(IntrinsicSize.Min)
    ) {
        Box(
            modifier = Modifier
                .fillMaxHeight()
                .width(24.dp),
            contentAlignment = Alignment.TopCenter
        ) {
            Box(
                modifier = Modifier
                    .fillMaxHeight()
                    .width(2.dp)
                    .background(Color.Gray.copy(alpha = 0.4f))
            )

            Box(
                modifier = Modifier
                    .padding(top = 6.dp)
                    .size(if (isActive) 12.dp else 8.dp)
                    .background(
                        color = if (isActive) Color(0xFFE53935) else Color.Gray, shape = CircleShape
                    )
            )
        }

        Spacer(Modifier.width(12.dp))

        Column(
            Modifier
                .padding(bottom = if (isLast) 0.dp else 32.dp)
                .weight(1f)
        ) {
            TextViewNormal(item.date?.uppercase().orEmpty(), 13, Color.White.copy(0.8f))
            Spacer(modifier = Modifier.height(2.dp))
            TextViewSemiBold(item.event.orEmpty())
        }
    }
}

val DarkBackground = Color(0xFF161616) // Main card background
val CardBackground = Color(0xFF1E1E1E) // Slightly lighter if needed, or use black
val RedAccent = Color(0xFFE94057)
val GrayBorder = Color(0xFF484848)
val TextWhite = Color.White
val TextLabel = Color(0xFF666666)

@OptIn(ExperimentalGlideComposeApi::class)
@Composable
fun ProfileItem(name: String, imageRes: String) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier.width(100.dp)
    ) {
        GlideImage(
            imageRes,
            contentDescription = name,
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .size(80.dp)
                .padding(4.dp) // Gap between border and image
                .clip(CircleShape)
                .background(Color.Gray) // Placeholder bg
        )
        Spacer(modifier = Modifier.height(12.dp))
        Text(
            text = name,
            color = TextWhite,
            fontWeight = FontWeight.Bold,
            fontSize = 14.sp,
            textAlign = TextAlign.Center,
            lineHeight = 18.sp
        )
    }
}

@OptIn(ExperimentalGlideComposeApi::class)
@Composable
private fun LoveBuzzComparison(comparison: LoveBuzzFullInfoResponse.Comparison?) {
    Card(
        modifier = Modifier
            .width(340.dp)
            .wrapContentHeight(),
        shape = RoundedCornerShape(32.dp),
        colors = CardDefaults.cardColors(containerColor = DarkBackground)
    ) {
        Column(
            modifier = Modifier.padding(24.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                // Left Profile
                ProfileItem(
                    name = comparison?.personA?.name.orEmpty(),
                    imageRes = comparison?.personA?.image.orEmpty(),
                )

                // VS Text
                Text(
                    text = "VS",
                    color = Color(0xFF333333),
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Black,
                    fontStyle = FontStyle.Italic
                )

                ProfileItem(
                    name = comparison?.personB?.name.orEmpty(),
                    imageRes = comparison?.personB?.image.orEmpty(),
                )

            }

            Spacer(modifier = Modifier.height(32.dp))

            StatRow(label = "Age", leftValue = "32", rightValue = "32")
            StatRow(label = "Height", leftValue = "5' 4\"", rightValue = "5' 11\"")
            StatRow(label = "Zodiac", leftValue = "Cancer", rightValue = "Leo")
            StatRow(
                label = "Job",
                leftValue = "Vocalist",
                rightValue = "Footballer",
                showDivider = false
            )
        }
    }
}
@Composable
fun StatRow(label: String, leftValue: String, rightValue: String, showDivider: Boolean = true) {
    Column {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Left Value
            Text(
                text = leftValue,
                color = TextWhite,
                fontSize = 16.sp,
                fontWeight = FontWeight.Medium,
                modifier = Modifier.weight(1f),
                textAlign = TextAlign.Start
            )

            // Center Label (AGE, HEIGHT, etc.)
            Text(
                text = label.uppercase(),
                color = TextLabel,
                fontSize = 12.sp,
                fontWeight = FontWeight.Bold,
                letterSpacing = 2.sp,
                modifier = Modifier.weight(1f),
                textAlign = TextAlign.Center
            )

            // Right Value
            Text(
                text = rightValue,
                color = TextWhite,
                fontSize = 16.sp,
                fontWeight = FontWeight.Medium,
                modifier = Modifier.weight(1f),
                textAlign = TextAlign.End
            )
        }

        if (showDivider) {
            HorizontalDivider(
                thickness = 1.dp, color = Color(0xFF222222)
            )
        }
    }
}

/* ---------------- CHILDREN ---------------- */

@Composable
private fun LoveBuzzChildren(children: List<LoveBuzzFullInfoResponse.Children>?) {
    if (children?.isEmpty() == true) return
    Column(Modifier.padding(horizontal = 16.dp)) {
        Text("Children", fontWeight = FontWeight.Bold)
        Spacer(Modifier.height(8.dp))
        LazyRow(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
            items(children?.size ?: 0) { i ->
                val c = children?.get(i)
                Card(shape = RoundedCornerShape(12.dp)) {
                    Column(Modifier.padding(12.dp)) {
                        Text(c?.name.orEmpty(), fontWeight = FontWeight.SemiBold)
                        Text("${c?.age} years old", fontSize = 12.sp, color = Color.Gray)
                    }
                }
            }
        }
    }
}

/* ---------------- EX FILES ---------------- */

@OptIn(ExperimentalGlideComposeApi::class)
@Composable
private fun LoveBuzzExFiles(list: List<LoveBuzzFullInfoResponse.OtherRelationship>?) {
    if (list?.isEmpty() == true) return
    Column(Modifier.padding(horizontal = 16.dp)) {
        Text("Ex-Files", fontWeight = FontWeight.Bold)
        Spacer(Modifier.height(8.dp))
        LazyRow(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
            items(list?.size ?: 0) { i ->
                val p = list?.get(i)
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    GlideImage(
                        model = p?.image,
                        contentDescription = p?.name,
                        modifier = Modifier
                            .size(80.dp)
                            .clip(RoundedCornerShape(10.dp)),
                        contentScale = ContentScale.Crop
                    )
                    Spacer(Modifier.height(6.dp))
                    Text(
                        p?.name.orEmpty(),
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        fontSize = 12.sp
                    )
                }
            }
        }
    }
}

/* ---------------- GALLERY ---------------- */

@OptIn(ExperimentalGlideComposeApi::class)
@Composable
private fun LoveBuzzGallery(photos: List<LoveBuzzFullInfoResponse.PhotoGallery>?) {
    if (photos?.isEmpty() == true) return
    Column(Modifier.padding(horizontal = 16.dp)) {
        Text("Spotted", fontWeight = FontWeight.Bold)
        Spacer(Modifier.height(8.dp))
        val rows = photos?.chunked(2)
        rows?.forEach { row ->
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                row.forEach {
                    GlideImage(
                        model = it.url,
                        contentDescription = it.alt,
                        modifier = Modifier
                            .weight(1f)
                            .height(120.dp)
                            .clip(RoundedCornerShape(10.dp)),
                        contentScale = ContentScale.Crop
                    )
                }
                if (row.size == 1) Spacer(Modifier.weight(1f))
            }
            Spacer(Modifier.height(8.dp))
        }
    }
}

