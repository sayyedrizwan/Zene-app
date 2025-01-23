package com.rizwansayyed.zene.ui.main.connect.profile

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.unit.dp
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.bumptech.glide.integration.compose.GlideImage
import com.rizwansayyed.zene.data.model.ConnectUserResponse


@OptIn(ExperimentalGlideComposeApi::class)
@Composable
fun ConnectProfileProfilePhotoView(user: ConnectUserResponse?) {
    val screenHeight = LocalConfiguration.current.screenHeightDp.dp

    Box(Modifier.fillMaxSize()) {
        GlideImage(
            user?.profile_photo,
            user?.name,
            Modifier
                .align(Alignment.TopCenter)
                .padding(bottom = 10.dp)
                .fillMaxWidth()
                .height(screenHeight * 0.55f),
            contentScale = ContentScale.Crop
        )
    }
}