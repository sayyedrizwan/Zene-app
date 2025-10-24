package com.rizwansayyed.zene.ui.main.lux

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.rizwansayyed.zene.R
import com.rizwansayyed.zene.ui.view.TextViewBold
import com.rizwansayyed.zene.ui.view.TextViewNormal

@Composable
fun LuxTypeChipsView() {
    val selected = remember { mutableStateOf(LuxeRSVPData.SUBSCRIBING) }

    Row(
        modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceEvenly
    ) {
        LuxeTypeChipsButton(R.string.subscribing, LuxeRSVPData.SUBSCRIBING, selected)
        LuxeTypeChipsButton(R.string.not_subscribing, LuxeRSVPData.NOT, selected)
    }
    LuxeTypeChipsButton(R.string.maybe, LuxeRSVPData.MAYBE, selected)

    Spacer(modifier = Modifier.height(20.dp))

    when (selected.value) {
        LuxeRSVPData.SUBSCRIBING -> {
            TextViewNormal(
                stringResource(R.string.subscribing_1),
                size = 16, center = true
            )
            Spacer(modifier = Modifier.height(17.dp))
            TextViewNormal(
                stringResource(R.string.subscribing_2),
                size = 16, center = true
            )
            Spacer(modifier = Modifier.height(17.dp))
            TextViewNormal(
                stringResource(R.string.subscribing_3),
                size = 16, center = true
            )
            Spacer(modifier = Modifier.height(17.dp))
            TextViewNormal(
                stringResource(R.string.subscribing_4),
                size = 16, center = true
            )
            Spacer(modifier = Modifier.height(17.dp))
            TextViewNormal(
                stringResource(R.string.subscribing_5),
                size = 16, center = true
            )
        }

        LuxeRSVPData.NOT -> {
            TextViewNormal(
                stringResource(R.string.not_subscribing_1),
                size = 16, center = true
            )
            Spacer(modifier = Modifier.height(17.dp))
            TextViewNormal(
                stringResource(R.string.not_subscribing_2),
                size = 16, center = true
            )
            Spacer(modifier = Modifier.height(17.dp))
            TextViewNormal(
                stringResource(R.string.not_subscribing_3),
                size = 16, center = true
            )
            Spacer(modifier = Modifier.height(17.dp))
            TextViewNormal(
                stringResource(R.string.not_subscribing_4),
                size = 16, center = true
            )
            Spacer(modifier = Modifier.height(17.dp))
            TextViewNormal(
                stringResource(R.string.not_subscribing_5),
                size = 16, center = true
            )
        }

        LuxeRSVPData.MAYBE -> {
            TextViewNormal(
                stringResource(R.string.maybe_subscribing_1),
                size = 16, center = true
            )
            Spacer(modifier = Modifier.height(17.dp))
            TextViewNormal(
                stringResource(R.string.maybe_subscribing_2),
                size = 16, center = true
            )
            Spacer(modifier = Modifier.height(17.dp))
            TextViewNormal(
                stringResource(R.string.maybe_subscribing_3),
                size = 16, center = true
            )
            Spacer(modifier = Modifier.height(17.dp))
            TextViewNormal(
                stringResource(R.string.maybe_subscribing_4),
                size = 16, center = true
            )
            Spacer(modifier = Modifier.height(17.dp))
            TextViewNormal(
                stringResource(R.string.maybe_subscribing_5),
                size = 16, center = true
            )
        }
    }

    Spacer(modifier = Modifier.height(17.dp))
    Spacer(modifier = Modifier.height(16.dp))

}

@Composable
private fun LuxeTypeChipsButton(text: Int, selected: LuxeRSVPData, type: MutableState<LuxeRSVPData>) {
    val bgColor = if (selected == type.value) Color.Green.copy(alpha = 0.7f) else Color.DarkGray

    val icon = when (selected) {
        LuxeRSVPData.SUBSCRIBING -> R.drawable.ic_tick
        LuxeRSVPData.NOT -> R.drawable.ic_cancel_close
        LuxeRSVPData.MAYBE -> R.drawable.ic_help_circle
    }

    Button(
        onClick = {
            type.value = selected
        },
        colors = ButtonDefaults.buttonColors(containerColor = bgColor),
        shape = RoundedCornerShape(50),
        modifier = Modifier.padding(horizontal = 4.dp)
    ) {
        Icon(
            painterResource(icon),
            contentDescription = null,
            tint = Color.White,
            modifier = Modifier.size(19.dp)
        )
        Spacer(modifier = Modifier.width(6.dp))
        TextViewBold(stringResource(text), 17, Color.White)
    }
}

enum class LuxeRSVPData {
    SUBSCRIBING, NOT, MAYBE
}