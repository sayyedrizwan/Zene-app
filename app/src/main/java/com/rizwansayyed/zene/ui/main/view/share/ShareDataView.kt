package com.rizwansayyed.zene.ui.main.view.share

import android.annotation.SuppressLint
import android.content.pm.ActivityInfo
import android.graphics.drawable.Drawable
import android.view.WindowManager
import androidx.activity.compose.LocalActivity
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import androidx.core.content.ContextCompat
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.LifecycleResumeEffect
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.bumptech.glide.integration.compose.GlideImage
import com.github.alexzhirkevich.customqrgenerator.QrData
import com.github.alexzhirkevich.customqrgenerator.vector.QrCodeDrawable
import com.github.alexzhirkevich.customqrgenerator.vector.QrVectorOptions
import com.github.alexzhirkevich.customqrgenerator.vector.style.QrVectorBallShape
import com.github.alexzhirkevich.customqrgenerator.vector.style.QrVectorColor
import com.github.alexzhirkevich.customqrgenerator.vector.style.QrVectorColors
import com.github.alexzhirkevich.customqrgenerator.vector.style.QrVectorFrameShape
import com.github.alexzhirkevich.customqrgenerator.vector.style.QrVectorLogo
import com.github.alexzhirkevich.customqrgenerator.vector.style.QrVectorLogoPadding
import com.github.alexzhirkevich.customqrgenerator.vector.style.QrVectorLogoShape
import com.github.alexzhirkevich.customqrgenerator.vector.style.QrVectorPixelShape
import com.github.alexzhirkevich.customqrgenerator.vector.style.QrVectorShapes
import com.github.skydoves.colorpicker.compose.HsvColorPicker
import com.github.skydoves.colorpicker.compose.rememberColorPickerController
import com.rizwansayyed.zene.R
import com.rizwansayyed.zene.data.ResponseResult
import com.rizwansayyed.zene.data.model.ZeneMusicData
import com.rizwansayyed.zene.ui.theme.FacebookColor
import com.rizwansayyed.zene.ui.theme.InstagramColor
import com.rizwansayyed.zene.ui.theme.MainColor
import com.rizwansayyed.zene.ui.theme.PinterestColor
import com.rizwansayyed.zene.ui.theme.SnapchatColor
import com.rizwansayyed.zene.ui.theme.WhatsAppColor
import com.rizwansayyed.zene.ui.view.ImageIcon
import com.rizwansayyed.zene.ui.view.ImageWithBorder
import com.rizwansayyed.zene.ui.view.ShimmerEffect
import com.rizwansayyed.zene.ui.view.TextViewNormal
import com.rizwansayyed.zene.ui.view.TextViewSemiBold
import com.rizwansayyed.zene.utils.share.ShareContentUtils
import com.rizwansayyed.zene.utils.share.ShareContentUtils.generateShareUrl
import com.rizwansayyed.zene.utils.share.SharingContentType
import com.rizwansayyed.zene.viewmodel.HomeViewModel
import kotlinx.coroutines.launch

