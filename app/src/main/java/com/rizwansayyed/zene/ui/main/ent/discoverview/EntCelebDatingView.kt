package com.rizwansayyed.zene.ui.main.ent.discoverview

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.combinedClickable
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
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.rememberNestedScrollInteropConnection
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.bumptech.glide.integration.compose.GlideImage
import com.rizwansayyed.zene.R
import com.rizwansayyed.zene.data.model.EntertainmentDiscoverResponse
import com.rizwansayyed.zene.data.model.WhoDatedWhoData
import com.rizwansayyed.zene.service.notification.NavigationUtils
import com.rizwansayyed.zene.ui.main.home.EntSectionSelector
import com.rizwansayyed.zene.ui.theme.LoveBuzzBg
import com.rizwansayyed.zene.ui.theme.MainColor
import com.rizwansayyed.zene.ui.view.ImageIcon
import com.rizwansayyed.zene.ui.view.TextViewBold
import com.rizwansayyed.zene.ui.view.TextViewLight
import com.rizwansayyed.zene.ui.view.TextViewNormal
import com.rizwansayyed.zene.ui.view.TextViewSemiBold
import com.rizwansayyed.zene.utils.share.MediaContentUtils.startMedia
import com.rizwansayyed.zene.viewmodel.NavigationViewModel


@Composable
fun EntCelebDatingView(data: EntertainmentDiscoverResponse, viewModel: NavigationViewModel) {
    Spacer(Modifier.height(50.dp))
    Row(Modifier.padding(horizontal = 6.dp), verticalAlignment = Alignment.CenterVertically) {
        TextViewBold(stringResource(R.string.love_buzz), 23)

        Spacer(Modifier.weight(1f))

        Box(Modifier.clickable {
            viewModel.setEntNavigation(EntSectionSelector.DATING)
        }) {
            ImageIcon(R.drawable.ic_arrow_right, 29, Color.White)
        }
    }
    Spacer(Modifier.height(12.dp))

    LazyRow(
        horizontalArrangement = Arrangement.spacedBy(16.dp),
        contentPadding = PaddingValues(horizontal = 16.dp)
    ) {
        items(data.dated ?: emptyList()) { dated ->
            CoupleCard(dated)
        }
    }
}

@Composable
fun CoupleCard(dated: WhoDatedWhoData) {
    var fullInfoSheet by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .clip(RoundedCornerShape(28.dp))
            .background(LoveBuzzBg)
            .combinedClickable(onLongClick = {
                NavigationUtils.triggerInfoSheet(dated.toMusicData())
            }, onClick = {
                fullInfoSheet = true
            })
            .padding(20.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        OverlappingAvatars(dated)

        Spacer(modifier = Modifier.height(16.dp))

        Row(verticalAlignment = Alignment.CenterVertically) {
            Row(Modifier.widthIn(max = 100.dp)) {
                TextViewSemiBold("${dated.coupleComparison?.personA?.name?.trim()}", 14, line = 1)
            }
            TextViewSemiBold("  &  ", size = 14)
            Row(Modifier.widthIn(max = 100.dp)) {
                TextViewSemiBold("${dated.coupleComparison?.personB?.name?.trim()}", size = 14, line = 1)
            }
        }
    }

    if (fullInfoSheet) DatedInfoSheet(dated) {
        fullInfoSheet = false
    }
}

@OptIn(ExperimentalGlideComposeApi::class)
@Composable
fun OverlappingAvatars(data: WhoDatedWhoData) {
    Box(contentAlignment = Alignment.Center) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy((-7).dp)
        ) {
            GlideImage(
                model = data.coupleComparison?.personA?.image ?: "",
                contentDescription = null,
                modifier = Modifier
                    .size(64.dp)
                    .clip(CircleShape),
                contentScale = ContentScale.Crop
            )

            GlideImage(
                model = data.coupleComparison?.personB?.image ?: "",
                contentDescription = null,
                modifier = Modifier
                    .size(64.dp)
                    .clip(CircleShape),
                contentScale = ContentScale.Crop
            )
        }

        Box(
            modifier = Modifier
                .size(26.dp)
                .background(data.relationshipBadge()?.color ?: Color.Black, CircleShape),
            contentAlignment = Alignment.Center
        ) {
            ImageIcon(data.relationshipBadge()?.icon ?: R.drawable.ic_romance_couple, 20,Color.Black)
        }
    }
}

