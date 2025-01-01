package com.rizwansayyed.zene.ui.home

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.rizwansayyed.zene.ui.login.LoginView
import com.rizwansayyed.zene.ui.theme.ZeneTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            ZeneTheme {


                LoginView()
            }
        }
    }
}