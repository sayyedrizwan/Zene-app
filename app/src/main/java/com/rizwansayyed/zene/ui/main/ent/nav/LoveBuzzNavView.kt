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
import com.rizwansayyed.zene.ui.theme.DarkCharcoal
import com.rizwansayyed.zene.ui.theme.LoveBuzzBg
import com.rizwansayyed.zene.ui.theme.MainColor
import com.rizwansayyed.zene.ui.view.ButtonArrowBack
import com.rizwansayyed.zene.ui.view.TextViewBold
import com.rizwansayyed.zene.ui.view.TextViewLight
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
                item {
                    Column(Modifier.fillMaxWidth()) {
                        ArtistsInfoView(v.data.comparison?.personA)
                        Box(Modifier.padding(25.dp)) {
                            HorizontalDivider(thickness = 1.dp, color = Color.Gray)
                        }
                        ArtistsInfoView(v.data.comparison?.personB)
                    }
                }

                item { Spacer(Modifier.height(35.dp)) }
                item { LoveBuzzComparison(v.data.comparison) }
                item { Spacer(Modifier.height(30.dp)) }
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
private fun ArtistsInfoView(v: LoveBuzzFullInfoResponse.PersonsInfo?) {
    Row(Modifier.padding(2.dp), verticalAlignment = Alignment.CenterVertically) {
        if (v != null) {
            GlideImage(
                v.image,
                v.name,
                Modifier
                    .padding(10.dp)
                    .size(80.dp)
                    .clip(RoundedCornerShape(100)),
                contentScale = ContentScale.Crop
            )

            TextViewNormal(v.about.orEmpty(), 14)
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


@OptIn(ExperimentalGlideComposeApi::class)
@Composable
fun ProfileItem(info: LoveBuzzFullInfoResponse.PersonsInfo?, modifier: Modifier) {
    Column(modifier, horizontalAlignment = Alignment.CenterHorizontally) {
        GlideImage(
            info?.image, info?.name,
            Modifier
                .size(110.dp)
                .clip(CircleShape)
                .background(Color.Gray),
            contentScale = ContentScale.Crop,
        )
        Spacer(Modifier.height(12.dp))
        TextViewBold(info?.name.orEmpty(), 15, center = true)
    }
}


@OptIn(ExperimentalGlideComposeApi::class)
@Composable
private fun LoveBuzzComparison(v: LoveBuzzFullInfoResponse.Comparison?) {
    Card(
        Modifier
            .padding(top = 15.dp)
            .padding(10.dp)
            .fillMaxWidth()
            .wrapContentHeight(),
        RoundedCornerShape(32.dp),
        CardDefaults.cardColors(DarkCharcoal)
    ) {
        Column(
            Modifier
                .padding(24.dp)
                .fillMaxWidth()
        ) {
            Row(
                Modifier.fillMaxWidth(), Arrangement.SpaceBetween, Alignment.Top
            ) {
                ProfileItem(v?.personA, Modifier.weight(1f))

                Text(
                    text = "VS",
                    color = Color(0xFF333333),
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Black,
                    fontStyle = FontStyle.Italic,
                    modifier = Modifier.padding(top = 40.dp)
                )

                ProfileItem(v?.personB, Modifier.weight(1f))
            }

            Spacer(modifier = Modifier.height(32.dp))

            if (v?.personA?.age != null || v?.personB?.age != null) StatRow(
                R.string.age_at_start_of_relationship, v.personA?.age, v.personB?.age
            )

            if (v?.personA?.height != null || v?.personB?.height != null) StatRow(
                R.string.height, v.personA?.height, v.personB?.height
            )

            if (v?.personA?.zodiac != null || v?.personB?.zodiac != null) StatRow(
                R.string.zodiac, v.personA?.zodiac, v.personB?.zodiac
            )

            if (v?.personA?.hair_color != null || v?.personB?.hair_color != null) StatRow(
                R.string.hair_color, v.personA?.hair_color, v.personB?.hair_color
            )

            if (v?.personA?.eye_color != null || v?.personB?.eye_color != null) StatRow(
                R.string.eye_color, v.personA?.eye_color, v.personB?.eye_color
            )

            if (v?.personA?.occupation != null || v?.personB?.occupation != null) StatRow(
                R.string.occupation, v.personA?.occupation, v.personB?.occupation
            )

            if (v?.personA?.nationality != null || v?.personB?.nationality != null) StatRow(
                R.string.nationality, v.personA?.nationality, v.personB?.nationality, false
            )
        }
    }
}

@Composable
private fun StatRow(
    label: Int, leftValue: String?, rightValue: String?, showDivider: Boolean = true
) {
    Column {
        Row(
            Modifier
                .fillMaxWidth()
                .padding(vertical = 16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(Modifier.weight(1f)) {
                TextViewSemiBold(leftValue.orEmpty(), 15, center = true)
            }

            Box(Modifier.weight(1f)) {
                TextViewLight(stringResource(label), 15, Color.Gray, true)
            }

            Box(Modifier.weight(1f)) {
                TextViewSemiBold(rightValue.orEmpty(), 15, center = true)
            }

        }

        if (showDivider) {
            HorizontalDivider(thickness = 1.dp, color = Color(0xFF222222))
        }
    }
}

@Composable
private fun LoveBuzzChildren(children: List<LoveBuzzFullInfoResponse.Children>?) {
    if (children?.isNotEmpty() == true) {
        Box(
            Modifier
                .padding(horizontal = 6.dp)
                .padding(bottom = 6.dp)
        ) {
            TextViewBold(stringResource(R.string.children), 23)
        }

        children.forEach { child ->
            Row(
                Modifier
                    .padding(2.dp)
                    .clip(RoundedCornerShape(22.dp))
                    .background(DarkCharcoal)
                    .padding(horizontal = 18.dp, vertical = 20.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(Modifier.weight(1f)) {
                    TextViewSemiBold(child.name.orEmpty(), 19)
                    TextViewLight("${stringResource(R.string.born)} ${child.born}", 14)
                }

                Box(
                    Modifier
                        .background(LoveBuzzBg, RoundedCornerShape(50))
                        .padding(horizontal = 14.dp, vertical = 6.dp), Alignment.Center
                ) {
                    TextViewSemiBold(child.age.orEmpty(), 13, Color.LightGray)
                }
            }

            Spacer(Modifier.height(14.dp))
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

