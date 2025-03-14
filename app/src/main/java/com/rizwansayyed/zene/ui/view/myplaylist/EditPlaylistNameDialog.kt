package com.rizwansayyed.zene.ui.view.myplaylist

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.hilt.navigation.compose.hiltViewModel
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.bumptech.glide.integration.compose.GlideImage
import com.rizwansayyed.zene.R
import com.rizwansayyed.zene.data.ResponseResult
import com.rizwansayyed.zene.data.model.MusicDataTypes
import com.rizwansayyed.zene.data.model.ZeneMusicData
import com.rizwansayyed.zene.datastore.model.MusicPlayerData
import com.rizwansayyed.zene.ui.theme.BlackGray
import com.rizwansayyed.zene.ui.theme.MainColor
import com.rizwansayyed.zene.ui.view.ButtonWithBorder
import com.rizwansayyed.zene.ui.view.CircularLoadingView
import com.rizwansayyed.zene.ui.view.ImageIcon
import com.rizwansayyed.zene.ui.view.TextAlertDialog
import com.rizwansayyed.zene.ui.view.TextViewNormal
import com.rizwansayyed.zene.ui.view.TextViewSemiBold
import com.rizwansayyed.zene.utils.share.MediaContentUtils.startMedia
import com.rizwansayyed.zene.viewmodel.MyLibraryViewModel

@Composable
fun EditPlaylistNameDialog(data: ZeneMusicData, close: (Boolean) -> Unit) {
    Dialog(onDismissRequest = { close(false) }) {
        val viewModel: MyLibraryViewModel = hiltViewModel(key = data.name)
        val titleNameChange = stringResource(R.string.edit_your_playlist_new_name)
        var name by remember { mutableStateOf(data.name ?: "") }
        var loading by remember { mutableStateOf(false) }
        val focusManager = LocalFocusManager.current

        Column(
            Modifier
                .fillMaxWidth()
                .fillMaxHeight(0.5f)
                .clip(RoundedCornerShape(16.dp))
                .background(MainColor), Arrangement.Center, Alignment.CenterHorizontally
        ) {
            TextViewNormal(titleNameChange, 16)
            Spacer(Modifier.height(10.dp))

            TextField(
                name,
                { if (it.length <= 100) name = it },
                Modifier
                    .padding(10.dp)
                    .fillMaxWidth()
                    .padding(vertical = 4.dp),
                keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
                keyboardActions = KeyboardActions { focusManager.clearFocus() },
                placeholder = {
                    TextViewNormal(
                        stringResource(R.string.playlist_name), 17, Color.Gray, center = true
                    )
                },
                textStyle = TextStyle(textAlign = TextAlign.Center, fontSize = 17.sp),
                shape = RoundedCornerShape(8.dp),
                colors = TextFieldDefaults.colors(
                    focusedContainerColor = BlackGray,
                    unfocusedContainerColor = BlackGray,
                    unfocusedIndicatorColor = Color.Transparent,
                    focusedIndicatorColor = Color.Transparent,
                    cursorColor = Color.White
                ),
                singleLine = true
            )

            Spacer(Modifier.height(10.dp))

            if (loading) {
                CircularLoadingView()
            } else {
                Row(Modifier.fillMaxWidth(), Arrangement.Center, Alignment.CenterVertically) {
                    ButtonWithBorder(R.string.close) {
                        close(false)
                    }
                    Spacer(Modifier.width(10.dp))
                    if (name.trim().length > 3) ButtonWithBorder(R.string.save) {
                        viewModel.updateMyPlaylistName(data.id, name)
                    }
                }
            }

            Spacer(Modifier.height(10.dp))
        }

        LaunchedEffect(viewModel.playlistNameStatus) {
            when (viewModel.playlistNameStatus) {
                ResponseResult.Empty -> loading = false
                is ResponseResult.Error -> {}
                ResponseResult.Loading -> loading = true
                is ResponseResult.Success -> {
                    loading = false
                    close(true)
                }
            }
        }
    }
}

@OptIn(ExperimentalGlideComposeApi::class)
@Composable
fun MyPlaylistItemView(
    data: ZeneMusicData,
    info: MusicPlayerData?,
    viewModel: MyLibraryViewModel,
    remove: (Boolean) -> Unit
) {
    var confirmationSheet by remember { mutableStateOf(false) }

    Box(Modifier
        .padding(top = 15.dp)
        .padding(horizontal = 5.dp, vertical = 10.dp)
        .fillMaxWidth()
        .clip(RoundedCornerShape(13.dp))
        .background(BlackGray)
        .clickable {
            startMedia(data, viewModel.myPlaylistSongsList)
        }
        .padding(horizontal = 10.dp, vertical = 15.dp), Alignment.Center) {
        Row(
            Modifier
                .fillMaxWidth()
                .padding(horizontal = 5.dp, vertical = 10.dp),
            Arrangement.Center,
            Alignment.CenterVertically
        ) {
            Box {
                GlideImage(
                    data.thumbnail,
                    data.name,
                    Modifier
                        .padding(end = 10.dp)
                        .size(90.dp),
                    contentScale = ContentScale.Crop
                )

                if (info?.data?.id == data.id) GlideImage(
                    R.raw.song_playing_wave,
                    "",
                    Modifier
                        .size(30.dp)
                        .align(Alignment.Center),
                    contentScale = ContentScale.Crop
                )

                Box(
                    Modifier
                        .align(Alignment.BottomStart)
                        .padding(2.dp)
                        .clip(RoundedCornerShape(100))
                        .background(Color.Black)
                        .padding(5.dp)
                ) {
                    if (data.type() == MusicDataTypes.SONGS) {
                        ImageIcon(R.drawable.ic_music_note, 20)
                    } else if (data.type() == MusicDataTypes.AI_MUSIC) {
                        ImageIcon(R.drawable.ic_robot_singing, 20)
                    } else if (data.type() == MusicDataTypes.PODCAST_AUDIO) {
                        ImageIcon(R.drawable.ic_podcast, 20)
                    } else if (data.type() == MusicDataTypes.RADIO) {
                        ImageIcon(R.drawable.ic_radio, 20)
                    } else if (data.type() == MusicDataTypes.VIDEOS) {
                        ImageIcon(R.drawable.ic_video_replay, 20)
                    }
                }
            }

            Column(Modifier.weight(1f), Arrangement.Center, Alignment.Start) {
                TextViewSemiBold(data.name ?: "", 15, line = 2)
                if ((data.artists?.trim()?.length ?: 0) > 3) {
                    TextViewNormal(data.artists!!, 13, line = 1)
                }
            }
        }


        if (viewModel.isPlaylistOfSameUser) Box(Modifier
            .align(Alignment.TopEnd)
            .clickable { confirmationSheet = true }) {
            ImageIcon(R.drawable.ic_delete, 20)
        }
    }

    if (confirmationSheet) TextAlertDialog(R.string.remove_media_from_playlist,
        R.string.remove_media_from_playlist_desc,
        {
            confirmationSheet = false
            remove(false)
        },
        {
            confirmationSheet = false
            remove(true)
        })
}
