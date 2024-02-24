package com.rizwansayyed.zene.presenter.ui.home.mymusic

import android.app.Activity
import android.content.Intent
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyHorizontalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImage
import com.rizwansayyed.zene.R
import com.rizwansayyed.zene.data.DataResponse
import com.rizwansayyed.zene.data.db.datastore.DataStorageManager.loginUser
import com.rizwansayyed.zene.data.utils.CacheFiles.tempProfilePic
import com.rizwansayyed.zene.domain.LoginUserData
import com.rizwansayyed.zene.presenter.theme.BlackColor
import com.rizwansayyed.zene.presenter.theme.MainColor
import com.rizwansayyed.zene.presenter.ui.LoadingStateBar
import com.rizwansayyed.zene.presenter.ui.RoundBorderButtonsView
import com.rizwansayyed.zene.presenter.ui.SearchEditTextView
import com.rizwansayyed.zene.presenter.ui.TextSemiBold
import com.rizwansayyed.zene.presenter.ui.TextThin
import com.rizwansayyed.zene.presenter.ui.TopInfoWithSeeMore
import com.rizwansayyed.zene.presenter.ui.dialog.SimpleTextDialog
import com.rizwansayyed.zene.presenter.util.UiUtils.GridSpan.TOTAL_ITEMS_GRID
import com.rizwansayyed.zene.presenter.util.UiUtils.GridSpan.TWO_ITEMS_GRID
import com.rizwansayyed.zene.presenter.util.UiUtils.toast
import com.rizwansayyed.zene.service.songparty.SongPartyService
import com.rizwansayyed.zene.service.songparty.Utils.Action.partyRoomId
import com.rizwansayyed.zene.service.songparty.Utils.ActionFunctions.closeParty
import com.rizwansayyed.zene.service.songparty.Utils.groupMusicUsersList
import com.rizwansayyed.zene.utils.GoogleSignInUtils
import com.rizwansayyed.zene.utils.Utils.AppUrl.appPartyJoinUrl
import com.rizwansayyed.zene.utils.Utils.copyFileTo
import com.rizwansayyed.zene.utils.Utils.shareTxt
import com.rizwansayyed.zene.viewmodel.HomeApiViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.launch

@Composable
fun MyMusicGroupMusicParty() {
    val userInfo by loginUser.collectAsState(initial = null)

    var isInParty by remember { mutableStateOf(false) }
    var editProfileDialog by remember { mutableStateOf(false) }

    Column {
        Column(Modifier.padding(horizontal = 9.dp)) {
            if (userInfo?.isLogin() == true)
                TopInfoWithSeeMore(R.string.music_group_party, R.string.edit_profile, 50) {
                    editProfileDialog = true
                }
            else
                TopInfoWithSeeMore(R.string.music_group_party, null, 50) {}
        }

        LazyHorizontalGrid(
            GridCells.Fixed(TOTAL_ITEMS_GRID), Modifier
                .fillMaxWidth()
                .height(360.dp)
        ) {
            item(key = 1, span = { GridItemSpan(TOTAL_ITEMS_GRID) }) {
                HostPartyButtonCard(userInfo)
            }
            if (isInParty && groupMusicUsersList.isEmpty())
                item(key = 2, span = { GridItemSpan(TOTAL_ITEMS_GRID) }) {
                    Column(
                        Modifier
                            .padding(start = 10.dp)
                            .size(250.dp, 350.dp)
                            .clip(RoundedCornerShape(12.dp)),
                        Arrangement.Center,
                        Alignment.CenterHorizontally
                    ) {
                        TextSemiBold(v = stringResource(R.string.no_one_is_at_the_party))
                    }
                }

            items(
                groupMusicUsersList,
                key = { i -> i.email },
                span = { GridItemSpan(TWO_ITEMS_GRID) }) {
                Column(
                    Modifier
                        .padding(horizontal = 5.dp, vertical = 2.dp)
                        .size(200.dp, 175.dp)
                        .clip(RoundedCornerShape(12.dp))
                        .background(BlackColor),
                    Arrangement.Center,
                    Alignment.CenterHorizontally
                ) {
                    AsyncImage(
                        it.image, it.name,
                        Modifier
                            .size(50.dp)
                            .clip(RoundedCornerShape(12.dp))
                    )
                    Spacer(Modifier.height(15.dp))

                    TextSemiBold(it.name, Modifier.fillMaxWidth(), true)
                    Spacer(Modifier.height(7.dp))
                    TextThin(
                        stringResource(R.string.is_in_the_party),
                        Modifier.fillMaxWidth(), true, size = 13
                    )
                }
            }
        }
    }


    if (editProfileDialog) EditProfileDialog(userInfo) {
        editProfileDialog = false
    }

    LaunchedEffect(partyRoomId) {
        isInParty = partyRoomId != null
    }
}

