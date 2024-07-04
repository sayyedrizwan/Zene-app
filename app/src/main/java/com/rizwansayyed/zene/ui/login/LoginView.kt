package com.rizwansayyed.zene.ui.login

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import com.rizwansayyed.zene.db.DataStoreManager.userInfoDB

@Composable
fun LoginView(modifier: Modifier = Modifier) {
    val isLogin by userInfoDB.collectAsState(initial = null)

    if (isLogin?.isLoggedIn() == true) LoginViewSpace()
}

@Composable
fun LoginViewSpace(modifier: Modifier = Modifier) {

}