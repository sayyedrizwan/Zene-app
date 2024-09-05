package com.rizwansayyed.zene.ui.search

import android.widget.EditText
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import com.rizwansayyed.zene.ui.search.view.SearchInputView
import com.rizwansayyed.zene.ui.search.view.SearchItemView
import com.rizwansayyed.zene.utils.FirebaseLogEvents
import com.rizwansayyed.zene.utils.FirebaseLogEvents.logEvents
import com.rizwansayyed.zene.viewmodel.HomeViewModel

@Composable
fun SearchView(homeViewModel: HomeViewModel, back: () -> Unit) {
    var search by remember { mutableStateOf("") }


    if (search == "")
        SearchInputView(homeViewModel, back) {
            if (it.trim().isEmpty()) return@SearchInputView
            logEvents(FirebaseLogEvents.FirebaseEvents.STARTED_SEARCHING)
            search = it
        }
    else
        SearchItemView(homeViewModel, search, back)



    BackHandler {
        if (search != "") {
            search = ""
            return@BackHandler
        }
        back()
    }
}