@OptIn(ExperimentalGlideComposeApi::class, ExperimentalMaterial3Api::class)
@Composable
fun DatedInfoSheet(data: WhoDatedWhoData, close: () -> Unit) {
    ModalBottomSheet(close, contentColor = MainColor, containerColor = MainColor) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(MainColor)
        ) {
            Column(
                modifier = Modifier
                    .verticalScroll(rememberScrollState())
                    .nestedScroll(rememberNestedScrollInteropConnection())
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(400.dp)
                ) {
                    GlideImage(
                        model = data.bannerImage,
                        contentDescription = data.relationshipSummary,
                        contentScale = ContentScale.Crop,
                        modifier = Modifier.fillMaxSize()
                    )

                    Box(
                        Modifier
                            .fillMaxSize()
                            .background(
                                Brush.verticalGradient(listOf(Color.Transparent, MainColor))
                            )
                    )

                    Column(
                        Modifier
                            .padding(top = 30.dp)
                            .align(Alignment.BottomStart)
                            .padding(10.dp)
                    ) {
                        Box(
                            Modifier
                                .background(Color.Black, RoundedCornerShape(50))
                                .padding(horizontal = 15.dp, vertical = 7.dp)
                        ) {
                            TextViewSemiBold(data.meta?.getStatus().orEmpty(), 16)
                        }

                        Spacer(Modifier.height(12.dp))
                        TextViewBold(
                            "${data.coupleComparison?.personA?.name} & ${data.coupleComparison?.personB?.name}",
                            27
                        )
                    }
                }

                Column(
                    Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 10.dp)
                ) {
                    Spacer(Modifier.height(16.dp))
                    TextViewNormal(data.relationshipSummary.orEmpty(), 15, center = true)
                    Spacer(modifier = Modifier.height(30.dp))

                    if (data.about?.isNotEmpty() == true) {
                        TextViewLight(stringResource(R.string.about), 16, Color.LightGray)
                        Spacer(modifier = Modifier.height(3.dp))

                        data.about.forEach { v ->
                            TextViewNormal(v, 15)
                            Spacer(modifier = Modifier.height(10.dp))
                        }

                        Spacer(Modifier.height(20.dp))
                    }

                    Row(
                        Modifier
                            .fillMaxWidth()
                            .background(Color.Black, RoundedCornerShape(16.dp))
                            .padding(16.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        data.relationshipBadge()?.icon?.let {
                            Box(
                                Modifier
                                    .size(40.dp)
                                    .background(Color(0xFFE23A5E), RoundedCornerShape(12.dp)),
                                contentAlignment = Alignment.Center
                            ) {
                                ImageIcon(it, 16)
                            }
                        }
                        Spacer(modifier = Modifier.width(12.dp))

                        Column(modifier = Modifier.weight(1f)) {
                            TextViewSemiBold(
                                "${stringResource(R.string.event)} • ${
                                    data.meta?.getStatus().orEmpty()
                                }".uppercase(), 13, Color.Gray
                            )

                            TextViewBold(data.meta?.getDate().orEmpty(), 19)
                        }
                    }

                    Spacer(modifier = Modifier.height(24.dp))

                    Button(
                        onClick = {
                            startMedia(data.toMusicData())
                            close()
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(56.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = Color.White),
                        shape = RoundedCornerShape(28.dp)
                    ) {
                        TextViewNormal(
                            stringResource(R.string.view_full_relationship_profile),
                            18, Color.Black
                        )
                    }
                    Spacer(modifier = Modifier.height(20.dp))
                }
            }
        }
    }
}