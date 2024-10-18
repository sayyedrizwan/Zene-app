package com.rizwansayyed.zene.ui.home.view

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.SheetState
import androidx.compose.material3.SheetValue
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.rizwansayyed.zene.R
import com.rizwansayyed.zene.ui.theme.MainColor
import com.rizwansayyed.zene.ui.view.TextPoppinsSemiBold
import com.rizwansayyed.zene.utils.FirebaseLogEvents
import com.rizwansayyed.zene.utils.FirebaseLogEvents.logEvents
import com.rizwansayyed.zene.utils.Utils.openWallZAppPageOnPlayStore

private val wallzImg = listOf(
    "https://i.pinimg.com/control/564x/70/24/da/7024dad2d176f1f933313d7e6603692e.jpg",
    "https://i.pinimg.com/control/564x/a3/36/d3/a336d36ab60a2af21e73a7151f24dc23.jpg",
    "https://i.pinimg.com/control/564x/5b/cf/74/5bcf741a7ff7a364a06722bafedc5b35.jpg"
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun WallZBottomSheetView(close: () -> Unit) {
    val pagerState = rememberPagerState(pageCount = { wallzImg.size })
    val context = LocalContext.current


    ModalBottomSheet(close, sheetState = SheetState(true, Density(context), SheetValue.Expanded)) {
        Column(Modifier.fillMaxWidth(), Arrangement.Center, Alignment.CenterHorizontally) {
            HorizontalPager(state = pagerState) { page ->
                AsyncImage(
                    wallzImg[page],
                    "",
                    Modifier
                        .fillMaxWidth()
                        .height(500.dp),
                    contentScale = ContentScale.Crop
                )
            }

            Spacer(Modifier.height(20.dp))
            Row(
                Modifier
                    .padding(vertical = 17.dp, horizontal = 14.dp)
                    .clickable {
                        logEvents(FirebaseLogEvents.FirebaseEvents.OPEN_ZENE_WALLZ)
                        openWallZAppPageOnPlayStore()
                    }
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(13.dp))
                    .background(MainColor)
                    .padding(vertical = 15.dp),
                Arrangement.Center, Alignment.CenterVertically
            ) {
                TextPoppinsSemiBold(stringResource(R.string.open_on_play_store), true, size = 14)
            }
            Spacer(Modifier.height(20.dp))
        }
    }
}