package com.rizwansayyed.zene.ui.login

import android.app.Activity
import android.net.Uri
import android.widget.VideoView
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.viewmodel.compose.viewModel
import com.rizwansayyed.zene.R
import com.rizwansayyed.zene.data.db.DataStoreManager.userInfoDB
import com.rizwansayyed.zene.ui.login.flow.LoginFlowType
import com.rizwansayyed.zene.ui.theme.MainColor
import com.rizwansayyed.zene.ui.view.ImageIcon
import com.rizwansayyed.zene.ui.view.LoadingView
import com.rizwansayyed.zene.ui.view.TextAntroVenctra
import com.rizwansayyed.zene.ui.view.TextPoppins
import com.rizwansayyed.zene.ui.view.TextPoppinsLight
import com.rizwansayyed.zene.ui.view.TextPoppinsSemiBold
import com.rizwansayyed.zene.utils.FirebaseLogEvents
import com.rizwansayyed.zene.utils.FirebaseLogEvents.logEvents
import com.rizwansayyed.zene.utils.Utils.URLS.PRIVACY_POLICY
import com.rizwansayyed.zene.utils.Utils.shareTxtImage
import com.rizwansayyed.zene.utils.Utils.toast
import com.rizwansayyed.zene.viewmodel.HomeViewModel
import kotlinx.coroutines.delay
import kotlin.time.Duration.Companion.seconds


@Composable
fun LoginView() {
    val isLogin by userInfoDB.collectAsState(initial = null)

    if (isLogin?.isLoggedIn() == false) LoginViewSpace()
}

@Composable
fun LoginViewSpace() {
    val context = LocalContext.current.applicationContext
    val lifecycleOwner = LocalLifecycleOwner.current
    val lifecycleState by lifecycleOwner.lifecycle.currentStateFlow.collectAsState()

    var videoView by remember { mutableStateOf<VideoView?>(null) }
    var visibleLoginTopView by remember { mutableStateOf(false) }

    Box(
        Modifier
            .fillMaxSize()
            .background(Color.Black)
    ) {
        AndroidView({ ctx ->
            VideoView(ctx).apply {
                videoView = this
                val uriPath = "android.resource://${context.packageName}/${R.raw.bg_login}"
                val uri = Uri.parse(uriPath)
                setVideoURI(uri)
                start()
                setOnPreparedListener { mp ->
                    mp.isLooping = true
                    mp.setVolume(0f, 0f)
                }
            }
        }, Modifier.fillMaxSize())

        AnimatedVisibility(
            visibleLoginTopView, Modifier.fillMaxSize(), fadeIn(tween(1000)), fadeOut(tween(1000))
        ) {
            LoginZeneLogo()
        }
    }

    LaunchedEffect(lifecycleState) {
        if (lifecycleState == Lifecycle.State.RESUMED) videoView?.start()
        delay(2.seconds)
        visibleLoginTopView = true
    }
}

