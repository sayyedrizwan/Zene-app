package com.rizwansayyed.zene.presenter.ui.home.artists

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.rizwansayyed.zene.R
import com.rizwansayyed.zene.data.DataResponse
import com.rizwansayyed.zene.data.db.datastore.DataStorageManager.pinnedArtists
import com.rizwansayyed.zene.di.ApplicationModule.Companion.context
import com.rizwansayyed.zene.domain.PinnedArtistsData
import com.rizwansayyed.zene.presenter.theme.MainColor
import com.rizwansayyed.zene.presenter.ui.LoadingCircle
import com.rizwansayyed.zene.presenter.ui.SmallIcons
import com.rizwansayyed.zene.presenter.ui.TextRegular
import com.rizwansayyed.zene.presenter.ui.TextSemiBold
import com.rizwansayyed.zene.presenter.ui.TextThinArtistsDesc
import com.rizwansayyed.zene.presenter.ui.shimmerBrush
import com.rizwansayyed.zene.presenter.util.UiUtils.toast
import com.rizwansayyed.zene.utils.Utils.AppUrl.appUrlArtistsShare
import com.rizwansayyed.zene.utils.Utils.AppUrl.appUrlSongShare
import com.rizwansayyed.zene.utils.Utils.shareTxt
import com.rizwansayyed.zene.viewmodel.ArtistsViewModel
import com.rizwansayyed.zene.viewmodel.HomeNavViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.cancel
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch

@Composable
fun ArtistsNameWithDescription() {
    val homeNav: HomeNavViewModel = hiltViewModel()
    val artists: ArtistsViewModel = hiltViewModel()

    var showFullDesc by remember { mutableStateOf(false) }

    TextSemiBold(
        homeNav.selectedArtists,
        Modifier
            .padding(start = 6.dp)
            .offset(y = (-45).dp),
        size = 34
    )

    when (val v = artists.artistsDesc) {
        DataResponse.Empty -> {}
        is DataResponse.Error -> {}
        DataResponse.Loading -> repeat(9) {
            Spacer(
                Modifier
                    .padding(horizontal = 10.dp, vertical = 8.dp)
                    .fillMaxWidth()
                    .height(11.dp)
                    .background(shimmerBrush())
            )
        }


        is DataResponse.Success -> {
            TextThinArtistsDesc(v.item.trim(), showFullDesc)
            Spacer(Modifier.height(5.dp))

            if (v.item.trim().length > 10)
                Row(Modifier.fillMaxWidth(), Arrangement.Center, Alignment.CenterVertically) {
                    if (showFullDesc) Box(Modifier.rotate(180f)) {
                        SmallIcons(R.drawable.ic_arrow_down_sharp) {
                            showFullDesc = !showFullDesc
                        }
                    }
                    else SmallIcons(R.drawable.ic_arrow_down_sharp) {
                        showFullDesc = !showFullDesc
                    }
                }
        }
    }
}

@Composable
fun ArtistsButtonView() {
    val artists: ArtistsViewModel = hiltViewModel()
    val homeNav: HomeNavViewModel = hiltViewModel()
    val artistsList by pinnedArtists.collectAsState(emptyArray())

    Row(Modifier.fillMaxWidth(), Arrangement.Center, Alignment.CenterVertically) {
        Row(
            Modifier
                .padding(10.dp)
                .clickable {
                    when (val v = artists.artistsImage) {
                        is DataResponse.Success -> {
                            if (artistsList.any {
                                    it.artistName
                                        .lowercase()
                                        .trim() == homeNav.selectedArtists
                                        .lowercase()
                                        .trim()
                                }) {
                                val a = PinnedArtistsData(homeNav.selectedArtists, v.item)
                                artistsPin(false, a)
                            } else {
                                val a = PinnedArtistsData(homeNav.selectedArtists, v.item)
                                artistsPin(true, a)
                            }
                        }

                        else -> {}
                    }
                }
                .weight(1f)
                .clip(RoundedCornerShape(12.dp))
                .background(MainColor)
                .padding(vertical = 10.dp, horizontal = 22.dp),
            Arrangement.Center, Alignment.CenterVertically
        ) {
            SmallIcons(if (artistsList.any {
                    it.artistName.lowercase().trim() == homeNav.selectedArtists.lowercase().trim()
                }) R.drawable.ic_pin_off else R.drawable.ic_pin, 22, 5)

            Spacer(Modifier.width(6.dp))

            TextRegular(stringResource(if (artistsList.any {
                    it.artistName.lowercase().trim() == homeNav.selectedArtists.lowercase().trim()
                }) R.string.unpin else R.string.pin), size = 16)
        }

        Row(
            Modifier
                .padding(10.dp)
                .clickable {
                    artists.startArtistsRadioPlaylist(homeNav.selectedArtists)
                }
                .weight(1f)
                .clip(RoundedCornerShape(12.dp))
                .background(MainColor)
                .padding(vertical = 10.dp, horizontal = 22.dp),
            Arrangement.Center, Alignment.CenterVertically
        ) {
            when (artists.radioStatus) {
                DataResponse.Loading -> LoadingCircle(22)
                else -> SmallIcons(R.drawable.ic_airdrop, 22, 5)
            }

            Spacer(Modifier.width(6.dp))

            TextRegular(stringResource(R.string.radio), size = 16)
        }
    }

    Row(
        Modifier
            .padding(10.dp)
            .clickable {
                shareTxt(appUrlArtistsShare(homeNav.selectedArtists))
            }
            .fillMaxWidth()
            .clip(RoundedCornerShape(12.dp))
            .background(MainColor)
            .padding(vertical = 10.dp, horizontal = 22.dp),
        Arrangement.Center, Alignment.CenterVertically
    ) {
        SmallIcons(R.drawable.ic_share, 22, 5)

        Spacer(Modifier.width(6.dp))

        TextRegular(stringResource(R.string.share), size = 16)
    }
}

fun artistsPin(add: Boolean, name: PinnedArtistsData) = CoroutineScope(Dispatchers.IO).launch {
    val arrayList = ArrayList<PinnedArtistsData>()
    arrayList.addAll(pinnedArtists.first())

    val onPosition = arrayList.lastIndexOf(name)
    if (onPosition >= 0) arrayList.removeAt(onPosition)

    if (add) arrayList.add(0, name)

    if (arrayList.size > 40){
        context.resources.getString(R.string.max_artists_you_can_add_is_40).toast()
        return@launch
    }

    pinnedArtists = flowOf(arrayList.toTypedArray())
    if (isActive) cancel()
}