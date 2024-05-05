package com.rizwansayyed.zene.presenter.ui.home.mymusic

import android.net.Uri
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import androidx.core.content.ContextCompat
import androidx.fragment.app.FragmentActivity
import coil.compose.AsyncImage
import com.rizwansayyed.zene.R
import com.rizwansayyed.zene.domain.UserAuthData
import com.rizwansayyed.zene.presenter.theme.MainColor
import com.rizwansayyed.zene.presenter.ui.SmallIcons
import com.rizwansayyed.zene.presenter.ui.TextBold
import com.rizwansayyed.zene.presenter.ui.TextRegular
import com.rizwansayyed.zene.presenter.ui.TextSemiBold
import com.rizwansayyed.zene.presenter.ui.TextThin
import com.rizwansayyed.zene.utils.Utils.AppUrl.APP_URL
import com.rizwansayyed.zene.utils.Utils.customBrowser


@Composable
fun TopMyMusicHeader(userAuth: UserAuthData?) {

    Spacer(Modifier.height(80.dp))

    Row(Modifier.fillMaxWidth(), Arrangement.Center, Alignment.CenterVertically) {
        if (userAuth?.isLoggedIn() == true && userAuth.photo != null)
            AsyncImage(
                userAuth.photo, userAuth.name,
                Modifier
                    .padding(start = 4.dp)
                    .size(85.dp)
                    .clip(RoundedCornerShape(100))
                    .background(Color.White),
                contentScale = ContentScale.Crop
            )
        else
            Image(
                painterResource(R.drawable.music_person), "",
                Modifier
                    .padding(start = 4.dp)
                    .size(85.dp)
                    .clip(RoundedCornerShape(100))
                    .background(Color.White)
                    .padding(17.dp),
            )

        Column(
            Modifier
                .padding(start = 12.dp)
                .weight(1f),
            Arrangement.Center,
        ) {
            TextBold(stringResource(id = R.string.app_name), size = 32)
            Spacer(Modifier.height(2.dp))
            TextThin("Ad free music experience", Modifier.padding(start = 1.dp), size = 11)
        }
    }

}


@Composable
fun LinkedToBrowser() {
    var scanLoginDialog by remember { mutableStateOf(false) }

    LazyRow(
        Modifier
            .padding(top = 10.dp)
            .fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        item {
            LinkedToBrowserItem(R.drawable.music_pc, stringResource(R.string.sign_in_on_zene_web)) {
                scanLoginDialog = true
            }
        }

        item {
            Spacer(
                Modifier
                    .height(17.dp)
                    .width(3.dp)
                    .clip(RoundedCornerShape(14.dp))
                    .background(Color.Gray)
            )
        }
    }


    if (scanLoginDialog) ScanLoginDialog {
        scanLoginDialog = false
    }
}

@Composable
fun LinkedToBrowserItem(icon: Int, text: String, click: () -> Unit) {
    Row(
        Modifier
            .padding(10.dp)
            .clip(RoundedCornerShape(14.dp))
            .background(Color.Gray.copy(0.3f))
            .clickable { click() }
            .padding(7.dp),
        Arrangement.Center, Alignment.CenterVertically
    ) {
        Spacer(Modifier.width(5.dp))
        SmallIcons(icon, 18, 0)
        Spacer(Modifier.width(5.dp))
        TextSemiBold(v = text)
        Spacer(Modifier.width(5.dp))
    }
}

@Composable
fun ScanLoginDialog(close: () -> Unit) {
    Dialog(close, DialogProperties(usePlatformDefaultWidth = false)) {
        Surface(Modifier.fillMaxSize(), RoundedCornerShape(16.dp), Color.Black) {
            Box(
                Modifier
                    .fillMaxSize()
                    .padding(20.dp)
            ) {
                Row(
                    Modifier.align(Alignment.TopStart),
                    Arrangement.Center,
                    Alignment.CenterVertically
                ) {
                    SmallIcons(icon = R.drawable.ic_arrow_left, 21, 0, close)

                    Spacer(Modifier.width(10.dp))

                    TextSemiBold(v = stringResource(R.string.zene_web))
                }

                Column(
                    Modifier.align(Alignment.Center),
                    Arrangement.Center, Alignment.CenterHorizontally
                ) {
                    Spacer(Modifier.height(50.dp))

                    TextRegular(
                        v = String.format(
                            stringResource(R.string.scan_qr_at_enjoy_sign_in), APP_URL
                        ),
                        Modifier.fillMaxWidth(), doCenter = true, size = 19
                    )

                    Spacer(Modifier.width(10.dp))

                    Row(
                        Modifier
                            .padding(10.dp)
                            .clip(RoundedCornerShape(14.dp))
                            .background(MainColor)
                            .clickable {
                                Uri
                                    .parse(APP_URL)
                                    .customBrowser()
                            }
                            .padding(17.dp),
                        Arrangement.Center,
                        Alignment.CenterVertically
                    ) {
                        TextSemiBold(v = stringResource(R.string.zene_web))
                        Spacer(Modifier.width(10.dp))
                        Box(Modifier.rotate(180f)) {
                            SmallIcons(icon = R.drawable.ic_arrow_left, 21, 0, close)
                        }
                    }

                    Spacer(Modifier.height(50.dp))
                }
            }
        }
    }
}
