package com.rizwansayyed.zene.ui.connect_status.view

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.rizwansayyed.zene.R
import com.rizwansayyed.zene.ui.theme.DarkCharcoal
import com.rizwansayyed.zene.ui.theme.MainColor
import com.rizwansayyed.zene.ui.view.TextViewNormal
import com.rizwansayyed.zene.ui.view.TextViewSemiBold

@Composable
fun ConnectStatusTopColumView(content: @Composable (ColumnScope.() -> Unit)) {
    Column(
        Modifier
            .verticalScroll(rememberScrollState())
            .fillMaxSize()
            .background(DarkCharcoal)
    ) {
        content()
    }
}

@Composable
fun ConnectStatusTopHeaderView() {
    Spacer(Modifier.height(70.dp))
    Column(
        Modifier.padding(horizontal = 2.dp), Arrangement.Center, Alignment.Start
    ) {
        TextViewSemiBold(stringResource(R.string.upload_a_vibes), 18)
        TextViewNormal(
            stringResource(R.string.vibes_will_be_shared_and_expire_after_24), 12
        )
    }

    Spacer(Modifier.height(50.dp))
}

@Composable
fun ConnectStatusCaptionView(caption: MutableState<String>) {
    TextField(
        caption.value,
        { if (it.length <= 200) caption.value = it },
        Modifier
            .padding(10.dp)
            .fillMaxWidth()
            .heightIn(min = 100.dp)
            .padding(vertical = 4.dp),
        placeholder = {
            TextViewNormal(stringResource(R.string.add_a_caption), 14)
        },
        shape = RoundedCornerShape(8.dp),
        colors = TextFieldDefaults.colors(
            focusedContainerColor = MainColor,
            unfocusedContainerColor = MainColor,
            unfocusedIndicatorColor = Color.Transparent,
            focusedIndicatorColor = Color.Transparent,
            cursorColor = Color.White
        )
    )
}