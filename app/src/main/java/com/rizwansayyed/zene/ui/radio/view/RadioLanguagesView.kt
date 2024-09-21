package com.rizwansayyed.zene.ui.radio.view

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.rizwansayyed.zene.R
import com.rizwansayyed.zene.data.api.APIResponse
import com.rizwansayyed.zene.data.api.model.ZeneMusicDataItems
import com.rizwansayyed.zene.ui.home.view.TextSize
import com.rizwansayyed.zene.ui.home.view.TextTitleHeader
import com.rizwansayyed.zene.ui.view.LoadingText
import com.rizwansayyed.zene.ui.view.TextAntroVenctra
import com.rizwansayyed.zene.ui.view.TextPoppins
import com.rizwansayyed.zene.ui.view.TextPoppinsThin
import com.rizwansayyed.zene.ui.view.imgBuilder
import com.rizwansayyed.zene.viewmodel.RadioViewModel

@Composable
fun RadioCountriesView(radioViewModel: RadioViewModel) {
    when (val v = radioViewModel.radioCountries) {
        APIResponse.Empty -> {}
        is APIResponse.Error -> {}
        APIResponse.Loading -> {
            Spacer(Modifier.height(40.dp))
            TextTitleHeader(Pair(TextSize.SMALL, R.string.radio_countries))

            LazyRow {
                items(20) {
                    LoadingText()
                }
            }
        }

        is APIResponse.Success -> {
            if (v.data.isNotEmpty()) {
                Spacer(Modifier.height(40.dp))

                TextTitleHeader(Pair(TextSize.SMALL, R.string.radio_countries))

                LazyRow {
                    items(v.data) {
                        RadiCountriesItems(it)
                    }
                }
            }
        }
    }
}

@Composable
fun RadiCountriesItems(items: ZeneMusicDataItems) {
    Column(
        Modifier
            .padding(horizontal = 10.dp)
            .clip(RoundedCornerShape(12.dp))
            .clickable { }
            .background(Color.White)
            .padding(20.dp)
            .padding(horizontal = 20.dp),
        Arrangement.Center, Alignment.CenterHorizontally
    ) {
        AsyncImage(
            imgBuilder(items.thumbnail),
            items.name,
            Modifier.padding(vertical = 20.dp).size(80.dp),
            contentScale = ContentScale.Crop
        )
        TextPoppins(items.name?.trim() ?: "", true, Color.Black, 17, limit = 1)
    }
}
        