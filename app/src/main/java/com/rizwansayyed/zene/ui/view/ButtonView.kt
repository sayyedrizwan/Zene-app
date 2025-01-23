package com.rizwansayyed.zene.ui.view

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.rizwansayyed.zene.ui.theme.MainColor

@Composable
fun ButtonWithImageAndBorder(img: Int, txt: Int, border: Color = Color.White, click: () -> Unit) {
    Row(
        modifier = Modifier
            .padding(horizontal = 10.dp)
            .fillMaxWidth()
            .border(0.5.dp, border, RoundedCornerShape(14.dp))
            .padding(vertical = 14.dp)
            .clickable {
                click()
            },
        Arrangement.Center, Alignment.CenterVertically
    ) {
        ImageIcon(img, 27, null)
        Spacer(Modifier.width(14.dp))
        TextViewNormal(stringResource(txt), 16, center = false)
    }
}

@Composable
fun ButtonWithBorder(txt: Int, border: Color = Color.White, click: () -> Unit) {
    Row(
        modifier = Modifier
            .padding(horizontal = 10.dp)
            .border(0.8.dp, border, RoundedCornerShape(14.dp))
            .padding(vertical = 7.dp, horizontal = 14.dp)
            .clickable {
                click()
            },
        Arrangement.Center, Alignment.CenterVertically
    ) {
        TextViewNormal(stringResource(txt), 14, center = false)
    }
}

@Composable
fun ImageWithBorder(img: Int, border: Color = Color.White, click: () -> Unit) {
    Row(
        modifier = Modifier
            .padding(horizontal = 10.dp)
            .border(0.8.dp, border, RoundedCornerShape(14.dp))
            .padding(vertical = 7.dp, horizontal = 14.dp)
            .clickable {
                click()
            },
        Arrangement.Center, Alignment.CenterVertically
    ) {
       ImageIcon(img, 15)
    }
}

@Composable
fun ButtonHeavy(text: String, click: () -> Unit) {
    Row(
        modifier = Modifier
            .padding(horizontal = 10.dp)
            .fillMaxWidth()
            .clickable {
                click()
            }
            .clip(RoundedCornerShape(14.dp))
            .background(MainColor)
            .padding(vertical = 13.dp, horizontal = 5.dp),
        Arrangement.Center, Alignment.CenterVertically
    ) {
        TextViewNormal(text, 16, center = false)
    }
}