@Composable
fun LoginZeneLogo() {
    var visibleLoginButton by remember { mutableStateOf(false) }

    var nameText by remember { mutableStateOf("") }
    val name = stringResource(R.string.app_name)

    Box(
        Modifier
            .fillMaxSize()
            .background(MainColor.copy(0.7f))
    ) {
        Column(
            Modifier
                .fillMaxSize()
                .align(Alignment.Center),
            Arrangement.Center,
            Alignment.CenterHorizontally
        ) {
            TextAntroVenctra(nameText)

            AnimatedVisibility(
                visibleLoginButton,
                Modifier
                    .fillMaxWidth()
                    .offset(x = 5.dp, y = (-35).dp),
                fadeIn(tween(1000)),
                fadeOut(tween(1000))
            ) {
                TextPoppinsLight(stringResource(R.string.a_music_app), true, size = 18)
            }
        }

        AnimatedVisibility(
            visibleLoginButton,
            Modifier
                .fillMaxWidth()
                .align(Alignment.BottomCenter),
            fadeIn(tween(1000)),
            fadeOut(tween(1000))
        ) {
            LoginButtonView()
        }
    }

    LaunchedEffect(Unit) {
        logEvents(FirebaseLogEvents.FirebaseEvents.OPEN_LOGIN_VIEW)
        while (nameText != name) {
            delay(400)
            nameText += name.split("").filter { it.isNotEmpty() }[nameText.length]
        }
        visibleLoginButton = true
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LoginButtonView() {
    val homeViewModel: HomeViewModel = viewModel()

    var emailLogin by remember { mutableStateOf(false) }
    var isLoginLoading by remember { mutableStateOf(false) }

    val activity = LocalContext.current as Activity
    var bottomSheet by remember { mutableStateOf(false) }

    val errorLogin = stringResource(R.string.error_while_login)
    val openURLOnBrowser = stringResource(R.string.open_url_on_browser)

    val imgBorder = Modifier
        .padding(9.dp)
        .size(50.dp)
        .border(BorderStroke(1.dp, Color.White), RoundedCornerShape(100))
        .padding(9.dp)

    Row(Modifier
        .padding(bottom = 70.dp)
        .padding(horizontal = 15.dp)
        .clip(RoundedCornerShape(15.dp))
        .fillMaxWidth()
        .background(Color.White)
        .clickable {
            bottomSheet = true
        }
        .padding(5.dp), Arrangement.Center, Alignment.CenterVertically) {
        Spacer(Modifier.height(40.dp))
        if (isLoginLoading) LoadingView(Modifier.size(34.dp))
        else TextPoppinsSemiBold(stringResource(R.string.login_to_continue), true, MainColor, 17)
        Spacer(Modifier.height(40.dp))
    }

    fun startLogin(t: LoginFlowType) {
        emailLogin = false
        bottomSheet = false
        isLoginLoading = true
        homeViewModel.loginFlow.init(t, activity) {
            isLoginLoading = false
            errorLogin.toast()
        }
    }

    if (emailLogin) LoginEmailAlertView(homeViewModel) {
        emailLogin = false
    }

    if (bottomSheet) ModalBottomSheet({ bottomSheet = false }, containerColor = Color.Black) {
        Column(Modifier.padding(horizontal = 10.dp)) {
            Spacer(Modifier.height(20.dp))

            TextPoppins(stringResource(R.string.login_to_enjoy_free_music), false, Color.White, 15)

            Spacer(Modifier.height(30.dp))

            Row(Modifier.fillMaxWidth(), Arrangement.SpaceAround) {
                Image(painterResource(R.drawable.ic_google),
                    stringResource(R.string.login_to_continue),
                    imgBorder.clickable {
                        startLogin(LoginFlowType.GOOGLE)
                    })

                Image(painterResource(R.drawable.ic_apple),
                    stringResource(R.string.login_to_continue),
                    imgBorder.clickable {
                        startLogin(LoginFlowType.APPLE)
                    })

                Image(painterResource(R.drawable.ic_facebook),
                    stringResource(R.string.login_to_continue),
                    imgBorder.clickable {
                        startLogin(LoginFlowType.FACEBOOK)
                    })
            }

            Spacer(Modifier.height(40.dp))

            Row(Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp)
                .clip(RoundedCornerShape(10.dp))
                .background(MainColor)
                .clickable {
                    emailLogin = true
                    bottomSheet = false
                }
                .padding(vertical = 10.dp), Arrangement.Center, Alignment.CenterVertically) {
                Spacer(Modifier.width(6.dp))
                TextPoppins(
                    stringResource(R.string.sign_in_with_email), false, Color.White, 14
                )
                Spacer(Modifier.width(6.dp))
                ImageIcon(R.drawable.ic_arrow_right, 21)
            }

            Spacer(Modifier.height(80.dp))

            Row(Modifier.clickable {
                shareTxtImage(PRIVACY_POLICY, openURLOnBrowser)
                openURLOnBrowser.toast()
            }) {
                TextPoppins(
                    stringResource(R.string.agreeing_privacy_policy), true, Color.Blue, 10
                )
            }

            Spacer(Modifier.height(50.dp))
        }
    }
}