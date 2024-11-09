package com.rizwansayyed.zene.ui.premium

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.rizwansayyed.zene.R
import com.rizwansayyed.zene.ui.theme.ZeneTheme
import com.rizwansayyed.zene.ui.view.TextPoppins
import com.rizwansayyed.zene.ui.view.TextPoppinsSemiBold
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class PremiumActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            ZeneTheme {
                LazyColumn(
                    Modifier
                        .fillMaxSize()
                        .background(Color.Black)
                        .padding(horizontal = 12.dp)
                ) {
                    item {
                        Spacer(Modifier.height(100.dp))
                        TextPoppinsSemiBold(stringResource(R.string.zene_pro), true, size = 40)
                        Spacer(Modifier.height(2.dp))
                        TextPoppins(stringResource(R.string.zene_pro_desc), true, size = 16)
                        Spacer(Modifier.height(10.dp))
                    }

                    item {

                    }
                }
            }
        }
    }
}
