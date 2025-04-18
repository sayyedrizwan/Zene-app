package com.rizwansayyed.zene.ui.main.lux

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.rizwansayyed.zene.R
import com.rizwansayyed.zene.ui.view.TextViewBoldBig
import com.rizwansayyed.zene.ui.view.TextViewNormal

@Composable
fun LuxView() {
    val context = LocalContext.current
    val billingManager by remember { mutableStateOf(BillingManager(context)) }

    Column(
        Modifier
            .padding(horizontal = 10.dp)
            .fillMaxWidth()
            .verticalScroll(rememberScrollState())
    ) {
        Spacer(Modifier.height(20.dp))
        TextViewBoldBig(stringResource(R.string.app_name), 120)
        TextViewBoldBig(stringResource(R.string.lux), 120)
        Box(Modifier.padding(horizontal = 10.dp)) {
            TextViewNormal(stringResource(R.string.lux_tag_line), 18)
        }
    }


    LaunchedEffect(Unit) {
        billingManager.startConnection()
    }
}