@OptIn(ExperimentalGlideComposeApi::class)
@Composable
fun ShowSharingImageSlider(modifier: Modifier, view: ComposeView, data: ZeneMusicData?) {
    val viewModel: HomeViewModel = hiltViewModel()
    val coroutine = rememberCoroutineScope()

    var centerCrop by remember { mutableStateOf(true) }
    var backgroundColorAlert by remember { mutableStateOf(false) }
    var textColorAlert by remember { mutableStateOf(false) }
    var backgroundColor by remember { mutableStateOf(Color.Black) }
    var textColor by remember { mutableStateOf(Color.White) }

    Box(modifier.fillMaxWidth()) {
        when (val v = viewModel.searchImages) {
            ResponseResult.Empty -> {}
            is ResponseResult.Error -> {}
            ResponseResult.Loading -> {
                ShimmerEffect(
                    Modifier
                        .padding(horizontal = 15.dp, vertical = 20.dp)
                        .fillMaxSize()
                        .clip(RoundedCornerShape(11.dp))
                )
            }

            is ResponseResult.Success -> {
                val pagerState = rememberPagerState(pageCount = { v.data.size + 1 })

                Box(Modifier.fillMaxSize()) {
                    AndroidView(
                        { view },
                        modifier = Modifier
                            .padding(vertical = 20.dp)
                            .align(Alignment.Center)
                    ) { composeView ->
                        composeView.setContent {
                            Box(Modifier.fillMaxSize()) {
                                HorizontalPager(
                                    state = pagerState,
                                    modifier = Modifier.fillMaxWidth(),
                                    contentPadding = PaddingValues(horizontal = 0.dp),
                                ) { page ->
                                    if (page == 0) Spacer(
                                        Modifier
                                            .padding(horizontal = 10.dp)
                                            .fillMaxSize()
                                            .background(backgroundColor)
                                    )
                                    else GlideImage(
                                        v.data[page - 1],
                                        data?.name,
                                        modifier = Modifier
                                            .padding(horizontal = 10.dp)
                                            .fillMaxSize()
                                            .background(backgroundColor),
                                        contentScale = if (centerCrop) ContentScale.Crop else ContentScale.Fit
                                    )
                                }

                                Column(
                                    Modifier
                                        .align(Alignment.Center)
                                        .alpha(0.79f)
                                        .fillMaxWidth(0.6f)
                                        .clip(RoundedCornerShape(10.dp))
                                        .background(backgroundColor)
                                        .padding(horizontal = 20.dp, vertical = 20.dp)
                                ) {
                                    GlideImage(
                                        data?.thumbnail,
                                        data?.name,
                                        Modifier
                                            .fillMaxWidth()
                                            .aspectRatio(1f),
                                        contentScale = ContentScale.Crop
                                    )

                                    Spacer(Modifier.height(5.dp))
                                    TextViewSemiBold(data?.name ?: "", 18, textColor, line = 2)
                                    TextViewNormal(data?.artists ?: "", 15, textColor, line = 3)
                                    Spacer(Modifier.height(10.dp))
                                    Row(
                                        Modifier.fillMaxWidth(),
                                        Arrangement.Start,
                                        Alignment.CenterVertically
                                    ) {
                                        ImageIcon(R.mipmap.logo_circle, 20, null)
                                        Spacer(Modifier.width(5.dp))
                                        TextViewSemiBold(
                                            stringResource(R.string.app_name), 15, textColor
                                        )
                                    }
                                    Spacer(Modifier.height(5.dp))
                                }
                            }
                        }
                    }


                    Column(
                        Modifier
                            .align(Alignment.BottomEnd)
                            .padding(bottom = 10.dp)
                    ) {
                        Row(horizontalArrangement = Arrangement.End) {
                            ImageWithBorder(R.drawable.ic_image_crop) {
                                centerCrop = !centerCrop
                            }

                            if (pagerState.canScrollForward) ImageWithBorder(R.drawable.ic_arrow_right) {
                                coroutine.launch {
                                    pagerState.animateScrollToPage(pagerState.currentPage + 1)
                                }
                            }
                        }

                        Spacer(Modifier.height(10.dp))

                        Row {
                            ImageWithBorder(R.drawable.ic_colors) {
                                backgroundColorAlert = true
                            }

                            ImageWithBorder(R.drawable.ic_text_color) {
                                textColorAlert = true
                            }
                        }
                    }
                }
            }
        }
    }

    LaunchedEffect(Unit) { data?.let { viewModel.searchImages(it) } }


    if (backgroundColorAlert) BackgroundColorPicker({
        backgroundColorAlert = false
    }, {
        backgroundColor = it
    })

    if (textColorAlert) BackgroundColorPicker({
        textColorAlert = false
    }, {
        textColor = it
    })
}

