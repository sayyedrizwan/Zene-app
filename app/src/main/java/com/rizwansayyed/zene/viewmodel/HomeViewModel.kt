package com.rizwansayyed.zene.viewmodel

import androidx.lifecycle.ViewModel
import com.rizwansayyed.zene.ui.login.flow.LoginFlow
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(val loginFlow: LoginFlow): ViewModel() {
}