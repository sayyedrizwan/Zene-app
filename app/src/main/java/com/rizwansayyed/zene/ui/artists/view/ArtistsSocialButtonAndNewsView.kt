package com.rizwansayyed.zene.ui.artists.view

import android.net.Uri
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.rizwansayyed.zene.data.api.model.ZeneArtistsInfoResponse
import com.rizwansayyed.zene.ui.view.TextPoppins
import com.rizwansayyed.zene.ui.view.TextPoppinsThin
import com.rizwansayyed.zene.utils.Utils
import com.rizwansayyed.zene.utils.Utils.openBrowser


@Composable
fun ArtistsSocialButton(social: List<ZeneArtistsInfoResponse.SocialMedia?>?) {

    if (social?.isNotEmpty() == true) {
        Spacer(Modifier.height(80.dp))

        LazyRow(Modifier.fillMaxWidth()) {
            items(social) {
                Column(
                    Modifier
                        .padding(horizontal = 9.dp)
                        .size(200.dp, 150.dp)
                        .clip(RoundedCornerShape(14.dp))
                        .background(Color.Black)
                        .clickable { it?.url?.let { it1 -> openBrowser(it1) } }
                        .padding(vertical = 9.dp, horizontal = 10.dp),
                    Arrangement.Center
                ) {

                    Spacer(Modifier.height(20.dp))

                    TextPoppins(v = it?.title ?: "", false, size = 15, limit = 1)

                    Spacer(Modifier.height(10.dp))

                    if (it?.username != null) {
                        TextPoppinsThin(v = "/${it.username}", false, size = 15, limit = 1)
                    } else {
                        TextPoppinsThin(
                            v = it?.url?.substringAfter("https://www.")
                                ?.substringAfter("https://") ?: "", false, size = 15, limit = 1
                        )
                    }

                    Spacer(Modifier.height(20.dp))
                }
            }
        }
    }
}

@Composable
fun ArtistsNews(modifier: Modifier = Modifier) {

}