@SuppressLint("SourceLockedOrientationActivity")
@Composable
fun ShareDataView(data: ZeneMusicData?, close: () -> Unit) {
    Dialog(close, DialogProperties(usePlatformDefaultWidth = false)) {
        val activity = LocalActivity.current
        val context = LocalContext.current
        val view = remember { ComposeView(context) }
        var showQR by remember { mutableStateOf(false) }
        var lastOrientation by remember {
            mutableIntStateOf(
                activity?.requestedOrientation ?: ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
            )
        }
        Column(
            Modifier
                .fillMaxSize()
                .background(Color.Black.copy(0.5f))
        ) {
            ShowSharingImageSlider(Modifier.weight(0.75f), view, data)

            Row(
                Modifier
                    .padding(bottom = 30.dp)
                    .padding(horizontal = 10.dp)
                    .weight(0.2f)
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(13.dp))
                    .background(MainColor)
                    .padding(vertical = 30.dp)
                    .horizontalScroll(rememberScrollState())
            ) {
                ShareRoundIcon(R.drawable.ic_link_backward, R.string.close) {
                    close()
                }

                ShareRoundIcon(R.drawable.ic_link, R.string.copy_link) {
                    ShareContentUtils.shareTheData(data, SharingContentType.COPY, view)
                    close()
                }

                ShareRoundIcon(R.drawable.ic_share, R.string.share_to_) {
                    ShareContentUtils.shareTheData(data, SharingContentType.SHARE_TO, view)
                    close()
                }


//                ShareRoundIcon(R.drawable.ic_hotspot, R.string.connect_) {
//                    if ((userInfo?.phoneNumber?.trim()?.length ?: 0) < 6) {
//                        enableConnect.toast()
//                        return@ShareRoundIcon
//                    }
//                    ShareContentUtils.shareTheData(data, SharingContentType.CONNECT, view)
//                    close()
//                }

                ShareRoundIcon(R.drawable.ic_qr_code, R.string.show_qr) {
                    showQR = true
                }

                ShareRoundIcon(R.drawable.ic_whatsapp, R.string.whatsapp, WhatsAppColor) {
                    ShareContentUtils.shareTheData(data, SharingContentType.WHATS_APP, view)
                    close()
                }

                ShareRoundIcon(R.drawable.ic_instagram, R.string.instagram, InstagramColor) {
                    ShareContentUtils.shareTheData(data, SharingContentType.INSTAGRAM, view)
                    close()
                }

                ShareRoundIcon(R.drawable.ic_snapchat, R.string.snapchat, SnapchatColor) {
                    ShareContentUtils.shareTheData(data, SharingContentType.SNAPCHAT, view)
                    close()
                }

                ShareRoundIcon(R.drawable.ic_facebook_border, R.string.facebook, FacebookColor) {
                    ShareContentUtils.shareTheData(data, SharingContentType.FACEBOOK, view)
                    close()
                }

                ShareRoundIcon(R.drawable.ic_twitter_x, R.string.x, Color.Black) {
                    ShareContentUtils.shareTheData(data, SharingContentType.X, view)
                    close()
                }

                ShareRoundIcon(R.drawable.ic_pinterest, R.string.pinterest, PinterestColor) {
                    ShareContentUtils.shareTheData(data, SharingContentType.PINTEREST, view)
                    close()
                }
            }
        }


        if (showQR) ShowShareQR(data) {
            showQR = false
        }

        DisposableEffect(Unit) {
            activity?.requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
            onDispose { activity?.requestedOrientation = lastOrientation }
        }
    }
}

