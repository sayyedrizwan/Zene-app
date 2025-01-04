package com.rizwansayyed.zene.viewmodel

import androidx.lifecycle.ViewModel
import com.rizwansayyed.zene.data.implementation.ZeneAPIInterface
import com.rizwansayyed.zene.ui.login.utils.LoginUtils
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    val loginUtils: LoginUtils, private val zeneAPI: ZeneAPIInterface
) : ViewModel() {


}