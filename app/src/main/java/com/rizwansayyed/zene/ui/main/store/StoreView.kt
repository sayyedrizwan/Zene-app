package com.rizwansayyed.zene.ui.main.store

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.IconButton
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.bumptech.glide.integration.compose.GlideImage
import com.rizwansayyed.zene.R
import com.rizwansayyed.zene.ui.main.store.view.StoreBannerAdsView
import com.rizwansayyed.zene.ui.main.store.view.StoreChipsTypeView
import com.rizwansayyed.zene.ui.main.store.view.storeChips
import com.rizwansayyed.zene.ui.view.ImageIcon
import com.rizwansayyed.zene.ui.view.TextViewBold
import com.rizwansayyed.zene.ui.view.TextViewNormal

@OptIn(ExperimentalGlideComposeApi::class)
@Composable
fun StoreView() {
    var searchQuery by remember { mutableStateOf("") }
    val selectedCategory = remember { mutableStateOf(storeChips[0]) }
    val focusManager = LocalFocusManager.current

    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(top = 60.dp, bottom = 250.dp)
    ) {
        item {
            Row(Modifier.padding(horizontal = 6.dp)) {
                Column(horizontalAlignment = Alignment.End) {
                    TextViewBold(stringResource(R.string.store).uppercase(), 27)
                    Spacer(Modifier.height(1.dp))
                    GlideImage(R.drawable.ic_amazon_logo, "", Modifier.width(50.dp))
                }
                Spacer(Modifier.weight(1f))
            }
            Spacer(modifier = Modifier.height(20.dp))
        }

        item {
            TextField(
                searchQuery,
                { if (it.length <= 20) searchQuery = it },
                Modifier
                    .padding(10.dp)
                    .fillMaxWidth()
                    .padding(vertical = 4.dp),
                keyboardOptions = KeyboardOptions(imeAction = ImeAction.Search),
                keyboardActions = KeyboardActions {
                    focusManager.clearFocus()
//                    if (searchQuery.length > 3) viewModel.searchZene(searchQuery)
                },
                placeholder = {
                    TextViewNormal(stringResource(R.string.search_product), 16, Color.Black)
                },
                textStyle = TextStyle(fontSize = 16.sp, color = Color.Black),
                trailingIcon = {
                    if (searchQuery.length > 3) {
                        IconButton({
                            focusManager.clearFocus()
//                            viewModel.searchZene(search)
                        }) {
                            ImageIcon(R.drawable.ic_search, 24)
                        }
                    }
                },
                shape = RoundedCornerShape(8.dp),
                colors = TextFieldDefaults.colors(
                    focusedContainerColor = Color.White,
                    unfocusedContainerColor = Color.White,
                    unfocusedIndicatorColor = Color.Transparent,
                    focusedIndicatorColor = Color.Transparent,
                    cursorColor = Color.Black
                ),
                singleLine = true
            )
            Spacer(modifier = Modifier.height(5.dp))
        }

        item {
            StoreChipsTypeView(selectedCategory)
        }

        item {
            Spacer(modifier = Modifier.height(5.dp))
            StoreBannerAdsView()
        }
    }
}