@SuppressLint("AutoboxingStateCreation")
@OptIn(ExperimentalGlideComposeApi::class)
@Composable
fun ShowShareQR(data: ZeneMusicData?, close: () -> Unit) {
    Dialog(close, DialogProperties(usePlatformDefaultWidth = false)) {
        val context = LocalContext.current.applicationContext

        val activity = LocalActivity.current
        var originalBrightness by remember {
            mutableFloatStateOf(WindowManager.LayoutParams.BRIGHTNESS_OVERRIDE_NONE)
        }

        var showQRDrawable by remember { mutableStateOf<Drawable?>(null) }

        LifecycleResumeEffect(Unit) {
            val window = activity?.window
            if (window != null) {
                originalBrightness = window.attributes.screenBrightness
                val layoutParams = window.attributes
                layoutParams.screenBrightness = 1.0f
                window.attributes = layoutParams
            }
            onPauseOrDispose {
                val layoutParams = activity?.window?.attributes
                if (layoutParams != null) {
                    layoutParams.screenBrightness = originalBrightness
                    activity.window.attributes = layoutParams
                }
            }
        }

        Column(
            Modifier
                .padding(horizontal = 10.dp)
                .clip(RoundedCornerShape(14.dp))
                .background(MainColor)
                .fillMaxWidth()
                .padding(horizontal = 5.dp, vertical = 40.dp),
            Arrangement.Center,
            Alignment.CenterHorizontally
        ) {
            if (showQRDrawable != null) GlideImage(
                showQRDrawable, data?.name, Modifier
                    .fillMaxWidth()
                    .aspectRatio(1f)
                    .clip(RoundedCornerShape(14.dp))
                    .padding(horizontal = 10.dp)
            )


            LaunchedEffect(Unit) {
                val link = generateShareUrl(data)
                val qrData = QrData.Url(link)

                val options = QrVectorOptions.Builder().setPadding(.3f).setLogo(
                    QrVectorLogo(
                        drawable = ContextCompat.getDrawable(context, R.mipmap.logo_circle),
                        size = .25f,
                        padding = QrVectorLogoPadding.Natural(.2f),
                        shape = QrVectorLogoShape.RoundCorners(0.3f)
                    )
                ).setColors(
                    QrVectorColors(
                        dark = QrVectorColor.Solid(Color.White.toArgb()),
                        ball = QrVectorColor.Solid(Color.White.toArgb()),
                        frame = QrVectorColor.LinearGradient(
                            colors = listOf(
                                0f to Color.White.copy(0.4f).toArgb(),
                                1f to Color.White.toArgb(),
                            ),
                            orientation = QrVectorColor.LinearGradient.Orientation.LeftDiagonal
                        )
                    )
                ).setShapes(
                    QrVectorShapes(
                        darkPixel = QrVectorPixelShape.RoundCorners(.0f),
                        ball = QrVectorBallShape.RoundCorners(.2f),
                        frame = QrVectorFrameShape.RoundCorners(.2f),
                    )
                ).build()

                showQRDrawable = QrCodeDrawable(qrData, options)
            }
        }
    }
}

@Composable
fun ShareRoundIcon(icon: Int, text: Int, bg: Color = Color.Gray.copy(0.4f), click: () -> Unit) {
    Column(
        Modifier
            .clickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = null,
                onClick = click
            )
            .width(90.dp), Arrangement.Center, Alignment.CenterHorizontally
    ) {
        Row(
            modifier = Modifier
                .padding(horizontal = 10.dp)
                .clip(RoundedCornerShape(100))
                .background(bg)
                .padding(15.dp), Arrangement.Center, Alignment.CenterVertically
        ) {
            ImageIcon(icon, 23, if (icon == R.drawable.ic_snapchat) Color.Black else Color.White)
        }
        Spacer(Modifier.height(7.dp))
        TextViewNormal(stringResource(text), 13, center = true)
    }
}

@Composable
fun BackgroundColorPicker(close: () -> Unit, changeColor: (Color) -> Unit) {
    Dialog(close, DialogProperties(usePlatformDefaultWidth = false)) {
        val controller = rememberColorPickerController()
        Box(
            Modifier
                .clickable { close() }
                .fillMaxSize(), Alignment.Center) {
            HsvColorPicker(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(450.dp)
                    .padding(10.dp),
                controller = controller,
                onColorChanged = { colorEnvelope ->
                    changeColor(colorEnvelope.color)
                })
        }
    }
}