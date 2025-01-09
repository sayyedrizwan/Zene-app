package com.rizwansayyed.zene.ui.main.connect

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.bumptech.glide.integration.compose.GlideImage
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.MarkerComposable
import com.google.maps.android.compose.rememberMarkerState
import com.rizwansayyed.zene.datastore.DataStorageManager.userInfo
import com.rizwansayyed.zene.ui.view.TextViewBold

@OptIn(ExperimentalGlideComposeApi::class)
@Composable
fun MainMarkerView(currentLatLng: LatLng) {
    val userInfo by userInfo.collectAsState(initial = null)
    val mainUserMarker = rememberMarkerState(position = currentLatLng)

    MarkerComposable(1, mainUserMarker) {
        Box(
            modifier = Modifier
                .size(40.dp)
                .padding(4.dp), contentAlignment = Alignment.Center
        ) {
            GlideImage("https://encrypted-tbn2.gstatic.com/images?q=tbn:ANd9GcR_ObshU2ins98O8AZINR-kjLdMyn2IUmNnkS3CWVW4Nb8j2yuDjUMj0q-luS1YF5pnMbZ0WFyOYxRdNFFxVQrnHQ", userInfo?.name, Modifier.clip(RoundedCornerShape(13.dp)))
            TextViewBold("ddhdhb")
        }
    }
}