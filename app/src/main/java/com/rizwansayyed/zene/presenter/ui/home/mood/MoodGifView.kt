package com.rizwansayyed.zene.presenter.ui.home.mood

import android.os.Build
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import coil.ImageLoader
import coil.compose.AsyncImage
import coil.decode.GifDecoder
import coil.decode.ImageDecoderDecoder
import com.rizwansayyed.zene.data.DataResponse
import com.rizwansayyed.zene.presenter.ui.TextSemiBold
import com.rizwansayyed.zene.viewmodel.MoodViewModel


@Composable
fun MoodGifView(moodViewModel: MoodViewModel) {
    val height = LocalConfiguration.current.screenHeightDp / 1.3
    val imageLoader = ImageLoader.Builder(LocalContext.current)
        .components {
            if (Build.VERSION.SDK_INT >= 28) {
                add(ImageDecoderDecoder.Factory())
            } else {
                add(GifDecoder.Factory())
            }
        }
        .build()


    when (val v = moodViewModel.gifMood) {
        DataResponse.Empty -> {}
        is DataResponse.Error -> {
            TextSemiBold(v = v.throwable.message ?: " ")
        }

        DataResponse.Loading -> {}
        is DataResponse.Success -> {
            AsyncImage(
                "https://i.giphy.com/${v.item}.gif",
                "", imageLoader,
                Modifier
                    .fillMaxWidth()
                    .height(height.dp),
                contentScale = ContentScale.Crop
            )
        }
    }
}