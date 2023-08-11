package com.rizwansayyed.zene.ui

import android.view.WindowManager
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.rizwansayyed.zene.R
import com.rizwansayyed.zene.ui.theme.BlackLight
import com.rizwansayyed.zene.ui.theme.PurpleGrey80
import com.rizwansayyed.zene.utils.QuickSandLight
import com.rizwansayyed.zene.utils.QuickSandRegular
import com.rizwansayyed.zene.utils.Utils.showToast


const val windowManagerNoLimit = WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS


@Composable
fun RoundOutlineButtons(vector: ImageVector, text: String, click: () -> Unit) {
    OutlinedButton(
        onClick = click,
        border = BorderStroke(1.dp, PurpleGrey80)
    ) {
        Image(
            imageVector = vector,
            contentDescription = null,
            colorFilter = ColorFilter.tint(Color.White),
            modifier = Modifier.padding(end = 3.dp)
        )
        Text(
            text = text,
            color = Color.White,
            modifier = Modifier.padding(end = 8.dp)
        )
    }
}

@Composable
fun BlackShade() {
    val colorBlack = listOf(
        Color.Transparent, Color(0x66000000), Color(0x99000000), Color(0xCB000000),
        Color(0xCD000000), Color(0xFF000000), Color(0xFF000000), Color.Black
    )

    Spacer(
        modifier = Modifier
            .fillMaxWidth()
            .offset(0.dp, (-135).dp)
            .background(
                brush = Brush.verticalGradient(
                    colors = colorBlack,
                    startY = 0.0f,
                    endY = Float.POSITIVE_INFINITY
                )
            )
            .height(190.dp)

    )
}

@Composable
fun ViewAllBtnView(click: () -> Unit) {
    Card(
        Modifier
            .padding(6.dp)
            .height(intrinsicSize = IntrinsicSize.Max)
            .width(LocalConfiguration.current.screenWidthDp.dp - 140.dp)
            .clickable {
                click()
            },
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = BlackLight),
    ) {
        Column(Modifier.fillMaxSize(), Arrangement.Center, Alignment.CenterHorizontally) {
            Spacer(modifier = Modifier.height(18.dp))

            QuickSandRegular(stringResource(id = R.string.view_all), size = 16)

            Image(
                imageVector = Icons.Default.ArrowForward,
                contentDescription = "",
                modifier = Modifier
                    .padding(8.dp)
                    .size(25.dp),
                colorFilter = ColorFilter.tint(color = Color.White)
            )
            Spacer(modifier = Modifier.height(18.dp))
        }
    }
}