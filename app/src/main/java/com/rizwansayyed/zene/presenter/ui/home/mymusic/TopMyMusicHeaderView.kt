package com.rizwansayyed.zene.presenter.ui.home.mymusic

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
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
import com.rizwansayyed.zene.presenter.ui.TextSemiBold
import com.rizwansayyed.zene.presenter.ui.TextThin

@Composable
fun TopMyMusicHeader(userAuth: UserAuthData?) {

    Spacer(Modifier.height(60.dp))

    Row(Modifier.fillMaxWidth(), Arrangement.Center, Alignment.CenterVertically) {
        if (userAuth?.isLoggedIn() == true && userAuth.photo != null)
            AsyncImage(
                userAuth.photo, "",
                Modifier
                    .size(70.dp)
                    .clip(RoundedCornerShape(100))
                    .background(Color.White),
                contentScale = ContentScale.Crop
            )
        else
            Image(
                painterResource(R.drawable.music_person), "",
                Modifier
                    .size(70.dp)
                    .clip(RoundedCornerShape(100))
                    .background(Color.White)
                    .padding(15.dp),
            )

        Column(
            Modifier
                .padding(horizontal = 5.dp)
                .weight(1f),
            Arrangement.Center,
        ) {
            TextSemiBold("Zene App", size = 25)
            Spacer(Modifier.height(9.dp))
            TextThin("Login to sync", Modifier.padding(start = 3.dp))
        }
    }
}