package com.rizwansayyed.zene.presenter.ui.home.mymusic

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.rizwansayyed.zene.R
import com.rizwansayyed.zene.domain.UserAuthData
import com.rizwansayyed.zene.presenter.ui.SmallIcons
import com.rizwansayyed.zene.presenter.ui.TextBold
import com.rizwansayyed.zene.presenter.ui.TextRegular
import com.rizwansayyed.zene.presenter.ui.TextThin

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
            TextBold("Rizwan Sayyed", size = 32)
            Spacer(Modifier.height(2.dp))
            TextThin("sayyedrizwanahmed@gmail.com", Modifier.padding(start = 1.dp), size = 11)
        }
    }

}

@Composable
fun LinkedToBrowser(userAuth: UserAuthData?) {

    LazyRow(Modifier.padding(top = 10.dp).fillMaxWidth()) {
        items(6) {
            LinkedToBrowserItem()
        }
    }
}

@Composable
fun LinkedToBrowserItem() {
    Row(
        Modifier
            .padding(10.dp)
            .clip(RoundedCornerShape(14.dp))
            .background(Color.Gray.copy(0.3f))
            .padding(7.dp),
        Arrangement.Center,
        Alignment.CenterVertically
    ) {
        Spacer(Modifier.width(5.dp))
        SmallIcons(R.drawable.ic_plus_sign_square, 18, 0)
        Spacer(Modifier.width(5.dp))
        TextRegular(v = "Login Web")
        Spacer(Modifier.width(5.dp))
    }
}