@Composable
fun EditProfileDialog(userInfo: LoginUserData?, close: () -> Unit) {
    val coroutine = rememberCoroutineScope()
    val homeApiViewModel: HomeApiViewModel = hiltViewModel()
    val errorLoadingImage = stringResource(R.string.error_loading_image)
    val errorUploadingImage = stringResource(R.string.error_uploading_image)


    var imageLoading by remember { mutableStateOf(false) }
    var name by remember { mutableStateOf(userInfo?.name ?: "") }


    val imgPicker = rememberLauncherForActivityResult(ActivityResultContracts.PickVisualMedia()) {
        if (it?.copyFileTo(tempProfilePic) == true)
            homeApiViewModel.fileUploader(tempProfilePic)
        else
            errorLoadingImage.toast()
    }
    Dialog(close) {
        Surface(Modifier.fillMaxWidth(), RoundedCornerShape(16.dp), MainColor) {
            Column(
                Modifier
                    .fillMaxWidth()
                    .padding(20.dp), Arrangement.Center, Alignment.CenterHorizontally
            ) {
                Spacer(Modifier.height(20.dp))

                Box {
                    AsyncImage(
                        userInfo?.image, userInfo?.name,
                        Modifier
                            .align(Alignment.Center)
                            .size(120.dp)
                            .clip(RoundedCornerShape(100)),
                        contentScale = ContentScale.Crop
                    )

                    if (imageLoading) LoadingStateBar()
                }

                Spacer(Modifier.height(10.dp))


                TextThin(
                    stringResource(R.string.update_profile_picture).lowercase(),
                    Modifier.clickable {
                        imgPicker.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
                    }, size = 14
                )

                Spacer(Modifier.height(30.dp))


                SearchEditTextView(stringResource(R.string.start), name, null, {
                    if (it.length < 25) {
                        name = it
                        coroutine.launch {
                            val u = loginUser.first()
                            u?.name = name
                            loginUser = flowOf(u)
                        }
                    }
                }) {

                }

                Spacer(Modifier.height(30.dp))

                RoundBorderButtonsView(stringResource(R.string.done)) {
                    close()
                }

                Spacer(Modifier.height(50.dp))
            }
        }
    }

    LaunchedEffect(homeApiViewModel.fileUpload) {
        when (val v = homeApiViewModel.fileUpload) {
            DataResponse.Empty -> {}
            is DataResponse.Error -> {
                imageLoading = false
                errorUploadingImage.toast()
            }

            DataResponse.Loading -> {
                imageLoading = true
            }

            is DataResponse.Success -> {
                imageLoading = false
                val u = loginUser.first()
                u?.image = v.item
                loginUser = flowOf(u)
            }
        }
    }
}

@Composable
fun HostPartyButtonCard(userInfo: LoginUserData?) {
    val context = LocalContext.current as Activity
    val coroutine = rememberCoroutineScope()

    val linkCopyInfo = stringResource(R.string.disconnected_from_party)

    var googleSignIn: GoogleSignInUtils? = null

    val googleListener =
        rememberLauncherForActivityResult(ActivityResultContracts.StartActivityForResult()) {
            googleSignIn?.startGoogleStartSignIn(it)
        }

    googleSignIn = GoogleSignInUtils(context, googleListener)

    var showPartyDialog by remember { mutableStateOf(false) }
    var isLoginLoading by remember { mutableStateOf(false) }
    var isInParty by remember { mutableStateOf(false) }

    Column(
        Modifier
            .padding(start = 10.dp)
            .size(250.dp, 350.dp)
            .clip(RoundedCornerShape(12.dp))
            .background(MainColor), Arrangement.Center, Alignment.CenterHorizontally
    ) {

        if (userInfo?.isLogin() == true) {
            if (isInParty) {
                TextSemiBold(
                    v = stringResource(R.string.already_in_a_party),
                    Modifier.fillMaxWidth(), true, size = 17
                )

                Spacer(Modifier.height(15.dp))

                RoundBorderButtonsView(stringResource(R.string.share).lowercase()) {
                    linkCopyInfo.toast()
                    shareTxt(appPartyJoinUrl(partyRoomId!!))
                }

                Spacer(Modifier.height(15.dp))

                RoundBorderButtonsView(stringResource(R.string.leave_party).lowercase()) {
                    closeParty()
                }
            } else {
                TextSemiBold(
                    v = stringResource(R.string.start_new_party),
                    Modifier.fillMaxWidth(), true, size = 17
                )

                Spacer(Modifier.height(15.dp))

                RoundBorderButtonsView(stringResource(R.string.start).lowercase()) {
                    showPartyDialog = true
                }
            }
            LaunchedEffect(partyRoomId) {
                isInParty = partyRoomId != null
            }
        } else {
            TextSemiBold(
                v = stringResource(R.string.start_a_group_party),
                Modifier.fillMaxWidth(), true, size = 20
            )

            Spacer(Modifier.height(25.dp))

            if (isLoginLoading)
                LoadingStateBar()
            else
                RoundBorderButtonsView(stringResource(R.string.login_to_continue)) {
                    isLoginLoading = true
                    coroutine.launch(Dispatchers.IO) {
                        googleSignIn.startLogin()
                        isLoginLoading = false
                    }
                }
        }

        if (showPartyDialog) SimpleTextDialog(
            stringResource(R.string.start_new_party),
            stringResource(R.string.song_group_party_desc),
            stringResource(R.string.start),
            {
                Intent(context, SongPartyService::class.java).apply {
                    flags = Intent.FLAG_ACTIVITY_NEW_TASK
                    context.startService(this)
                }
                showPartyDialog = false
            }, {
                showPartyDialog = false
            })
    }
}