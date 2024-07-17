package com.rizwansayyed.zene.ui.view

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.rounded.Search
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SearchBar
import androidx.compose.material3.SearchBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.rizwansayyed.zene.R
import com.rizwansayyed.zene.ui.theme.MainColor
import com.rizwansayyed.zene.utils.Utils.toast

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchScreenBar(
    searchQuery: String, onChange: (String) -> Unit, onSearch: (String) -> Unit
) {
    SearchBar(
        query = searchQuery,
        onQueryChange = { onChange(it) },
        active = false,
        onActiveChange = {},
        onSearch = { onSearch(it) },
        modifier = Modifier
            .padding(start = 12.dp, top = 2.dp, end = 12.dp, bottom = 12.dp)
            .fillMaxWidth(),
        placeholder = {
            TextPoppins(stringResource(R.string.search_zene), false, Color.Gray, 15)
        },
        leadingIcon = {
            Icon(
                imageVector = Icons.Rounded.Search,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.onSurfaceVariant,
            )
        },
        colors = SearchBarDefaults.colors(containerColor = MainColor),
        tonalElevation = 0.dp,
    ) {}
}

@Composable
fun SearchTexts(txt: String, showIcon: Boolean, clicked: (Boolean) -> Unit) {
    Row(
        Modifier
            .padding(top = 17.dp)
            .padding(horizontal = 7.dp)
            .clickable { clicked(true) }, verticalAlignment = Alignment.CenterVertically
    ) {

        if (showIcon) {
            ImageIcon(R.drawable.ic_go_forward, 15, Color.White)
            Spacer(Modifier.width(9.dp))
        }

        Box(Modifier.weight(1f)) {
            TextPoppins(txt, size = 15)
        }

        Image(
            painterResource(R.drawable.ic_arrow_up_right), "", Modifier
                .size(20.dp)
                .clickable { clicked(false) }, contentScale = ContentScale.Fit,
            colorFilter = ColorFilter.tint(Color.White)
        )
    }
}