package com.rizwansayyed.zene.ui.view

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.ContextWrapper
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.updateTransition
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.gestures.awaitFirstDown
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.gestures.waitForUpOrCancellation
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.selection.TextSelectionColors
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.rounded.Create
import androidx.compose.material.icons.rounded.Search
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SearchBar
import androidx.compose.material3.SearchBarColors
import androidx.compose.material3.SearchBarDefaults
import androidx.compose.material3.TextFieldColors
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.rizwansayyed.zene.R
import com.rizwansayyed.zene.ui.theme.MainColor
import com.rizwansayyed.zene.utils.Utils.toast

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchScreenBar(
    placeholder: Int,
    searchQuery: String,
    onChange: (String) -> Unit,
    onSearch: (String) -> Unit
) {
    SearchBar(
        query = searchQuery,
        onQueryChange = { onChange(it) },
        active = false,
        onActiveChange = {},
        onSearch = { onSearch(it) },
        modifier = Modifier
            .padding(start = 12.dp, top = 2.dp, end = 12.dp, bottom = 12.dp)
            .fillMaxWidth(),
        placeholder = {
            TextPoppins(stringResource(placeholder), false, Color.Gray, 15)
        },
        leadingIcon = {
            Icon(
                imageVector = Icons.Rounded.Search,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.onSurfaceVariant,
            )
        },
        colors = SearchBarDefaults.colors(
            MainColor, MainColor, TextFieldColors(
                Color.White,
                Color.White,
                Color.White,
                Color.White,
                Color.White,
                Color.White,
                Color.White,
                Color.White,
                Color.White,
                Color.White,
                TextSelectionColors(Color.White, Color.White),
                Color.White,
                Color.White,
                Color.White,
                Color.White,
                Color.White,
                Color.White,
                Color.White,
                Color.White,
                Color.White,
                Color.White,
                Color.White,
                Color.White,
                Color.White,
                Color.White,
                Color.White,
                Color.White,
                Color.White,
                Color.White,
                Color.White,
                Color.White,
                Color.White,
                Color.White,
                Color.White,
                Color.White,
                Color.White,
                Color.White,
                Color.White,
                Color.White,
                Color.White,
                Color.White,
                Color.White,
                Color.White
            )
        ),
        tonalElevation = 0.dp,
    ) {}
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PlaylistNameBar(
    searchQuery: String,
    onChange: (String) -> Unit,
    onSearch: (String) -> Unit
) {
    SearchBar(
        query = searchQuery,
        onQueryChange = { onChange(it) },
        active = false,
        onActiveChange = {},
        onSearch = { onSearch(it) },
        modifier = Modifier
            .padding(start = 12.dp, top = 2.dp, end = 12.dp, bottom = 12.dp)
            .fillMaxWidth(),
        placeholder = {
            TextPoppins(stringResource(R.string.playlist_name), false, Color.Gray, 15)
        },
        leadingIcon = {
            Icon(
                imageVector = Icons.Rounded.Create,
                contentDescription = null,
                tint = Color.Black,
            )
        },
        colors = SearchBarDefaults.colors(
            Color.White, Color.White, TextFieldColors(
                Color.Black,
                Color.Black,
                Color.Black,
                Color.Black,
                Color.Black,
                Color.Black,
                Color.Black,
                Color.Black,
                Color.Black,
                Color.Black,
                TextSelectionColors(Color.Black, Color.Black),
                Color.Black,
                Color.Black,
                Color.Black,
                Color.Black,
                Color.Black,
                Color.Black,
                Color.Black,
                Color.Black,
                Color.Black,
                Color.Black,
                Color.Black,
                Color.Black,
                Color.Black,
                Color.Black,
                Color.Black,
                Color.Black,
                Color.Black,
                Color.Black,
                Color.Black,
                Color.Black,
                Color.Black,
                Color.Black,
                Color.Black,
                Color.Black,
                Color.Black,
                Color.Black,
                Color.Black,
                Color.Black,
                Color.Black,
                Color.Black,
                Color.Black,
                Color.Black
            )
        ),
        tonalElevation = 0.dp,
    ) {}
}

@Composable
fun SearchTexts(txt: String, showIcon: Boolean, clicked: (Boolean) -> Unit) {
    Row(
        Modifier
            .padding(top = 17.dp)
            .padding(horizontal = 7.dp)
            .clickable { clicked(true) },
        verticalAlignment = Alignment.CenterVertically
    ) {

        if (showIcon) {
            ImageIcon(R.drawable.ic_go_forward, 15, Color.White)
            Spacer(Modifier.width(9.dp))
        }

        Box(Modifier.weight(1f)) {
            TextPoppins(txt, size = 15)
        }

        Image(
            painterResource(R.drawable.ic_arrow_up_right),
            "",
            Modifier
                .size(20.dp)
                .clickable { clicked(false) },
            contentScale = ContentScale.Fit,
            colorFilter = ColorFilter.tint(Color.White)
        )
    }
}

@Composable
fun BorderButtons(modifier: Modifier = Modifier, icon: Int, s: Int) {
    Row(
        modifier
            .padding(5.dp)
            .border(BorderStroke(1.dp, Color.White), RoundedCornerShape(25))
            .padding(vertical = 7.dp, horizontal = 10.dp),
        Arrangement.Center,
        Alignment.CenterVertically
    ) {
        ImageIcon(icon, 18)

        Spacer(Modifier.width(9.dp))

        TextPoppins(stringResource(id = s), size = 15)
    }
}

@Composable
fun ButtonWithImage(img: Int, txt: Int, click: () -> Unit) {
    Row(
        Modifier
            .padding(horizontal = 20.dp)
            .fillMaxWidth()
            .clip(RoundedCornerShape(15.dp))
            .background(MainColor)
            .clickable { click() }
            .padding(horizontal = 25.dp, vertical = 15.dp),
        Arrangement.Center, Alignment.CenterVertically
    ) {
        ImageIcon(img, 18)

        Spacer(Modifier.width(10.dp))

        TextPoppinsSemiBold(v = stringResource(id = txt), size = 14)
    }
}


enum class ButtonState { Pressed, Idle }

@SuppressLint("ReturnFromAwaitPointerEventScope")
fun Modifier.bounceClick() = composed {
    var buttonState by remember { mutableStateOf(ButtonState.Idle) }
    val scale by animateFloatAsState(
        if (buttonState == ButtonState.Pressed) 0.70f else 1f, label = ""
    )

    this
        .graphicsLayer {
            scaleX = scale
            scaleY = scale
        }
        .clickable(interactionSource = remember { MutableInteractionSource() },
            indication = null,
            onClick = { })
        .pointerInput(buttonState) {
            awaitPointerEventScope {
                buttonState = if (buttonState == ButtonState.Pressed) {
                    waitForUpOrCancellation()
                    ButtonState.Idle
                } else {
                    awaitFirstDown(false)
                    ButtonState.Pressed
                }
            }
        }
}

@OptIn(ExperimentalFoundationApi::class)
@SuppressLint("ModifierFactoryUnreferencedReceiver")
fun Modifier.bouncingClickable(
    scaleDown: Float = 0.9f, onClick: (Boolean) -> Unit
) = composed {
    val interactionSource = remember { MutableInteractionSource() }
    val isPressed by interactionSource.collectIsPressedAsState()

    val animatable = remember { Animatable(1f) }

    LaunchedEffect(key1 = isPressed) {
        if (isPressed) {
            animatable.animateTo(scaleDown)
        } else animatable.animateTo(1f)
    }

    Modifier
        .bounceClick()
        .graphicsLayer {
            val scale = animatable.value
            scaleX = scale
            scaleY = scale
        }
        .combinedClickable(onClick = {
            onClick(true)
        }, onLongClick = {
            onClick(false)
        })
}